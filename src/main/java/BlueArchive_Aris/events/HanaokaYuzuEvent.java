package BlueArchive_Aris.events;

import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.cards.*;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.patches.EnumPatch;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static BlueArchive_Aris.DefaultMod.makeEventPath;
import static BlueArchive_Aris.events.Object.Paint.saveHandle;

public class HanaokaYuzuEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeID("HanaokaYuzuEvent");
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("HanaokaYuzuEvent.png");
    public int price = 250;

    static public AbstractCard hasCompleteCustomGameCard() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c instanceof CustomGameCard && c.misc == 2) {
                return c;
            }
        }
        return null;
    }


    public HanaokaYuzuEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel >= 15) {
            price = 200;
        }

        this.imageEventText.setDialogOption(OPTIONS[0]);
        if(hasCompleteCustomGameCard() != null) {
            imageEventText.setDialogOption(OPTIONS[1] + price + OPTIONS[2]);
        } else {
            imageEventText.setDialogOption(OPTIONS[3], true);
        }
    }

    public static AbstractCard generateAnyRareCard() {
        AbstractCard tmp;
        while(true) {
            tmp = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE);
            if(tmp != null && tmp.color != AbstractDungeon.player.getCardColor()
                    && tmp.color != AbstractCard.CardColor.COLORLESS
                    && tmp.color != AbstractCard.CardColor.CURSE)
                break;
        }

        return tmp.makeCopy();
    }

    protected void rewardRareCard(){
        ArrayList<AbstractCard> rewardCards = new ArrayList<>();
        while(rewardCards.size() < 3) {
            AbstractCard rewardCard = generateAnyRareCard();
            if(!rewardCards.contains(rewardCard))
                rewardCards.add(rewardCard);
        }
        if (rewardCards != null && !rewardCards.isEmpty()) {
            AbstractDungeon.cardRewardScreen.open(rewardCards, (RewardItem)null, TEXT[4]);
        }
    }

    public boolean hasOldCard() {
        try {
            SpireConfig config = new SpireConfig("BlueArchive_Aris", "SavedCustomCardAbility");
            config.load();
            if(config.has("cost")) {
                return true;
            }
            else {
                return false;
            }

        } catch (Throwable ignored) {
        }
        return false;

    }
    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0:
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.setDialogOption(OPTIONS[6]);
                        rewardRareCard();
                        screenNum = 3;
                        break;
                    case 1:
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.setDialogOption(OPTIONS[7]);
                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                switch (i) {
                    case 0:
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.setDialogOption(hasOldCard()?OPTIONS[5]:OPTIONS[4]);
                        this.imageEventText.setDialogOption(OPTIONS[6]);

                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.RARE);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        AbstractDungeon.player.gainGold(price);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(price));
                        AbstractEvent.logMetricGainGoldAndRelic("HanaokaYuzuEvent", "Join the Contest", relic, price);

                        screenNum = 2;
                        break;
                }
                break;
            case 2:
                switch (i) {
                    case 0:
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.setDialogOption(OPTIONS[6]);
                        AbstractCard c = hasCompleteCustomGameCard();
                        if(c != null) {
                            if(saveHandle.exists()) {
                                Pixmap pixmap = new Pixmap(saveHandle);
                                FileHandle fileHandle = new FileHandle(SpireConfig.makeFilePath("BlueArchive_Aris", "OldRunPaint", "png"));
                                PixmapIO.writePNG(fileHandle, pixmap);
                            }
                            CustomGameCard.saveOldAbility(c.cost, c.type);
                        }
                        screenNum = 3;
                        break;
                    case 1:
                        openMap();
                        break;
                }
                break;
            case 3:
                switch (i) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }

    public void update() {
        super.update();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("RewardItem");
        TEXT = uiStrings.TEXT;
    }
}
