package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.CounterfeitAction;
import もこけね.actions.cards.RevisionAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Counterfeit extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Counterfeit",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int COPIES = 2;
    private static final int UPG_COPIES = 1;

    public Counterfeit()
    {
        super(cardInfo, false);

        setMagic(COPIES, UPG_COPIES);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new CounterfeitAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Counterfeit();
    }
}