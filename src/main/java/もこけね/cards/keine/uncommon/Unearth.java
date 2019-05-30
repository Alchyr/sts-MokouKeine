package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.UnearthAction;
import もこけね.actions.character.OtherPlayerDeckShuffleAction;
import もこけね.character.MokouKeine;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Unearth extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Unearth",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int ENERGY = 1;
    private static final int UPG_ENERGY = 1;

    public Unearth()
    {
        super(cardInfo, false);

        setMagic(ENERGY, UPG_ENERGY);

        this.rawDescription = cardStrings.DESCRIPTION;
        for (int i = 0; i < this.magicNumber; i++)
        {
            this.rawDescription = this.rawDescription.concat(cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        this.rawDescription = this.rawDescription.concat(cardStrings.EXTENDED_DESCRIPTION[1]);
        initializeDescription();
    }

    @Override
    public void upgrade() {
        super.upgrade();

        this.rawDescription = cardStrings.DESCRIPTION;
        for (int i = 0; i < this.magicNumber; i++)
        {
            this.rawDescription = this.rawDescription.concat(cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        this.rawDescription = this.rawDescription.concat(cardStrings.EXTENDED_DESCRIPTION[1]);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean other = TrackCardSource.useOtherEnergy && AbstractDungeon.player instanceof MokouKeine;
        if (other)
        {
            if (((MokouKeine) AbstractDungeon.player).otherPlayerDraw.isEmpty()) {// 41
                AbstractDungeon.actionManager.addToBottom(new OtherPlayerDeckShuffleAction());// 42
            }
        }
        else
        {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());
            }
        }

        AbstractDungeon.actionManager.addToBottom(new UnearthAction(this.magicNumber, other));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Unearth();
    }
}