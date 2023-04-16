package BlueArchive_Aris.patches.power;

import BlueArchive_Aris.cards.OverloadCard;
import BlueArchive_Aris.powers.ChargePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

import static BlueArchive_Aris.powers.JobPower.jobThisCombat;

public class GameActionManagerPatch {

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            //ShuffleCard.totalShuffledThisTurn.set(0);
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ShuffleActionPatch {
        public static void Postfix(GameActionManager __instance)
        {
            ChargePower.chargeThisCombat = 0;
            OverloadCard.overloadThisCombat = 0;
            jobThisCombat.clear();
        }
    }

}
