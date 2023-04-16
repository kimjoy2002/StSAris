package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.JobChangeAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.JobAoePower;
import BlueArchive_Aris.DefaultMod;
import BlueArchive_Aris.patches.EnumPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class SuperNova extends EquipmentCard {

    public static final String ID = DefaultMod.makeID(SuperNova.class.getSimpleName());
    public static final String IMG = makeCardPath("SuperNova.png");


    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 2;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    private static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;


    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public String getOriginalName() {
        return cardStrings.NAME + (upgraded?"+":"");
    }
    public SuperNova() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;

        this.tags.add(EnumPatch.EQUIPMENT);
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new JobChangeAction(this,
                new JobAoePower(p, magicNumber, this, this.multiDamage, this.damageTypeForTurn)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(1);
            initializeDescription();
        }
    }
}
