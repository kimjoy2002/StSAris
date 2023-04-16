package BlueArchive_Aris.actions;

import BlueArchive_Aris.cards.EquipmentCard;
import BlueArchive_Aris.cards.StraightStrike;
import BlueArchive_Aris.powers.JobPower;
import BlueArchive_Aris.relics.ClassChangeRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.Iterator;

public class JobChangeAction extends AbstractGameAction {
    AbstractCard card;
    JobPower power;
    public JobChangeAction(AbstractCard card, JobPower power) {
        this.card = card;
        this.power = power;
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }


    public void update() {
        //CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        //tmp.addToRandomSpot(card);
        AbstractDungeon.player.drawPile.removeCard(card);
        AbstractDungeon.player.hand.removeCard(card);
        AbstractDungeon.player.discardPile.removeCard(card);
        boolean sameJob = false;
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if(p instanceof JobPower) {
                {
                    if(((JobPower)p).equip instanceof EquipmentCard) {
                        ((EquipmentCard)((JobPower)p).equip).str = ((JobPower)p).str;
                        ((EquipmentCard)((JobPower)p).equip).dex = ((JobPower)p).dex;
                        ((EquipmentCard)((JobPower)p).equip).initializeDescription();
                    }
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(((JobPower)p).equip));
                }

                ((EquipmentCard)((JobPower)p).equip).stopGlowing();
                ((EquipmentCard)((JobPower)p).equip).unhover();
                ((EquipmentCard)((JobPower)p).equip).unfadeOut();

                this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p));
                if(power.ID == p.ID) {
                    sameJob = true;
                }
            }
        }



        this.addToBot(new ChangeArisAniAction(power.getAnimation()));


        if(card instanceof EquipmentCard) {
            if(((EquipmentCard) card).str != 0) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, ((EquipmentCard) card).str), ((EquipmentCard) card).str));
                ((JobPower)power).str += ((EquipmentCard) card).str;
            }
            if(((EquipmentCard) card).dex != 0){
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, ((EquipmentCard) card).dex), ((EquipmentCard) card).dex));
                ((JobPower)power).dex += ((EquipmentCard) card).dex;
            }
        }


        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                power));

        if(!sameJob){
            power.onJobChange(true);

            Iterator iter = AbstractDungeon.player.discardPile.group.iterator();

            while(iter.hasNext()) {
                AbstractCard c = (AbstractCard)iter.next();
                if (c instanceof StraightStrike){
                    this.addToBot(new DiscardToHandAction(c));
                }
            }

            Iterator relicIter = AbstractDungeon.player.relics.iterator();
            while(relicIter.hasNext()) {
                AbstractRelic r = (AbstractRelic) relicIter.next();
                if (r instanceof ClassChangeRelic) {
                    ((ClassChangeRelic)r).onClassChange();
                }
            }
        }
        AbstractDungeon.player.hand.refreshHandLayout();

        this.isDone = true;
    }
}
