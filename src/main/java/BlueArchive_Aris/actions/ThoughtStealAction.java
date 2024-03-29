package BlueArchive_Aris.actions;

import BlueArchive_Aris.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.Iterator;

import static BlueArchive_Aris.DefaultMod.copyOriginal;

public class ThoughtStealAction extends AbstractGameAction {
    int amount;
    boolean upgrade;
    public ThoughtStealAction(int amount, boolean upgrade) {
        this.setValues(this.target, this.source, 0);
        this.upgrade = upgrade;
        this.amount =amount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    private AbstractCard generateCard() {
        int roll = AbstractDungeon.cardRandomRng.random(99);
        AbstractCard.CardRarity cardRarity;
        if (roll < 55) {
            cardRarity = AbstractCard.CardRarity.COMMON;
        } else if (roll < 85) {
            cardRarity = AbstractCard.CardRarity.UNCOMMON;
        } else {
            cardRarity = AbstractCard.CardRarity.RARE;
        }

        AbstractCard tmp = CardLibrary.getAnyColorCard(cardRarity);


        if(tmp.color == AbstractDungeon.player.getCardColor() ||
          tmp.color == AbstractCard.CardColor.COLORLESS ||
          tmp.hasTag(AbstractCard.CardTags.HEALING)) {
            return generateCard();
        }

        if(copyOriginal &&
        tmp.color != AbstractCard.CardColor.RED &&
        tmp.color != AbstractCard.CardColor.BLUE &&
        tmp.color != AbstractCard.CardColor.GREEN &&
        tmp.color != AbstractCard.CardColor.PURPLE) {
            return generateCard();
        }

        return tmp.makeCopy();
    }

    public void update() {
        if(upgrade) {
            Iterator var1 = AbstractDungeon.player.hand.group.iterator();

            while(var1.hasNext()) {
                AbstractCard c = (AbstractCard)var1.next();
                if (c.color != AbstractDungeon.player.getCardColor() &&
                        c.color != AbstractCard.CardColor.COLORLESS) {
                    if(c.costForTurn>0){
                        c.costForTurn--;
                    }
                    c.isCostModifiedForTurn = true;
                }
            }
        }

        for(int i = 0; i < amount; i++) {
            AbstractCard card = generateCard();
            if(card.costForTurn>0){
                card.costForTurn--;
            }
            card.isCostModifiedForTurn = true;

            if(!card.exhaust && card.type != AbstractCard.CardType.POWER && card.cost != -2){
                card.exhaust = true;
                card.tags.add(EnumPatch.EXHAUST);
                card.initializeDescription();
            }

            if (AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            } else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
        }

        this.isDone = true;
    }
}
