package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.JobChangeAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.JobWarriorPower;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.patches.EnumPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class Mask extends EquipmentCard {

    public static final String ID = DefaultMod.makeID(Mask.class.getSimpleName());
    public static final String IMG = makeCardPath("Mask.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int BLOCK = 9;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    private static final int MAGIC = 4;


    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public String getOriginalName() {
        return cardStrings.NAME + (upgraded?"+":"");
    }
    public Mask() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = MAGIC;

        this.tags.add(EnumPatch.EQUIPMENT);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new JobChangeAction(this,
                new JobWarriorPower(p, block, magicNumber, this)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
