package BlueArchive_Aris.events;

import BlueArchive_Aris.cards.CustomGameCard;
import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.Iterator;
import java.util.List;

import static BlueArchive_Aris.cards.CustomGameCard.makeCustomCard;
import static BlueArchive_Aris.DefaultMod.makeEventPath;
import static com.megacrit.cardcrawl.cards.AbstractCard.TEXT;
import static com.megacrit.cardcrawl.core.Settings.GameLanguage.ZHS;

public class SaibaMomoiEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeID("SaibaMomoiEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("SaibaMomoiEvent.png");
    public static final String IMG2 = makeEventPath("SaibaMomoiEvent2.png");
    public CUR_SCREEN screen;
    private int count = 0;

    private int heal = 25;
    private int gold = 50;
    private int reroll_gold = 12;
    AbstractCard current_card;


    static public boolean hasCustomGameCard() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c instanceof CustomGameCard && c.misc == 1) {
                return true;
            }
        }
        return false;
    }



    static public boolean isUpgradedCustomGameCard() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c instanceof CustomGameCard) {
                return c.upgraded;
            }
        }
        return false;
    }


    public void ConfirmedCustomGameCard() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard) var2.next();
            if (c instanceof CustomGameCard) {
                c.misc = 2;
                c.cost = current_card.cost;
                c.costForTurn = current_card.cost;
                ((CustomGameCard) c).initializeCustomCard();
                ((CustomGameCard) c).reloadImage();
            }
        }
    }

    public SaibaMomoiEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.screen = CUR_SCREEN.INTRO;

        if (AbstractDungeon.ascensionLevel >= 15) {
            heal = 20;
            reroll_gold = 15;
        }


        if (AbstractDungeon.player.gold >= gold) {
            imageEventText.setDialogOption(OPTIONS[0]+gold+OPTIONS[1]+heal+OPTIONS[2]+(gold*2)+OPTIONS[3]);
        } else {
            imageEventText.setDialogOption(OPTIONS[12]+gold+OPTIONS[13], AbstractDungeon.player.gold < gold);
        }
        imageEventText.setDialogOption(OPTIONS[4]+heal+OPTIONS[5]);
        if (hasCustomGameCard()) {
            imageEventText.setDialogOption(OPTIONS[6]);
        } else {
            imageEventText.setDialogOption(OPTIONS[14], true);
        }
    }


    public void update() {
        super.update();
    }
    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0: {
                        if (AbstractDungeon.miscRng.randomBoolean(0.6F)) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                            this.imageEventText.clearRemainingOptions();
                            AbstractDungeon.player.loseGold(gold);
                            AbstractDungeon.player.gainGold(gold*2);
                            CardCrawlGame.sound.play("GOLD_GAIN");
                            AbstractDungeon.player.heal(heal, true);
                            this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                            AbstractEvent.logMetric("SaibaMomoiEvent", "Make a Bet", (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, 0, heal, 0, 0, gold*2, gold);
                            this.screen = CUR_SCREEN.COMPLETE;
                        } else {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                            this.imageEventText.clearRemainingOptions();
                            AbstractDungeon.player.loseGold(gold);
                            AbstractDungeon.player.heal(heal, true);
                            this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                            AbstractEvent.logMetric("SaibaMomoiEvent", "Make a Bet", (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, 0, heal, 0, 0, 0, gold);
                            this.screen = CUR_SCREEN.COMPLETE;
                        }
                        break;
                    }
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.heal(heal, true);
                        this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                        AbstractEvent.logMetric("SaibaMomoiEvent", "Enjoy the game", (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, 0, heal, 0, 0, 0, 0);
                        this.screen = CUR_SCREEN.COMPLETE;
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        imageEventText.loadImage(IMG2);
                        this.screen = CUR_SCREEN.REROLL;
                        break;
                }
                break;
            case REROLL:
                switch (buttonPressed) {
                    case 0:
                        imageEventText.loadImage(IMG);
                        current_card = makeCustomGameCard();
                        if(isUpgradedCustomGameCard())
                            current_card.upgrade();
                        String text_ = count%3==0?DESCRIPTIONS[5]:(count%3==1?DESCRIPTIONS[6]:DESCRIPTIONS[7]);
                        text_ += " NL NL ";


                        String typetext;
                        switch (current_card.type) {
                            case ATTACK:
                                typetext = TEXT[0];
                                break;
                            case CURSE:
                                typetext = TEXT[3];
                                break;
                            case STATUS:
                                typetext = TEXT[7];
                                break;
                            case SKILL:
                                typetext = TEXT[1];
                                break;
                            case POWER:
                                typetext = TEXT[2];
                                break;
                            default:
                                typetext = TEXT[5];
                        }

                        text_ += " (" + current_card.cost + "/"+typetext+") #g" + current_card.name.replace(" ", " #g");
                        text_ += " NL ";
                        for(DescriptionLine iter : current_card.description) {
                            String text_temp =  iter.text;
                            text_temp = text_temp.replace("*", "#y");
                            text_temp = text_temp.replace(Settings.language==ZHS?" D ":"!D!",String.valueOf(current_card.baseDamage));
                            text_temp = text_temp.replace(Settings.language==ZHS?"!B!!":"!B!",String.valueOf(current_card.baseBlock));
                            text_temp = text_temp.replace(Settings.language==ZHS?"!M!!":"!M!",String.valueOf(current_card.baseMagicNumber));
                            text_ += text_temp;
                            text_ += " NL ";
                        }
                        this.imageEventText.updateBodyText(text_);
                        this.imageEventText.clearRemainingOptions();
                        if(count!=0) {
                            AbstractDungeon.player.loseGold(reroll_gold);
                        }

                        if (AbstractDungeon.player.gold >= reroll_gold) {
                            this.imageEventText.updateDialogOption(0, OPTIONS[8]+reroll_gold+OPTIONS[9]);
                        } else {
                            this.imageEventText.updateDialogOption(0, OPTIONS[12]+reroll_gold+OPTIONS[13], AbstractDungeon.player.gold < reroll_gold);
                        }

                        if(current_card != null) {
                            this.imageEventText.updateDialogOption(1, OPTIONS[10], current_card.makeStatEquivalentCopy());
                        } else {
                            this.imageEventText.updateDialogOption(1, OPTIONS[10]);
                        }
                        this.screen = CUR_SCREEN.REROLL;
                        count++;
                        return;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[8]);
                        this.imageEventText.clearRemainingOptions();
                        if(current_card != null) {
                            ShowCardBrieflyEffect e = new ShowCardBrieflyEffect(current_card.makeStatEquivalentCopy());
                            AbstractDungeon.effectList.add(e);
                        }
                        CustomGameCard.saveAbility(current_card.cost, current_card.type);
                        ConfirmedCustomGameCard();
                        this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                        this.screen = CUR_SCREEN.COMPLETE;
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

    private AbstractCard makeCustomGameCard() {
        AbstractCard.CardType type_ = AbstractDungeon.miscRng.randomBoolean() ? AbstractCard.CardType.ATTACK: AbstractCard.CardType.SKILL;
        int cost = AbstractDungeon.miscRng.randomBoolean(0.4f)?1:(AbstractDungeon.miscRng.randomBoolean(0.5f)?2:(AbstractDungeon.miscRng.randomBoolean(0.66f)?0:3));
        if(type_ == AbstractCard.CardType.SKILL && cost > 2)
            cost = 2;
        int value = cost==0?60:(cost==1?100:(cost==2?200:300));
        value+=AbstractDungeon.miscRng.random(0.9f,1.15f);
        current_card = makeCustomCard(cost, value, type_);
        return current_card;
    }

    public enum CUR_SCREEN {
        INTRO,
        REROLL,
        COMPLETE;

        private CUR_SCREEN() {
        }
    }


}
