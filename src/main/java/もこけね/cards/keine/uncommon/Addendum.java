package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.character.SetEnergyGainAction;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.patch.enums.CustomCardTags;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Addendum extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Addendum",
            2,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 3;

    private static final int DRAW = 1;

    public Addendum()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(DRAW);

        tags.add(CustomCardTags.MK_ECHO_ATTACK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (LastCardType.type == CardType.ATTACK)
        {
            AbstractDungeon.actionManager.addToBottom(new SetEnergyGainAction(TrackCardSource.useOtherEnergy));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(2));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Addendum();
    }
}