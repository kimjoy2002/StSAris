package BlueArchive_Aris.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract  class EquipmentCard extends AbstractDynamicCard {
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(EnhancementScroll.ID);
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    public int str = 0;
    public int dex = 0;

    public static String temp_str = new String();

    public EquipmentCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }
    public void initializeDescription() {
       if(str != 0 || dex != 0) {
            this.name = getOriginalName() + " {" + (str>0?"+":"") + str + ", "+ (dex>0?"+":"") + dex + "}";
            this.initializeTitle();
        } else {
           this.name = getOriginalName();
           this.initializeTitle();
       }
        super.initializeDescription();
    }
    public abstract String getOriginalName();
}
