package もこけね.cards.mokou.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.SummonSparkAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Warmup extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Warmup",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int COST_UPG = 0;

    public Warmup()
    {
        super(cardInfo, false);

        setCostUpgrade(COST_UPG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SummonSparkAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Warmup();
    }
}