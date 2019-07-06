package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.TamperingAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Tampering extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Tampering",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG = 2;
    private static final int UPG_UPG = 1;

    public Tampering()
    {
        super(cardInfo, false);

        setMagic(UPG, UPG_UPG);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new TamperingAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Tampering();
    }
}