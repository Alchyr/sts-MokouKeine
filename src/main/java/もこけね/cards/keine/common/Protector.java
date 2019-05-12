package もこけね.cards.keine.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.ProtectorAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Protector extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Protector",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 2;
    private static final int UPG_BLOCK = 1;

    public Protector()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ProtectorAction(p, this.block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Protector();
    }
}