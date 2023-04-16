package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class Battery extends CustomRelic {

    public static int AMOUNT = 3;
    // ID, images, text.
    public static final String ID = DefaultMod.makeID("Battery");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Battery.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Battery.png"));

    public Battery() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }


    public void onTrigger(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new ShockPower(target, AbstractDungeon.player, AMOUNT)));
    }
}
