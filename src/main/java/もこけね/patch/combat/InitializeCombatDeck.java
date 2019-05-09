package もこけね.patch.combat;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import もこけね.character.MokouKeine;

@SpirePatch(
        clz = CardGroup.class,
        method = "initializeDeck"
)
public class InitializeCombatDeck {
    @SpirePrefixPatch
    public static SpireReturn preOtherPlayer(CardGroup __instance, CardGroup masterDeck) //Keine's deck is always shuffled first, to ensure consistency in rng.
    {
        if (AbstractDungeon.player instanceof MokouKeine && ((MokouKeine) AbstractDungeon.player).isMokou && !__instance.equals(((MokouKeine) AbstractDungeon.player).otherPlayerDraw))
        {
            ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.initializeDeck(((MokouKeine) AbstractDungeon.player).otherPlayerMasterDeck);
        }
        return SpireReturn.Continue();
    }

    @SpirePostfixPatch
    public static SpireReturn postOtherPlayer(CardGroup __instance, CardGroup masterDeck)
    {
        if (AbstractDungeon.player instanceof MokouKeine && !((MokouKeine) AbstractDungeon.player).isMokou && !__instance.equals(((MokouKeine) AbstractDungeon.player).otherPlayerDraw))
        {
            ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.initializeDeck(((MokouKeine) AbstractDungeon.player).otherPlayerMasterDeck);
        }
        return SpireReturn.Continue();
    }
}
