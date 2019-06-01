package もこけね.cards.keine.common;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.character.MakeTempCardInOtherHandAction;
import もこけね.cards.colorless.Fragment;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Perusal extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Perusal",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int GENERATE = 1;
    private static final int UPG_GENERATE = 1;

    public Perusal()
    {
        super(cardInfo, true);
        setMagic(GENERATE, UPG_GENERATE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (TrackCardSource.useOtherEnergy && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInOtherHandAction(new Fragment(), this.magicNumber));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Fragment(), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Perusal();
    }
}