package BlueArchive_Aris.cards;

import BlueArchive_Aris.DefaultMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.DefaultMod.makeCardPath;

public class ChooseRefuse extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ChooseRefuse.class.getSimpleName());
    public static final String IMG = makeCardPath("ChooseRefuse.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    AbstractCard card;
    public ChooseRefuse(AbstractCard card) {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.card = card;
    }
    public ChooseRefuse() {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onChoseThisOption() {
        card.misc++;
        card.applyPowers();
    }

    public void upgrade() {
    }

    public AbstractCard makeCopy() {
        return new ChooseRefuse(card);
    }

}
