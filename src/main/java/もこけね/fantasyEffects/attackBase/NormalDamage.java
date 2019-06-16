package もこけね.fantasyEffects.attackBase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.FantasyEffect;
import もこけね.actions.general.DamageRandomConditionalEnemyAction;
import もこけね.cards.colorless.FantasyCard;
import もこけね.util.TextureLoader;

import static もこけね.もこけねは神の国.assetPath;
import static もこけね.もこけねは神の国.makeID;

public class NormalDamage extends FantasyEffect {
    private static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(makeID("FantasyAttackNormal")).EXTENDED_DESCRIPTION;
    private TARGET_TYPE type;

    //first is X, then 0, 1, 2, 3
    private static final int[] DAMAGE = new int[] {
            7,
            5,
            9,
            16,
            24
    };

    private static final int[] UPG_DAMAGE = new int[] {
            3,
            3,
            3,
            4,
            6
    };

    private static final float ALL_MULT = 0.8f;
    private static final float RANDOM_MULT = 1.2f;

    public NormalDamage()
    {
    }

    @Override
    public int register(int id) {
        NormalDamage dmg = new NormalDamage(TARGET_TYPE.SINGLE);
        FantasyEffect.baseAttackEffects.add(dmg);
        FantasyEffect.effectIDs.put(dmg, id++);
        dmg = new NormalDamage(TARGET_TYPE.RANDOM);
        FantasyEffect.baseAttackEffects.add(dmg);
        FantasyEffect.effectIDs.put(dmg, id++);
        dmg = new NormalDamage(TARGET_TYPE.ALL);
        FantasyEffect.baseAttackEffects.add(dmg);
        FantasyEffect.effectIDs.put(dmg, id++);
        return id;
    }

    public NormalDamage(TARGET_TYPE t)
    {
        this.type = t;
    }

    @Override
    public boolean isUsable(FantasyCard base) {
        return true;
    }

    @Override
    public TARGET_TYPE getTargetType() {
        return type;
    }

    @Override
    public String getDescription(FantasyCard c) {
        String end;

        if (c.cost == -1)
            end = TEXT[3];
        else
            end = TEXT[4];

        switch (type)
        {
            case RANDOM:
                return TEXT[1] + end;
            case ALL:
                return TEXT[2] + end;
            default:
                return TEXT[0] + end;
        }
    }

    @Override
    public Texture getTexture(FantasyCard c) {
        switch (type)
        {
            case ALL:
                return TextureLoader.getTexture(assetPath("img/cards/fantasy/attackall.png"));
            case RANDOM:
                return TextureLoader.getTexture(assetPath("img/cards/fantasy/attackrandom.png"));
            default:
                return TextureLoader.getTexture(assetPath("img/cards/fantasy/attacksingle.png"));
        }
    }
    @Override
    public Texture getPortraitTexture(FantasyCard c) {
        switch (type)
        {
            case ALL:
                return TextureLoader.getTexture(assetPath("img/cards/fantasy/attackall_p.png"));
            case RANDOM:
                return TextureLoader.getTexture(assetPath("img/cards/fantasy/attackrandom.png"));
            default:
                return TextureLoader.getTexture(assetPath("img/cards/fantasy/attacksingle_p.png"));
        }
    }

    @Override
    public void initialize(FantasyCard c) {
        int i = c.cost + 1;
        if (i < 0)
            i = 1;

        if (i > 4)
            i = 4;

        float mult = 1.0f;

        if (type == TARGET_TYPE.ALL)
            mult *= ALL_MULT;
        else if (type == TARGET_TYPE.RANDOM)
            mult *= RANDOM_MULT;

        c.setDamage(MathUtils.floor(DAMAGE[i] * mult), UPG_DAMAGE[i]);

        switch (type)
        {
            case ALL:
                c.setMultiDamage(true);
            case RANDOM:
                c.target = AbstractCard.CardTarget.ALL_ENEMY;
                break;
            default:
                c.target = AbstractCard.CardTarget.ENEMY;
        }
    }

    @Override
    public void applyPowers(FantasyCard c) {
    }
    @Override
    public void calculateCardDamage(FantasyCard c, AbstractMonster m) {
    }

    @Override
    public void use(FantasyCard c, AbstractPlayer p, AbstractMonster m) {
        switch (type)
        {
            case SINGLE:
                if (m != null)
                {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, c.damage, c.damageTypeForTurn), c.attackEffect));
                }
                break;
            case ALL:
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, c.multiDamage, c.damageTypeForTurn, c.attackEffect));
                break;
            case RANDOM:
                AbstractDungeon.actionManager.addToBottom(new DamageRandomConditionalEnemyAction((mo)->true, new DamageInfo(p, c.baseDamage, c.damageTypeForTurn), c.attackEffect));
                break;
        }
    }
}
