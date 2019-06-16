package もこけね.cards.keine.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.FantasyAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Fantasy extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Fantasy",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    public Fantasy()
    {
        super(cardInfo, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FantasyAction(this));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fantasy();
    }
}