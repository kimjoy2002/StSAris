package BlueArchive_Aris.patches.cards;

import BlueArchive_Aris.cards.BalanceBroken;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

public class BalanceBrokenPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderHand",
            paramtypez={
                SpriteBatch.class
            }
    )
    public static class renderHandPatcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, SpriteBatch sb) {
            if(__instance.hoveredCard instanceof BalanceBroken) {
                __instance.hoveredCard.calculateCardDamage(null);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredMonster");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
