package BlueArchive_Aris.actions;

import BlueArchive_Aris.ui.ArisTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Aris.DefaultMod.*;

public class ArisTutorialAction extends AbstractGameAction {

    public ArisTutorialAction() {
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (activeTutorial) {
            AbstractDungeon.ftue = new ArisTutorial();
            activeTutorial = false;

            try {
                SpireConfig config = new SpireConfig("BlueArchive_Aris", "BlueArchiveConfig", arisSettings);
                config.setBool(ACTIVE_TUTORIAL, activeTutorial);
                config.save();
            } catch (Exception ignore) {
            }
        }
        this.isDone = true;
    }
}
