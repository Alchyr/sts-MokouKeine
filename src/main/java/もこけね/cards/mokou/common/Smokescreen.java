package もこけね.cards.mokou.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.SummonSparkAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Smokescreen extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Smokescreen",
            2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 9;
    private static final int UPG_BLOCK = 4;

    public Smokescreen()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new SummonSparkAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Smokescreen();
    }
}