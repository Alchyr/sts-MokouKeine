package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.RefuteAction;
import もこけね.actions.cards.RevisionAction;
import もこけね.cards.keine.common.Refute;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Revision extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Revision",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 0;

    public Revision()
    {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new RevisionAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Revision();
    }
}