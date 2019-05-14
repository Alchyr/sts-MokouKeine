package もこけね.cards.keine.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.enums.CustomCardTags;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Stabilize extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Stabilize",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 1;

    private static final int BONUS_BLOCK = 3;
    private static final int UPG_BONUS_BLOCK = 2;

    public Stabilize()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(BONUS_BLOCK, UPG_BONUS_BLOCK);

        tags.add(CustomCardTags.ECHO_SKILL);
    }

    @Override
    public void applyPowers() {
        int originalBlock = this.baseBlock;

        this.baseBlock = this.baseMagicNumber;
        super.applyPowers();

        this.magicNumber = this.block;
        this.isMagicNumberModified = this.isBlockModified;

        this.baseBlock = originalBlock;
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        if (LastCardType.type == CardType.SKILL)
        {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Stabilize();
    }
}