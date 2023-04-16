package BlueArchive_Aris.relics;

import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class HPPotion extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("HPPotion");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HPPotion.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HPPotion.png"));

    public HPPotion() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.SOLID);
        this.counter = 0;
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atPreBattle() {
        this.counter = 0;
    }

    public void wasHPLost(int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.counter++;
        }
    }

    public void onVictory() {
        if(this.counter > 0) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractPlayer p = AbstractDungeon.player;
            if (p.currentHealth > 0) {
                p.heal(this.counter);
            }
        }
    }
}
