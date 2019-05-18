package もこけね.abstracts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.character.MokouKeine;

import java.util.HashMap;

import static もこけね.もこけねは神の国.logger;

public abstract class ReceiveSignalCardsAction extends AbstractGameAction {
    public static HashMap<AbstractCard, CardGroup> signaledCards = new HashMap<>();

    public static String signalCardString(AbstractCard c, CardGroup source, boolean otherGroups)
    {
        String sourceType = "";
        switch (source.type)
        {
            case HAND:
                sourceType = "h";
                break;
            case DRAW_PILE:
                sourceType = "d";
                break;
            case DISCARD_PILE:
                sourceType = "c";
                break;
            case EXHAUST_PILE:
                sourceType = "e";
                otherGroups = false;
                break;
        }
        if (sourceType.isEmpty() || !source.contains(c))
            return "";
        return "signalcard" + (otherGroups ? "o" : "m") + sourceType + source.group.indexOf(c);
    }
    public static String signalCardString(int index, CardGroup source, boolean otherGroups)
    {
        String sourceType = "";
        switch (source.type)
        {
            case HAND:
                sourceType = "h";
                break;
            case DRAW_PILE:
                sourceType = "d";
                break;
            case DISCARD_PILE:
                sourceType = "c";
                break;
            case EXHAUST_PILE:
                sourceType = "e";
                otherGroups = false;
                break;
        }
        if (sourceType.isEmpty())
            return "";
        return "signalcard" + (otherGroups ? "o" : "m") + sourceType + index;
    }
    public static void receiveCardString(String data)
    {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            int index = Integer.parseInt(data.substring(2));
            CardGroup source = null;

            switch (data.charAt(0))
            {
                case 'o':
                    switch (data.charAt(1))
                    {
                        case 'h':
                            source = ((MokouKeine) AbstractDungeon.player).otherPlayerHand;
                            break;
                        case 'd':
                            source = ((MokouKeine) AbstractDungeon.player).otherPlayerDraw;
                            break;
                        case 'c':
                            source = ((MokouKeine) AbstractDungeon.player).otherPlayerDiscard;
                            break;
                    }
                    break;
                case 'm':
                    switch (data.charAt(1))
                    {
                        case 'h':
                            source = AbstractDungeon.player.hand;
                            break;
                        case 'd':
                            source = AbstractDungeon.player.drawPile;
                            break;
                        case 'c':
                            source = AbstractDungeon.player.discardPile;
                            break;
                        case 'e':
                            source = AbstractDungeon.player.exhaustPile;
                            break;
                    }
                    break;
                default:
                    logger.error("Invalid signaled card string.");
                    return;
            }

            if (source != null && index >= 0 && index < source.group.size())
            {
                AbstractCard c = source.group.get(index);
                signaledCards.put(c, source);
            }
        }
    }
}
