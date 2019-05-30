package もこけね.cards.mokou.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.cards.mokou.rare.Ignition;
import もこけね.powers.IgnitionPower;
import もこけね.powers.InhibitionPower;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Inhibition extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Inhibition",
            2,
            CardType.POWER,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BUFF = 1;

    private static final int UPG_COST = 1;

    public Inhibition()
    {
        super(cardInfo, false);

        setMagic(BUFF);
        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new InhibitionPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Inhibition();
    }
}