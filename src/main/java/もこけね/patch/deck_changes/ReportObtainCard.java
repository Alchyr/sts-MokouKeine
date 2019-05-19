package もこけね.patch.deck_changes;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.daily.mods.Hoarder;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import もこけね.character.MokouKeine;
import もこけね.util.MessageHelper;
import もこけね.util.MultiplayerHelper;

import java.io.Serializable;
import java.util.Iterator;

import static もこけね.util.MessageHelper.cardInfoString;
import static もこけね.もこけねは神の国.chat;
import static もこけね.もこけねは神の国.logger;

public class ReportObtainCard implements Serializable {
    @SpirePatch(
            clz = Soul.class,
            method = "obtain"
    )
    public static class OnObtainCard
    {
        @SpirePrefixPatch
        public static void ReportObtain(Soul __instance, AbstractCard c)
        {
            String msg = "other_obtain_card" + cardInfoString(c);

            MultiplayerHelper.sendP2PString(msg);
            if (ModHelper.isModEnabled(Hoarder.ID)) {
                MultiplayerHelper.sendP2PString(msg);
                MultiplayerHelper.sendP2PString(msg);
            }
        }
    }

    public static void receiveOtherObtainCard(String cardInfo)
    {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            try
            {
                AbstractCard c = MessageHelper.cardFromInfo(cardInfo);

                ((MokouKeine) AbstractDungeon.player).otherPlayerMasterDeck.addToTop(c);
                chat.receiveMessage(MultiplayerHelper.partnerName + " obtained " + c.name);

                for (AbstractRelic r : AbstractDungeon.player.relics)
                {
                    r.onMasterDeckChange();
                }
            }
            catch (Exception e)
            {
                logger.info("Received invalid card information! !Desync! !warning!");
            }
        }
    }

}
