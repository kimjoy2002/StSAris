package BlueArchive_Aris.actions;


import BlueArchive_Aris.cards.EquipmentCard;
import BlueArchive_Aris.powers.JobPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Iterator;

public class EquipsmithAction extends AbstractGameAction {
    int amount;
    boolean isStrength;
    public EquipsmithAction(int amount, boolean isStrength) {
        this.amount = amount;
        this.isStrength = isStrength;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }
    public void update() {
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if (p instanceof JobPower) {
                if(isStrength) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.amount), this.amount));
                    ((JobPower)p).str += amount;

                    if(((JobPower)p).equip instanceof EquipmentCard) {
                        ((EquipmentCard)((JobPower)p).equip).str += amount;
                        ((EquipmentCard)((JobPower)p).equip).initializeDescription();
                    }
                }
                else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.amount), this.amount));
                    ((JobPower)p).dex += amount;

                    if(((JobPower)p).equip instanceof EquipmentCard) {
                        ((EquipmentCard)((JobPower)p).equip).dex += amount;
                        ((EquipmentCard)((JobPower)p).equip).initializeDescription();
                    }
                }
                this.isDone = true;
                return;
            }
        }
        this.isDone = true;
    }
}
