package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.EndTurnBlockPower;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class GameManual extends CustomRelic {

    public static int AMOUNT = 3;
    // ID, images, text.
    public static final String ID = DefaultMod.makeID("GameManual");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("GameManual.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("GameManual.png"));

    public GameManual() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    public void atTurnStart() {
        int afterBlock = AbstractDungeon.player.currentBlock / 2;
        if(afterBlock > 0) {
            AbstractDungeon.player.loseBlock(afterBlock);
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EndTurnBlockPower(AbstractDungeon.player,  afterBlock),  afterBlock));
            flash();
        }
    }

}
