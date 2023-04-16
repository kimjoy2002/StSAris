package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ClassChangeEvokeAction;
import BlueArchive_Aris.actions.ReturnJobAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class FakeIDCard extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(FakeIDCard.class.getSimpleName());
    public static final String IMG = makeCardPath("FakeIDCard.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;

    public FakeIDCard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ClassChangeEvokeAction());
        this.addToBot(new ReturnJobAction(true));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
