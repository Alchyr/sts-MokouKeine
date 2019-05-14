package もこけね.patch.events;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import もこけね.character.MokouKeine;
import もこけね.patch.enums.CharacterEnums;

import static もこけね.もこけねは神の国.logger;

@SpirePatch(
        clz = CardHelper.class,
        method = "hasCardWithXDamage"
)
public class CardDamageCheckBothDecks {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> checkAlt(int damage)
    {
        if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && AbstractDungeon.player instanceof MokouKeine)
        {
            for (AbstractCard c : ((MokouKeine) AbstractDungeon.player).otherPlayerMasterDeck.group)
            {
                if (c.type == AbstractCard.CardType.ATTACK && c.baseDamage >= damage)
                {
                    logger.info(c + " has " + damage + " or more damage.");
                    return SpireReturn.Return(true);
                }
            }
        }
        return SpireReturn.Continue();
    }
}
