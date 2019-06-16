package もこけね.cards.colorless;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.BaseCard;
import もこけね.abstracts.FantasyEffect;
import もこけね.util.CardInfo;
import もこけね.util.TextureLoader;

import static もこけね.もこけねは神の国.assetPath;
import static もこけね.もこけねは神の国.makeID;

public class FantasyCard extends BaseCard {//implements CustomSavable<String> { i don't actually need these to be savable, because you can't save.
    private final static CardInfo cardInfo = new CardInfo(
            "FantasyPiece",
            -2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.SPECIAL
    );

    public final static String ID = makeID(cardInfo.cardName);

    public FantasyEffect baseEffect;
    public FantasyEffect bonusEffect;

    public AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.FIRE;

    public FantasyCard()
    {
        super(CardColor.COLORLESS, cardInfo, false);
    }

    public FantasyCard(CardType type)
    {
        super(CardColor.COLORLESS, cardInfo.cardName, cardInfo.cardCost, type, cardInfo.cardTarget, cardInfo.cardRarity, false);
    }
    public FantasyCard(CardType type, int cost)
    {
        super(CardColor.COLORLESS, cardInfo.cardName, cost, type, cardInfo.cardTarget, cardInfo.cardRarity, false);
    }

    protected void initializeEffects()
    {
        //set base damage/magic/block from effects
        if (baseEffect != null)
            baseEffect.initialize(this);

        if (bonusEffect != null)
            bonusEffect.initialize(this);

        //construct image from effect images
        String textureString = getTextureString();

        if (!CustomCard.imgMap.containsKey(textureString))
        {
            //create texture and add to list
            Texture t = getCardTexture();

            CustomCard.imgMap.put(textureString, t);
        }

        this.img = this.textureImg = textureString;
        loadCardImage(this.img);

        updateDescription();
    }

    public void updateDescription()
    {
        if (baseEffect != null)
        {
            this.rawDescription = baseEffect.getDescription(this);
        }
        if (bonusEffect != null)
        {
            this.rawDescription += bonusEffect.getDescription(this);
        }
        this.initializeDescription();
    }

    @Override
    protected Texture getPortraitImage() {
        Texture t;
        Pixmap m;

        switch (type)
        {
            case ATTACK:
                t = TextureLoader.getTexture(assetPath("img/cards/fantasy/attack_p.png"));
                break;
            default:
                t = TextureLoader.getTexture(assetPath("img/cards/fantasy/skill_p.png"));
                break;
        }

        if (t != null)
        {
            if (!t.getTextureData().isPrepared())
                t.getTextureData().prepare();

            m = t.getTextureData().consumePixmap();

            if (baseEffect != null)
            {
                t = baseEffect.getPortraitTexture(this);
                if (t != null)
                {
                    if (!t.getTextureData().isPrepared())
                        t.getTextureData().prepare();

                    Pixmap layer = t.getTextureData().consumePixmap();
                    m.drawPixmap(layer, 0, 0);
                    layer.dispose();
                }
            }

            if (bonusEffect != null)
            {
                t = bonusEffect.getPortraitTexture(this);
                if (t != null)
                {
                    if (!t.getTextureData().isPrepared())
                        t.getTextureData().prepare();

                    Pixmap layer = t.getTextureData().consumePixmap();
                    m.drawPixmap(layer, 0, 0);
                    layer.dispose();
                }
            }

            t = new Texture(m);
            m.dispose();
        }

        return t;
    }

