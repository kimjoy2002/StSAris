package BlueArchive_Aris.actions;

import BlueArchive_Aris.cards.EquipmentCard;
import BlueArchive_Aris.powers.JobPower;
import BlueArchive_Aris.powers.WeaponMasterPower2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.Iterator;

public class ReturnJobAction extends AbstractGameAction {

    boolean discard = false;
    public ReturnJobAction(boolean discard) {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.discard = discard;
    }

    public void update() {
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if(p instanceof JobPower) {
                if(discard){
                    if(((JobPower)p).equip instanceof EquipmentCard) {
                        ((EquipmentCard)((JobPower)p).equip).str = ((JobPower)p).str;
                        ((EquipmentCard)((JobPower)p).equip).dex = ((JobPower)p).dex;
                        ((EquipmentCard)((JobPower)p).equip).initializeDescription();
                    }
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(((JobPower)p).equip));
                }
                else {
                    if(((JobPower)p).equip instanceof EquipmentCard) {
                        ((EquipmentCard)((JobPower)p).equip).str = ((JobPower)p).str;
                        ((EquipmentCard)((JobPower)p).equip).dex = ((JobPower)p).dex;
                        ((EquipmentCard)((JobPower)p).equip).initializeDescription();
                    }
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(((JobPower)p).equip));
                }
                if(AbstractDungeon.player.hasPower(WeaponMasterPower2.POWER_ID)) {
                    ((WeaponMasterPower2) AbstractDungeon.player.getPower(WeaponMasterPower2.POWER_ID)).onClassChange(true);
                }
                this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p));
            }
        }
        this.isDone = true;
    }
}
