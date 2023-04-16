package BlueArchive_Aris.actions;


import BlueArchive_Aris.cards.LevelUp;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class RankerAction extends AbstractGameAction {
    private AbstractCard theCard = null;

    public RankerAction(AbstractCard theCard) {
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_MED;
        this.theCard = theCard;
    }

    public void setCost() {
        boolean is_only_rare = true;
        boolean is_only_attack = true;
        for(AbstractCard iter : AbstractDungeon.player.hand.group) {
            if(iter != theCard) {
                if(iter.type == AbstractCard.CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == AbstractCard.CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }
        for(AbstractCard iter : AbstractDungeon.player.hand.group) {
            if(iter != theCard) {
                if(iter.type == AbstractCard.CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == AbstractCard.CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }
        for(AbstractCard iter : AbstractDungeon.player.discardPile.group) {
            if(iter != theCard) {
                if(iter.type == AbstractCard.CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == AbstractCard.CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }

        for(AbstractCard iter : AbstractDungeon.player.drawPile.group) {
            if(iter != theCard) {
                if(iter.type == AbstractCard.CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == AbstractCard.CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }
        if(is_only_attack) {
            this.newCost(0);
        }
        else if(is_only_rare) {
            this.newCost(2);
        }
        else {
            this.newCost(3);
        }
        theCard.initializeDescription();
    }

    protected void newCost(int newBaseCost) {
        int diff = theCard.costForTurn - theCard.cost;
        theCard.cost = newBaseCost;
        if (theCard.costForTurn > 0) {
            theCard.costForTurn = theCard.cost + diff;
        }

        if (theCard.costForTurn < 0) {
            theCard.costForTurn = 0;
        }
    }

    public void update() {
        setCost();
        this.isDone = true;
    }
}
