package もこけね.cards.keine.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.powers.ChroniclePower;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Chronicle extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Chronicle",
            3,
            CardType.POWER,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 2;
    private static final int BUFF = 1;

    public Chronicle()
    {
        super(cardInfo, false);
        setCostUpgrade(UPG_COST);
        setMagic(BUFF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ChroniclePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Chronicle();
    }
}