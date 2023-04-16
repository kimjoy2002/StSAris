package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class ArisBaseRelicPlus extends CustomRelic implements ClassChangeRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ArisBaseRelicPlus");

    public static final int AMOUNT = 1;
    private static boolean usedThisTurn = false;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArisBaseRelicPlus.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArisBaseRelicPlus.png"));

    public ArisBaseRelicPlus() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }

    public void obtain() {
        if (AbstractDungeon.player.hasRelic(ArisBaseRelic.ID)) {
            for(int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (((AbstractRelic)AbstractDungeon.player.relics.get(i)).relicId.equals(ArisBaseRelic.ID)) {
                    this.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }

    }

    public void atPreBattle() {
        usedThisTurn = false;
    }

    @Override
    public void atTurnStart() {
        usedThisTurn = false;
        this.grayscale = false;
    }
    @Override
    public void onClassChange() {
        if(!usedThisTurn) {
            flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, AMOUNT), AMOUNT));
            this.grayscale = true;
            usedThisTurn = true;
        }
    }
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ArisBaseRelic.ID);
    }


}
