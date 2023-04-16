package BlueArchive_Aris.patches.power;

import BlueArchive_Aris.powers.NanoMachinePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

public class NanoMachinePatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez = {
                    DamageInfo.class
            }
    )
    public static class damagePatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"damageAmount"}
        )
        public static void Insert(AbstractPlayer __instance, DamageInfo damageInfo, @ByRef int[] damageAmount) {
            //출력 키워드가 있는지 확인
            if(AbstractDungeon.player.hasPower(NanoMachinePower.POWER_ID) && damageInfo.type != DamageInfo.DamageType.HP_LOSS) {
                AbstractPower nmPower = AbstractDungeon.player.getPower(NanoMachinePower.POWER_ID);
                nmPower.amount+=damageAmount[0];
                if(damageAmount[0] > 0) {
                    nmPower.flash();
                }
                damageAmount[0] = 0;
                nmPower.updateDescription();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "onLoseHpLast");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
