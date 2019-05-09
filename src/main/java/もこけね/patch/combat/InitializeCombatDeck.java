package もこけね.patch.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.character.MokouKeine;

@SpirePatch(
        clz = CardGroup.class,
        method = "initializeDeck"
)
public class InitializeCombatDeck {
    @SpirePostfixPatch
    public static SpireReturn GetSplitDeck(CardGroup __instance, CardGroup masterDeck)
    {
        if (AbstractDungeon.player instanceof MokouKeine && !__instance.equals(((MokouKeine) AbstractDungeon.player).otherPlayerDraw))
        {
            //Add other player's cards to other deck.
            ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.initializeDeck(((MokouKeine) AbstractDungeon.player).otherPlayerMasterDeck);
        }
        return SpireReturn.Continue();
    }
}
