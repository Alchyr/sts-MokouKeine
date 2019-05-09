package もこけね.patch.ui;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import もこけね.character.MokouKeine;
import もこけね.ui.OtherDiscardPilePanel;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "closeCurrentScreen"
)
public class ClearDiscardView {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void clearAltDiscard() //static, no arguments
    {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            for (AbstractCard c : ((MokouKeine) AbstractDungeon.player).otherPlayerDiscard.group)
            {
                c.drawScale = 0.12F;
                c.targetDrawScale = 0.12F;
                c.teleportToDiscardPile();
                c.current_y += OtherDiscardPilePanel.ALT_DISCARD_OFFSET;
                c.target_y += OtherDiscardPilePanel.ALT_DISCARD_OFFSET;
                c.darken(true);
                c.unhover();
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "discardPile");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
