package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class Grinding extends QuestCard {

    public static final String ID = DefaultMod.makeID(Grinding.class.getSimpleName());
    public static final String IMG = makeCardPath("Grinding.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 1;
    private static final int AMOUNT = 2;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    private static int count = 500;

    public Grinding() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
        this.cardsToPreview = new ExecutionSword();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            if(this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
            makeDescription();
            initializeDescription();
        }
    }

    @Override
    public boolean onQuestCheck(QuestProcess process) {
        if(process == QuestProcess.GAIN_GOLD) {
            makeDescription();
            if(AbstractDungeon.player.gold >= count) {
                return true;
            }
        }
        return false;
    }
    public void questInit() {
        misc = 1;
        makeDescription();
    }
    public void onQuestComplete() {
        super.onQuestComplete();
        ExecutionSword executionSword = new ExecutionSword();
        if (upgraded) {
            executionSword.upgrade();
        }
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(executionSword, (float)(Settings.WIDTH/2), (float)(Settings.HEIGHT / 2)));
    }

    public void makeDescription() {
        this.rawDescription = upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION;
        if(misc > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + (count - AbstractDungeon.player.gold) + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }
}
