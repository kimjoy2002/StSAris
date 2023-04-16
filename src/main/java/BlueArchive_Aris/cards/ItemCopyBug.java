package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ItemCopyAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class ItemCopyBug extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ItemCopyBug.class.getSimpleName());
    public static final String IMG = makeCardPath("ItemCopyBug.png");


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 2;


    public ItemCopyBug() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ItemCopyAction());
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);
            initializeDescription();
        }
    }
}
