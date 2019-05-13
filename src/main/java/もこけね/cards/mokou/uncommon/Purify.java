package もこけね.cards.mokou.uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.SummonSparkAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Purify extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Purify",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int COST = 5;
    private static final int UPG_COST = -2;

    public Purify()
    {
        super(cardInfo, false);

        setMagic(COST, UPG_COST);
        setExhaust(true);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (magicNumber < 0) //infinite journal xd
        {
            this.magicNumber = this.baseMagicNumber = 0;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Purify();
    }
}