package BlueArchive_Aris.relics;

import BlueArchive_Aris.actions.StartingEquipmentAction;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class StartingEquipment extends CustomRelic implements ClassChangeRelic {

    public static final int AMOUNT = 1;
    private static boolean usedThisCombat = false;
    // ID, images, text.
    public static final String ID = DefaultMod.makeID("StartingEquipment");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("StartingEquipment.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("StartingEquipment.png"));

    public StartingEquipment() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
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
            this.addToBot(new StartingEquipmentAction(AMOUNT));
            this.grayscale = true;
            usedThisCombat = true;
        }
    }
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }
}
