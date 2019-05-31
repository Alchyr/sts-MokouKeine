package もこけね.cards.keine.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.util.CardInfo;

import static basemod.helpers.BaseModCardTags.BASIC_STRIKE;
import static もこけね.もこけねは神の国.makeID;

public class KeineStrike extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "K_Strike",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.BASIC
    );
    //1 [0] - Whenever the enemy takes damage this turn, apply 1 Weak to it.
    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;

    public KeineStrike()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);

        tags.add(BASIC_STRIKE);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new KeineStrike();
    }
}