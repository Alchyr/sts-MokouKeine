package もこけね.cards.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.BaseCard;
import もこけね.character.MokouKeine;
import もこけね.patch.enums.CustomCardTags;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Fragment extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Fragment",
            0,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.SPECIAL
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;

    public Fragment()
    {
        super(CardColor.COLORLESS, cardInfo, false);
        setMagic(DAMAGE, UPG_DAMAGE);

        this.tags.add(CustomCardTags.FRAGMENT);
    }


    @Override
    public void applyPowers() {
        int amt = fragmentCount();
        this.baseDamage = this.baseMagicNumber * amt;
        super.applyPowers();

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int amt = fragmentCount();
        this.baseDamage = this.baseMagicNumber * amt;
        super.calculateCardDamage(mo);

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.magicNumber > 0)
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fragment();
    }

    private static int fragmentCount()
    {
        if (AbstractDungeon.player != null)
        {
            int amt = 0;

            if (AbstractDungeon.player instanceof MokouKeine)
            {

                for (AbstractCard c : ((MokouKeine) AbstractDungeon.player).otherPlayerHand.group)
                    if (c.hasTag(CustomCardTags.FRAGMENT))
                        ++amt;

                for (AbstractCard c : ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.group)
                    if (c.hasTag(CustomCardTags.FRAGMENT))
                        ++amt;

                for (AbstractCard c : ((MokouKeine) AbstractDungeon.player).otherPlayerDiscard.group)
                    if (c.hasTag(CustomCardTags.FRAGMENT))
                        ++amt;
            }

            for (AbstractCard c : AbstractDungeon.player.hand.group)
                if (c.hasTag(CustomCardTags.FRAGMENT))
                    ++amt;

            for (AbstractCard c : AbstractDungeon.player.drawPile.group)
                if (c.hasTag(CustomCardTags.FRAGMENT))
                    ++amt;

            for (AbstractCard c : AbstractDungeon.player.discardPile.group)
                if (c.hasTag(CustomCardTags.FRAGMENT))
                    ++amt;
            return amt;
        }
        return 1;
    }
}