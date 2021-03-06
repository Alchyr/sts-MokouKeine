package もこけね.cards.mokou.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.cards.BlockPerAttackPlayedAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Expiation extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Expiation",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 2;

    public Expiation()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
    }

    public void applyPowers() {
        super.applyPowers();

        int count = 0;

        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (c.type == AbstractCard.CardType.ATTACK) {
                ++count;
            }
        }

        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0] + count;
        if (count == 1) {
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
        } else {
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[2];
        }

        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new BlockPerAttackPlayedAction(p, this.block));
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Expiation();
    }
}