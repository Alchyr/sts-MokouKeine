package もこけね.cards.keine.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Anger;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.character.MakeTempCardInOtherDrawAction;
import もこけね.actions.character.MakeTempCardInOtherHandAction;
import もこけね.cards.colorless.Fragment;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.patch.enums.CharacterEnums;
import もこけね.patch.enums.CustomCardTags;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Foresight extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Foresight",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 3;

    public Foresight()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        if (TrackCardSource.useOtherEnergy && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInOtherDrawAction(this.makeStatEquivalentCopy(), 1, true, true));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(this.makeStatEquivalentCopy(), 1, true, true));
        }
    }

    @Override
    public AbstractCard makeCopy() {
            return new Foresight();
        }
}