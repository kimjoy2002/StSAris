package BlueArchive_Aris.relics;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Aris.DefaultMod.*;

public class CopyCat extends CustomRelic implements CustomSavable<Integer> {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("CopyCat");
    public static AbstractCard.CardColor CopyCatColor =  Aris.Enums.COLOR_BLUE;
    public boolean cardSelected = true;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CopyCat.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CopyCat.png"));

    public CopyCat() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public Integer onSave() {
        if (CopyCatColor !=  Aris.Enums.COLOR_BLUE) {
            return CopyCatColor.ordinal();
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardColor) {
        if (cardColor == null) {
            return;
        }
        if(AbstractCard.CardColor.values().length <= cardColor) {
            return;
        }
        CopyCatColor = AbstractCard.CardColor.values()[cardColor];
        applyCharacter(CopyCatColor);
        setDescriptionAfterLoading();
    }


    public void onEquip() {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for(AbstractPlayer char_ :CardCrawlGame.characterManager.getAllCharacters()) {

            if(copyOriginal &&
                    char_.getCardColor() != AbstractCard.CardColor.RED &&
                    char_.getCardColor() != AbstractCard.CardColor.BLUE &&
                    char_.getCardColor() != AbstractCard.CardColor.GREEN &&
                    char_.getCardColor() != AbstractCard.CardColor.PURPLE) {
                continue;
            }

            try {
                if (char_.getCardColor() != Aris.Enums.COLOR_BLUE) {
                    AbstractCard temp = char_.getStartCardForEvent();
                    temp.name = char_.getLocalizedCharacterName();
                    temp.rawDescription = DESCRIPTIONS[3] + char_.getLocalizedCharacterName();
                    temp.initializeDescription();
                    temp.cardsToPreview = null;
                    retVal.addToRandomSpot(temp);
                }
            } catch (Throwable ignore) {
            }

        }
        retVal.shuffle();

        while(retVal.size()>3) {
            retVal.removeTopCard();
        }
        AbstractDungeon.gridSelectScreen.open(retVal, 1, DESCRIPTIONS[1], false, false, false, false);
    }


    public void applyCharacter(AbstractCard.CardColor char_color) {
        if (char_color == AbstractCard.CardColor.BLUE) {
            AbstractDungeon.player.masterMaxOrbs = 3;
        }
        //else if (char_color == Hoshino.Enums.COLOR_PINK) {
        //    BulletUI.useBulletBoolean = true;
        //}
    }

    @Override
    public void update() {
        super.update();
        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            CopyCatColor = card.color;

            {
                AbstractDungeon.combatRewardScreen.open();
                AbstractDungeon.combatRewardScreen.rewards.clear();

                for(AbstractPlayer char_ :CardCrawlGame.characterManager.getAllCharacters()) {
                    if(char_.getCardColor() == CopyCatColor) {
                        for(String relic_ : char_.getStartingRelics()) {
                            AbstractRelic relic = RelicLibrary.getRelic(relic_).makeCopy();
                            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(relic));
                            //AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        }
                        break;
                    }
                }

                AbstractDungeon.combatRewardScreen.positionRewards();
                AbstractDungeon.overlayMenu.proceedButton.setLabel(this.DESCRIPTIONS[2]);
                AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
            }

            applyCharacter(CopyCatColor);

            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();

        }
    }

    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 1;
    }

    public void setDescriptionAfterLoading() {
        for(AbstractPlayer char_ :CardCrawlGame.characterManager.getAllCharacters()) {
            if (char_.getCardColor() == CopyCatColor) {
                this.description = DESCRIPTIONS[5] + char_.getLocalizedCharacterName().replace(" ", " #r") + DESCRIPTIONS[6];
            }
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
}
