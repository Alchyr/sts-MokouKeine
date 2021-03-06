package もこけね.patch.actions;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CtBehavior;
import もこけね.actions.character.WaitForSignalAction;
import もこけね.character.MokouKeine;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.MultiplayerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static もこけね.util.MultiplayerHelper.partnerName;
import static もこけね.もこけねは神の国.makeID;

@SpirePatch(
        clz = GamblingChipAction.class,
        method = "update"
)
public class GamblingChip {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("DiscardWait"));

    private static GamblingChipAction currentAction = null;
    private static HashMap<AbstractCard, Integer> cardIndexes = new HashMap<>();
    private static ArrayList<String> discardMessages = new ArrayList<>();

    @SpirePrefixPatch
    public static SpireReturn waitForPlayer(GamblingChipAction __instance)
    {
        if (currentAction != __instance)
        {
            if (AbstractDungeon.player instanceof MokouKeine && MultiplayerHelper.active)
            {
                MokouKeine p = (MokouKeine)AbstractDungeon.player;
                if (TrackCardSource.useOtherEnergy) //played by other player.
                {
                    AbstractDungeon.actionManager.addToTop(new WaitForSignalAction(uiStrings.TEXT[0] + partnerName + uiStrings.TEXT[1]));
                    __instance.isDone = true;
                    return SpireReturn.Return(null);
                }
                else
                {
                    //Track card indexes in hand for later use
                    cardIndexes.clear();
                    discardMessages.clear();

                    for (int i = 0; i < AbstractDungeon.player.hand.group.size(); ++i)
                    {
                        cardIndexes.put(AbstractDungeon.player.hand.group.get(i), i);
                    }

                    if (!TrackCardSource.useMyEnergy) //this came from a neutral source. Both players have this action. Have to wait.
                    {
                        AbstractDungeon.actionManager.addToTop(new WaitForSignalAction(uiStrings.TEXT[0] + partnerName + uiStrings.TEXT[1]));
                        //Don't cancel this action, though.
                    }
                }
                currentAction = __instance;
            }
        }
        return SpireReturn.Continue();
    }

    @SpireInsertPatch(
        locator = DrawLocator.class
    )
    public static void reportDraw(GamblingChipAction __instance)
    {
        if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
        {
            MultiplayerHelper.sendP2PString("draw" + AbstractDungeon.handCardSelectScreen.selectedCards.group.size());
        }
    }

    @SpireInsertPatch(
        locator = DiscardLocator.class,
            localvars = { "c" }
    )
    public static void reportDiscards(GamblingChipAction __instance, AbstractCard c)
    {
        if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
        {
            if (cardIndexes.containsKey(c))
            {
                int index = cardIndexes.get(c);
                if (index >= 0)
                {
                    discardMessages.add("other_discard" + index); //indexes are not changed until after all discard actions are queued, so this is fine
                }
            }
        }
    }

    @SpireInsertPatch(
        locator = EndLocator.class
    )
    public static void finish(GamblingChipAction __instance)
    {
        if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
        {
            for (String s : discardMessages)
            {
                MultiplayerHelper.sendP2PString(s);
            }
            MultiplayerHelper.sendP2PString("signal");
        }
    }


    private static class DrawLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToTop");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
    private static class DiscardLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "incrementDiscard");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
    private static class EndLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "wereCardsRetrieved");
            return new int[] { LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1] };
        }
    }
}
