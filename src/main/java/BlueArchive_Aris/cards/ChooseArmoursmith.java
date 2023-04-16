package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.EquipsmithAction;
import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class ChooseArmoursmith extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ChooseArmoursmith.class.getSimpleName());
    public static final String IMG = makeCardPath("Armoursmith.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public ChooseArmoursmith(int amount) {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = amount;
    }
    public ChooseArmoursmith() {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onChoseThisOption() {
        this.addToBot(new EquipsmithAction(magicNumber, false));
    }

    public void upgrade() {
    }

    public AbstractCard makeCopy() {
        return new ChooseArmoursmith(magicNumber);
    }

}