    private String getTextureString()
    {
        StringBuilder textureString = new StringBuilder("_");

        switch (this.type)
        {
            case ATTACK:
                textureString.append('a');
                break;
            case SKILL:
                textureString.append('s');
                break;
            default:
                textureString.append('?'); //shouldn't happen.
        }

        if (baseEffect != null)
        {
            textureString.append(FantasyEffect.effectIDs.get(baseEffect));
        }
        if (bonusEffect != null)
        {
            textureString.append(FantasyEffect.effectIDs.get(bonusEffect));
        }

        return textureString.toString();
    }
    public String effectString()
    {
        StringBuilder effectString = new StringBuilder();

        switch (this.type)
        {
            case ATTACK:
                effectString.append("a:");
                break;
            case SKILL:
                effectString.append("s:");
                break;
            default:
                effectString.append("?:"); //shouldn't happen.
        }

        effectString.append(this.cost).append(':');

        switch (this.type)
        {
            case ATTACK:
                if (baseEffect != null)
                    effectString.append(FantasyEffect.baseAttackEffects.indexOf(baseEffect)).append(':');

                if (bonusEffect != null)
                    effectString.append(FantasyEffect.bonusAttackEffects.indexOf(bonusEffect)).append(':');
                break;
            case SKILL:
                if (baseEffect != null)
                    effectString.append(FantasyEffect.baseSkillEffects.indexOf(baseEffect)).append(':');

                if (bonusEffect != null)
                    effectString.append(FantasyEffect.bonusSkillEffects.indexOf(bonusEffect)).append(':');
                break;
        }

        if (effectString.length() > 0) //remove trailing :
            effectString.deleteCharAt(effectString.length() - 1);

        return effectString.toString();
    }
    public static FantasyCard fromEffectString(String effectString)
    {
        String[] params = effectString.split(":");

        CardType type = CardType.SKILL;
        int cost = -2;
        FantasyEffect baseEffect = null;
        FantasyEffect bonusEffect = null;

        if (params.length > 0)
        {
            if (params[0].equals("a"))
            {
                type = CardType.ATTACK;
            }
        }

        if (params.length > 1)
        {
            cost = Integer.parseInt(params[1]);
        }

        switch (type)
        {
            case ATTACK:
                if (params.length > 2)
                    baseEffect = FantasyEffect.baseAttackEffects.get(Integer.parseInt(params[2]));

                if (params.length > 3)
                    bonusEffect = FantasyEffect.bonusAttackEffects.get(Integer.parseInt(params[3]));
                break;
            case SKILL:
                if (params.length > 2)
                    baseEffect = FantasyEffect.baseSkillEffects.get(Integer.parseInt(params[2]));

                if (params.length > 3)
                    bonusEffect = FantasyEffect.bonusSkillEffects.get(Integer.parseInt(params[3]));
                break;
        }

        return new FantasyCard(type, cost).setBaseEffect(baseEffect).setBonusEffect(bonusEffect);
    }

    protected Texture getCardTexture()
    {
        Texture t;
        Pixmap m;
        switch (type)
        {
            case ATTACK:
                t = TextureLoader.getTexture(assetPath("img/cards/fantasy/attack.png"));
                break;
            default:
                t = TextureLoader.getTexture(assetPath("img/cards/fantasy/skill.png"));
                break;
        }
        if (t != null)
        {
            if (!t.getTextureData().isPrepared())
                t.getTextureData().prepare();

            m = t.getTextureData().consumePixmap();

            if (baseEffect != null)
            {
                t = baseEffect.getTexture(this);
                if (t != null)
                {
                    if (!t.getTextureData().isPrepared())
                        t.getTextureData().prepare();

                    Pixmap layer = t.getTextureData().consumePixmap();
                    m.drawPixmap(layer, 0, 0);
                    layer.dispose();
                }
            }

            if (bonusEffect != null)
            {
                t = bonusEffect.getTexture(this);
                if (t != null)
                {
                    if (!t.getTextureData().isPrepared())
                        t.getTextureData().prepare();

                    Pixmap layer = t.getTextureData().consumePixmap();
                    m.drawPixmap(layer, 0, 0);
                    layer.dispose();
                }
            }

            t = new Texture(m);
            m.dispose();
        }

        return t;
    }

    @Override
    public void applyPowers() {
        if (baseEffect != null)
            baseEffect.applyPowers(this);
        super.applyPowers();
        if (bonusEffect != null)
            bonusEffect.applyPowers(this); //If these need to modify values in real-time or modify card text
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (baseEffect != null)
            baseEffect.calculateCardDamage(this, mo);
        super.calculateCardDamage(mo);
        if (bonusEffect != null)
            bonusEffect.calculateCardDamage(this, mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        baseEffect.use(this, p, m);
        bonusEffect.use(this, p, m);
    }


    public FantasyCard setBaseEffect(FantasyEffect baseEffect)
    {
        this.baseEffect = baseEffect;
        this.initializeEffects();
        return this;
    }
    public FantasyCard setBonusEffect(FantasyEffect bonusEffect)
    {
        this.bonusEffect = bonusEffect;
        this.initializeEffects();
        return this;
    }

    @Override
    public AbstractCard makeCopy() {
        return new FantasyCard(this.type, this.cost).setBaseEffect(this.baseEffect).setBonusEffect(this.bonusEffect);
    }
}
