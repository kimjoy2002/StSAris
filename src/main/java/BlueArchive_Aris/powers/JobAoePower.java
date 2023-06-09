package BlueArchive_Aris.powers;

import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;

import static BlueArchive_Aris.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class JobAoePower extends JobPower implements CloneablePowerInterface {
    public int strike_power;
    public int base_strike_power;
    public int[] damage;
    public DamageInfo.DamageType damageTypeForTurn;

    public static final String POWER_ID = DefaultMod.makeID(JobAoePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("JobAoePower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("JobAoePower32.png"));

    public JobAoePower(final AbstractCreature owner, final int amount
          , AbstractCard equip  , final int[] damage, final DamageInfo.DamageType damageTypeForTurn) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.strike_power = amount;
        this.base_strike_power = amount;
        this.equip = equip;
        this.damage = damage;
        this.damageTypeForTurn = damageTypeForTurn;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {


        int amount_ =  this.strike_power;

        if(AbstractDungeon.player.hasPower(LevelUpPower.POWER_ID)) {
            amount_+=this.strike_power*AbstractDungeon.player.getPower(LevelUpPower.POWER_ID).amount;
        }

        return type == DamageInfo.DamageType.NORMAL && card.hasTag(AbstractCard.CardTags.STRIKE) ? damage + (float)amount_ : damage;
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + strike_power + DESCRIPTIONS[1];
    }

    public String getAnimation() {
        return "baseAnimation_AOEDPS";
    }
    public void onJobChange(boolean withEquip) {
        super.onJobChange(withEquip);
        this.addToBot(new SFXAction("ATTACK_DEFECT_BEAM"));
        this.addToBot(new VFXAction(owner, new SweepingBeamEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractDungeon.player.flipHorizontal), 0.4F));
        this.addToBot(new DamageAllEnemiesAction(owner, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    public void levelUp(){
        flash();
        strike_power+=base_strike_power;
        if(amount == -1) {
            amount = 2;
        }
        else {
            amount++;
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new JobAoePower(owner, strike_power, equip, damage, damageTypeForTurn);
    }
}
