package BlueArchive_Aris.patches.power;

import BlueArchive_Aris.powers.FreeCardPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class FreeCardPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "freeToPlay"
    )
    public static class freeToPlayPatcher {
        public static SpireReturn Prefix(AbstractCard __instance) {
            if ( AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hasPower(FreeCardPower.POWER_ID)) {
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }
}
