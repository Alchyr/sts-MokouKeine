package もこけね.cards.mokou.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.powers.IgnitionPower;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Ignition extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Ignition",
            2,
            CardType.POWER,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 1;

    public Ignition()
    {
        super(cardInfo, false);
        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IgnitionPower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Ignition();
    }
}