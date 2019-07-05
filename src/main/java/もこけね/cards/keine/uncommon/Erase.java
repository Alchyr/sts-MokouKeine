package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.EraseAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Erase extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Erase",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    public Erase()
    {
        super(cardInfo, true);

        setExhaust(true, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new EraseAction());
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Erase();
    }
}