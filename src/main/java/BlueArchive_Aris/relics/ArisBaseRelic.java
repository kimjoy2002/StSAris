package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Aris.DefaultMod.*;

public class ArisBaseRelic extends CustomRelic implements ClassChangeRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ArisBaseRelic");

    public static final int AMOUNT = 1;
    private static boolean usedThisCombat = false;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArisBaseRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArisBaseRelic.png"));

    public ArisBaseRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }

    public void atPreBattle() {
        usedThisCombat = false;
    }
    @Override
    public void onClassChange() {
        if(!usedThisCombat) {
            flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, AMOUNT), AMOUNT));
            this.grayscale = true;
            usedThisCombat = true;
        }
    }
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }
}
