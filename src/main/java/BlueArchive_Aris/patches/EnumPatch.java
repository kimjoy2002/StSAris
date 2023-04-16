package BlueArchive_Aris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EnumPatch {
    @SpireEnum
    public static AbstractCard.CardTags EQUIPMENT;
    @SpireEnum
    public static AbstractCard.CardTags ETHEREAL;
    @SpireEnum
    public static AbstractCard.CardTags EXHAUST;


    public EnumPatch() {
    }
}
