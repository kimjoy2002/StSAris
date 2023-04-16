package BlueArchive_Aris.relics;

import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class CoveredKnifeSwitch extends CustomRelic implements OverloadRelic {

    public static int AMOUNT = 3;
    // ID, images, text.
    public static final String ID = DefaultMod.makeID("CoveredKnifeSwitch");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CoveredKnifeSwitch.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CoveredKnifeSwitch.png"));

    public CoveredKnifeSwitch() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }

    public void onOverload() {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, AMOUNT));
        flash();
    }
}
