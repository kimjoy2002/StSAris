package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class Cabinet extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(Cabinet.class.getSimpleName());
    public static final String IMG = makeCardPath("Cabinet.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 2;
    private static final int BLOCK = 12;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;


    public Cabinet() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = MAGIC;
        misc = 0;
    }


    public void applyPowersToBlock() {
        super.applyPowersToBlock();
        this.block += misc * magicNumber;
        if(block != baseBlock)
            isBlockModified = true;
    }

    public void tookDamage() {
        misc++;
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
