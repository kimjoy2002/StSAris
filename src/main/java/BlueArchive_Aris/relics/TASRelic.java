package BlueArchive_Aris.relics;

import BlueArchive_Aris.cards.QuestCard;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

import static BlueArchive_Aris.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Aris.DefaultMod.makeRelicPath;

public class TASRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("TASRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("TAS.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("TAS.png"));

    public TASRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    public void onEquip() {
        QuestCard.questCheck(QuestCard.QuestProcess.TAS);
    }

}
