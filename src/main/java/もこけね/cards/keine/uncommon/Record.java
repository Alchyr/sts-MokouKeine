package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.powers.RecordPower;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Record extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Record",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int COPY = 2;
    private static final int UPG_COPY = 1;

    public Record()
    {
        super(cardInfo, false);

        setMagic(COPY, UPG_COPY);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RecordPower(p, this.magicNumber, TrackCardSource.useOtherEnergy), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Record();
    }
}