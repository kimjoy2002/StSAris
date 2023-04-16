package BlueArchive_Aris.patches.cards;

import BlueArchive_Aris.cards.QuestCard;
import BlueArchive_Aris.patches.EnumPatch;
import BlueArchive_Aris.powers.JobPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.Iterator;

import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_HEIGHT;
import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_WIDTH;

public class JobCardPatch {
    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class useCardActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(UseCardAction __instance) {
            AbstractCard targetCard = (AbstractCard) ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            if(targetCard.hasTag(EnumPatch.EQUIPMENT)) {
                targetCard.exhaustOnUseOnce = false;
                targetCard.dontTriggerOnUseCard = false;
                AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "reboundCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderPowerTips",
            paramtypez = {
                    SpriteBatch.class
            }
    )
    public static class renderPowerTipsPatcher {
        public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
            Iterator powerIter = AbstractDungeon.player.powers.iterator();
            while(powerIter.hasNext()) {
                AbstractPower p = (AbstractPower) powerIter.next();
                if (p instanceof JobPower) {
                    JobPower jp = (JobPower) p;
                    if(jp.equip != null) {
                        float tmpScale = 0.8F;
                        if (__instance.hb.x > (float) Settings.WIDTH * 0.50F) {
                            jp.equip.current_x = __instance.hb.x + (IMG_WIDTH / 2.0F * 0.8F + 16.0F);
                        } else {
                            jp.equip.current_x = __instance.hb.x - (IMG_WIDTH / 2.0F * 0.8F + 16.0F);
                        }

                        jp.equip.current_y = __instance.hb.y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F);
                        jp.equip.drawScale = tmpScale;
                        jp.equip.render(sb);
                    }
                }
            }
        }
    }
}
