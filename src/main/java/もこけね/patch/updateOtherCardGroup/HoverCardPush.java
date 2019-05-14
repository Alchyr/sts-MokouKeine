package もこけね.patch.updateOtherCardGroup;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.character.MokouKeine;
import もこけね.patch.enums.CharacterEnums;

@SpirePatch(
        clz = CardGroup.class,
        method = "hoverCardPush"
)
public class HoverCardPush {
    @SpirePrefixPatch
    public static SpireReturn pushAlt(CardGroup __instance, AbstractCard c)
    {
        if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && AbstractDungeon.player instanceof MokouKeine)
        {
            if (!__instance.contains(c) && __instance.equals(AbstractDungeon.player.hand))
            {
                ((MokouKeine) AbstractDungeon.player).otherPlayerHand.hoverCardPush(c);
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }
}
