package もこけね.cards.keine.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import もこけね.abstracts.KeineCard;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Untarnished extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Untarnished",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 3;

    private static final int BUFF = 1;

    public Untarnished()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(BUFF);

        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Untarnished();
    }
}