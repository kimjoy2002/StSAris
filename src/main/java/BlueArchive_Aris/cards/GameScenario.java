package BlueArchive_Aris.cards;

import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class GameScenario extends CustomCard {

    public List<ability> currentAbility = new ArrayList<ability>();



    public static final String ID = DefaultMod.makeID(GameScenario.class.getSimpleName());
    public static final String ATTACKIMG = makeCardPath("GameScenarioA.png");
    public static final String SKILLIMG = makeCardPath("GameScenario.png");
    private static final CardStrings cardStrings_gs;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int DMG = 0;
    private static final int MAGIC = 0;
    private int UPGRADE_PLUS_DMG = 0;
    private int UPGRADE_PLUS_BLOCK = 0;
    private int UPGRADE_PLUS_MAGIC = 0;

    public boolean alterDamage = false;
    public AbstractCard card;

    public GameScenario(AbstractCard card) {
        super(ID, ATTACKIMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DMG;
        this.card = card;
    }
    public GameScenario() {
        super(ID, ATTACKIMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DMG;
    }

    public void reloadImage() {
        if (type == CardType.SKILL) {
            this.textureImg = SKILLIMG;
        } else  {
            this.textureImg = ATTACKIMG;
        }
        if (textureImg != null) {
            this.loadCardImage(textureImg);
        }
    }
    public void initializeCustomCard() {
        initializeCustomCard(this, currentAbility);
        makeDescription();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m, currentAbility);
    }

    public void triggerWhenDrawn() {
        super.triggerWhenDrawn(currentAbility);
    }
    public void makeDescription() {
        if(!currentAbility.isEmpty()) {
            makeDescription(currentAbility);
            return;
        }
        this.rawDescription = cardStrings_gs.DESCRIPTION;
    }

    static public AbstractCard makeCustomCard(AbstractCard owner, int value, CardType type) {
        GameScenario card = new GameScenario(owner);
        card.type = type;
        card = (GameScenario) makeCustomCard(card, 1, value, type, card.currentAbility, true);
        card.initializeCustomCard();
        card.reloadImage();
        return card;
    }

    public void onChoseThisOption() {
        this.addToBot(new MakeTempCardInHandAction(this, 1));
        this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            super.upgrade();
            initializeDescription();
        }
    }
    public AbstractCard makeStatEquivalentCopy() {
        GameScenario card  = (GameScenario)super.makeStatEquivalentCopy();
        card.currentAbility = currentAbility;
        card.initializeCustomCard();
        card.reloadImage();
        return card;
    }
    static {
        cardStrings_gs = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
