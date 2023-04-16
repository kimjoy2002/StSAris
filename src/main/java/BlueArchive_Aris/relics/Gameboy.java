package BlueArchive_Aris.relics;

import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class Gameboy extends CustomRelic {

    public static int AMOUNT = 50;
    // ID, images, text.
    public static final String ID = DefaultMod.makeID("Gameboy");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Gameboy.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Gameboy.png"));

    public Gameboy() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }


    public void onEquip() {
        int maxHpLoss = MathUtils.ceil((float)AbstractDungeon.player.maxHealth * AMOUNT / 100);
        if (maxHpLoss >= AbstractDungeon.player.maxHealth) {
            maxHpLoss = AbstractDungeon.player.maxHealth - 1;
        }
        AbstractDungeon.player.decreaseMaxHealth(maxHpLoss);
    }

    public void onVictory() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0) {
            p.heal(AbstractDungeon.player.maxHealth);
        }

    }
}
