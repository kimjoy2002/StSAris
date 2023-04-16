package BlueArchive_Aris.events;

import BlueArchive_Aris.cards.CustomCard;
import BlueArchive_Aris.cards.CustomGameCard;
import BlueArchive_Aris.events.Object.Paint;
import BlueArchive_Aris.DefaultMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static BlueArchive_Aris.cards.CustomCard.abilityList;
import static BlueArchive_Aris.cards.CustomGameCard.makeCustomCard;
import static BlueArchive_Aris.events.Object.Paint.saveHandle;
import static BlueArchive_Aris.DefaultMod.makeEventPath;

public class SaibaMidoriEvent extends AbstractImageEvent {

    public static SpireConfig config;
    public static final String ID = DefaultMod.makeID("SaibaMidoriEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("SaibaMidoriEvent.png");

    private float waitTimer = 0.01f;
    public CUR_SCREEN screen;

    private Paint paint = null;
    public boolean isDrawing = false;
    public boolean isMasterPiece = false;
    private boolean cleanUpCalled = false;

    public AbstractCard getOldCard() {

        try {
            FileHandle fileHandle = new FileHandle(SpireConfig.makeFilePath("BlueArchive_Aris", "OldRunPaint", "png"));

            if(fileHandle.exists()) {
                Pixmap pixmap = new Pixmap(fileHandle);
                PixmapIO.writePNG(saveHandle, pixmap);
            } else {
                return null;
            }

            config = new SpireConfig("BlueArchive_Aris", "SavedCustomCardAbility");
            config.load();
            int makeCost;
            AbstractCard.CardType makeType = AbstractCard.CardType.ATTACK;
            CustomGameCard.currentAbility.clear();
            for(CustomCard.ability order : abilityList) {
                if(config.has(order.name)) {
                    int level = config.getInt(order.name);
                    if(level != 0) {
                        CustomCard.ability a = new CustomCard.ability(order);
                        a.level = level;
                        CustomGameCard.currentAbility.add(a);
                    }
                }
            }
            if(config.has("cost")) {
                makeCost = config.getInt("cost");
            }
            else {
                return null;
            }
            if(config.has("type")) {
                String type = config.getString("type");
                if(type.equals("Skill")) {
                    makeType = AbstractCard.CardType.SKILL;
                }
                else {
                    makeType = AbstractCard.CardType.ATTACK;
                }
            }
            AbstractCard card = makeCustomCard(makeCost, makeType);
            CustomGameCard.saveAbility(makeCost, makeType);
            return card;
        } catch (Throwable ignored) {
        }
        return null;
    }


    AbstractCard oldCard = null;
    public SaibaMidoriEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.screen = CUR_SCREEN.INTRO;

        if (AbstractDungeon.ascensionLevel >= 15) {
        }

        if (AbstractDungeon.ascensionLevel == 0) {
            oldCard = getOldCard();
        }

        this.imageEventText.setDialogOption(OPTIONS[0]);
        if(oldCard != null) {
            imageEventText.setDialogOption(OPTIONS[3], oldCard);
        }
        imageEventText.setDialogOption(OPTIONS[1]);
    }


    public void update() {
        super.update();
        if (this.screen == CUR_SCREEN.PLAY) {
            if (this.paint != null) {
                this.paint.update();
            }
        } else if (this.screen == CUR_SCREEN.CLEAN_UP) {
            if (!this.cleanUpCalled) {
                this.cleanUpCalled = true;
                this.paint = null;
            }

            if (this.waitTimer > 0.0F) {
                this.waitTimer -= Gdx.graphics.getDeltaTime();
                if (this.waitTimer < 0.0F) {
                    this.waitTimer = 0.0F;
                    this.screen = CUR_SCREEN.COMPLETE;
                    GenericEventDialog.show();
                    CustomGameCard card = new CustomGameCard(saveHandle, false);
                    AbstractEvent.logMetricObtainCard("SaibaMidoriEvent", "Drawing", card);
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                    this.imageEventText.updateBodyText(isMasterPiece?DESCRIPTIONS[4]:(isDrawing?DESCRIPTIONS[2]:DESCRIPTIONS[3]));
                    this.imageEventText.clearRemainingOptions();
                    this.imageEventText.setDialogOption(OPTIONS[1]);
                }
            }
        }

        if (!GenericEventDialog.waitForInput) {
            this.buttonEffect(GenericEventDialog.getSelectedOption());
        }

    }


    private void placePaint() {
        paint = new Paint(this, 500, 380, Settings.WIDTH / 2, Settings.HEIGHT/ 2+100);
    }
    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.screen = CUR_SCREEN.RULE_EXPLANATION;
                        break;
                    case 1:
                    case 2:
                        if(oldCard != null && buttonPressed == 1) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[6]);
                            this.imageEventText.clearRemainingOptions();
                            this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                            AbstractEvent.logMetricObtainCard("SaibaMidoriEvent", "Get Game", oldCard);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(oldCard, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                            this.screen = CUR_SCREEN.COMPLETE;
                            break;
                        }
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        this.screen = CUR_SCREEN.COMPLETE;
                        break;
                }
                break;
            case RULE_EXPLANATION:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.removeDialogOption(0);
                        GenericEventDialog.hide();
                        this.screen = CUR_SCREEN.PLAY;
                        this.placePaint();
                        return;
                    default:
                        return;
                }
            case COMPLETE:
                switch (buttonPressed) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }

    public void render(SpriteBatch sb) {
        if (this.paint != null) {
            this.paint.render(sb);
        }

        if (this.screen == CUR_SCREEN.PLAY) {
            FontHelper.renderSmartText(sb, FontHelper.panelNameFont, OPTIONS[2], 780.0F * Settings.scale, 80.0F * Settings.scale, 2000.0F * Settings.scale, 0.0F, Color.WHITE);
        }

    }
    public enum CUR_SCREEN {
        INTRO,
        RULE_EXPLANATION,
        PLAY,
        COMPLETE,
        CLEAN_UP;

        private CUR_SCREEN() {
        }
    }


}
