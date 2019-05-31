package もこけね.cards.keine.basic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.OriginateAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Originate extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Originate",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.BASIC
    );
    //1 [0] - Whenever the enemy takes damage this turn, apply 1 Weak to it.
    public final static String ID = makeID(cardInfo.cardName);

    public Originate()
    {
        super(cardInfo, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new OriginateAction(upgraded));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Originate();
    }
}