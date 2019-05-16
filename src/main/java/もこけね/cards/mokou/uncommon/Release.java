package もこけね.cards.mokou.uncommon;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.SummonSparkAction;
import もこけね.powers.SealPower;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Release extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Release",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 0;

    public Release()
    {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(SealPower.POWER_ID))
        {
            int amt = p.getPower(SealPower.POWER_ID).amount;

            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SealPower.POWER_ID));
            for (int i = 0; i < amt; ++i)
            {
                AbstractDungeon.actionManager.addToBottom(new SummonSparkAction());
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Release();
    }
}