package BlueArchive_Aris.powers;

import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Aris.DefaultMod.copyOriginal;
import static BlueArchive_Aris.DefaultMod.makePowerPath;


public class JobIdolPower extends JobPower implements CloneablePowerInterface {
    public int draw_power;
    public int hello_count = 1;

    public static final String POWER_ID = DefaultMod.makeID(JobIdolPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("JobIdolPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("JobIdolPower32.png"));

    public JobIdolPower(final AbstractCreature owner, final int draw_power
          , AbstractCard equip) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.draw_power = draw_power;
        this.equip = equip;
        hello_count = 1;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(hello_count == 1) {
            description = DESCRIPTIONS[0] + hello_count + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + hello_count + DESCRIPTIONS[2];
        }
    }
    public String getAnimation() {
        return "baseAnimation_Idol";
    }
    public void onJobChange(boolean withEquip) {
        super.onJobChange(withEquip);
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, draw_power));
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            int amout_ =  this.hello_count;

            if(AbstractDungeon.player.hasPower(LevelUpPower.POWER_ID)) {
                amout_+=AbstractDungeon.player.getPower(LevelUpPower.POWER_ID).amount;
            }


            for(int i = 0; i < amout_; ++i) {
                AbstractCard card = generateAnyCard(true);
                this.addToBot(new MakeTempCardInHandAction(card.makeCopy(), 1, false));
            }
        }
    }
    public static AbstractCard generateAnyCard(boolean common) {
        AbstractCard tmp;
        while(true) {
            int roll = AbstractDungeon.cardRandomRng.random(99);
            AbstractCard.CardRarity cardRarity;
            if (common || roll < 55) {
                cardRarity = AbstractCard.CardRarity.COMMON;
            } else if (roll < 85) {
                cardRarity = AbstractCard.CardRarity.UNCOMMON;
            } else {
                cardRarity = AbstractCard.CardRarity.RARE;
            }

            tmp = CardLibrary.getAnyColorCard(cardRarity);

            if(copyOriginal &&
                    tmp.color != AbstractCard.CardColor.RED &&
                    tmp.color != AbstractCard.CardColor.BLUE &&
                    tmp.color != AbstractCard.CardColor.GREEN &&
                    tmp.color != AbstractCard.CardColor.PURPLE) {
                continue;
            }

            if(tmp != null && tmp.color != Aris.Enums.COLOR_BLUE
                    && tmp.color != AbstractCard.CardColor.COLORLESS
                    && tmp.color != AbstractCard.CardColor.CURSE)
                break;
        }

        return tmp.makeCopy();
    }

    public void levelUp(){
        flash();
        hello_count++;
        if(amount == -1) {
            amount = 2;
        }
        else {
            amount++;
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new JobIdolPower(owner, draw_power, equip);
    }
}
