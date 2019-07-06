package もこけね.cards.keine.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.FamiliarityAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Familiarity extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Familiarity",
            3,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 2;

    public Familiarity()
    {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FamiliarityAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Familiarity();
    }
}