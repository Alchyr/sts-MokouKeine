package もこけね.cards.mokou.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.powers.SealPower;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Repose extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Repose",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DRAW = 2;
    private static final int UPG_DRAW = 2;

    public Repose()
    {
        super(cardInfo, false);

        setMagic(DRAW, UPG_DRAW);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SealPower(p, 2), 2));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Repose();
    }
}