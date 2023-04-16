package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.DelayDamageAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.effects.CursedWeaponEffect;
import BlueArchive_Aris.DefaultMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class CursedWeapon extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(CursedWeapon.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("CursedWeapon.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3;

    private static final int AMOUNT = 2;
    private static final int AMOUNT2 = 4;
    private static final int UPGRADE_PLUS_AMOUNT2 = 1;


    private ArrayList<CursedWeaponEffect> cwGlowList = new ArrayList<CursedWeaponEffect>();
    private float cwGlowTimer = 0.0F;


    public CursedWeapon() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = AMOUNT;
        secondMagicNumber = baseSecondMagicNumber = AMOUNT2;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    public void triggerOnOtherCardPlayed(AbstractCard c) {
        AbstractDungeon.actionManager.addToBottom(
                new DelayDamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, magicNumber, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        upgradeDamage(secondMagicNumber);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeSecondMagicNumber(UPGRADE_PLUS_AMOUNT2);
            initializeDescription();
        }
    }


    public void render(SpriteBatch sb) {


        super.render(sb);

        if(AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(this)) {
            this.cwGlowTimer -= Gdx.graphics.getDeltaTime();
            if (this.cwGlowTimer < 0.0F) {

                float x_ =  this.current_x + MathUtils.random(-50.0F, 50.0F);

                float y_ = this.current_y + MathUtils.random(-100.0F, 100.0F);

                this.cwGlowList.add(new CursedWeaponEffect(x_, y_));
                this.cwGlowTimer = 0.3F;
            }

            Iterator<CursedWeaponEffect> i = this.cwGlowList.iterator();

            while(i.hasNext()) {
                CursedWeaponEffect e = (CursedWeaponEffect)i.next();
                e.update();
                if (e.isDone) {
                    i.remove();
                }
            }

            if (!Settings.hideCards) {
                Iterator var2 = this.cwGlowList.iterator();

                while(var2.hasNext()) {
                    AbstractGameEffect e = (AbstractGameEffect)var2.next();
                    e.render(sb);
                }

                sb.setBlendFunction(770, 771);
            }
        }



    }



}
