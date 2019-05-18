package もこけね.cards.keine.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.general.PlayCardAction;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.enums.CustomCardTags;
import もこけね.powers.WeightPower;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class AntiquityBurden extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "AntiquityBurden",
            3,
            CardType.SKILL,
            CardTarget.ENEMY,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 2;

    public AntiquityBurden()
    {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeightPower(m), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AntiquityBurden();
    }
}
