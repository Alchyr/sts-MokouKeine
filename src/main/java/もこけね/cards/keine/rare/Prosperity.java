package もこけね.cards.keine.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.general.PlayCardAction;
import もこけね.cards.keine.common.Sinister;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.enums.CustomCardTags;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Prosperity extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Prosperity",
            2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 10;
    private static final int UPG_BLOCK = 4;

    public Prosperity()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);

        tags.add(CustomCardTags.ECHO_POWER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        if (LastCardType.type == CardType.POWER && LastCardType.lastCardCopy != null)
        {
            AbstractDungeon.actionManager.addToBottom(new PlayCardAction(LastCardType.lastCardCopy.makeStatEquivalentCopy(), null, false));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Prosperity();
    }
}
