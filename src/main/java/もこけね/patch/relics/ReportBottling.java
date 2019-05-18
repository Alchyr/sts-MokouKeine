package もこけね.patch.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import javassist.CtBehavior;
import もこけね.character.MokouKeine;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.MultiplayerHelper;

import static もこけね.util.MultiplayerHelper.partnerName;
import static もこけね.もこけねは神の国.chat;
import static もこけね.もこけねは神の国.makeID;

public class ReportBottling {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Bottling"));
    private static final String[] TEXT = uiStrings.TEXT;

    @SpirePatch(
            clz = BottledFlame.class,
            method = "update"
    )
    public static class Flame
    {
        @SpireInsertPatch(
            locator = Locator.class
        )
        public static void onBottle(BottledFlame __instance)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
            {
                MultiplayerHelper.sendP2PString("bottlef" + AbstractDungeon.player.masterDeck.group.indexOf(__instance.card));
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "inBottleFlame");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = BottledLightning.class,
            method = "update"
    )
    public static class Lightning
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void onBottle(BottledLightning __instance)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
            {
                MultiplayerHelper.sendP2PString("bottlel" + AbstractDungeon.player.masterDeck.group.indexOf(__instance.card));
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "inBottleLightning");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = BottledTornado.class,
            method = "update"
    )
    public static class Tornado
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void onBottle(BottledTornado __instance)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
            {
                MultiplayerHelper.sendP2PString("bottlet" + AbstractDungeon.player.masterDeck.group.indexOf(__instance.card));
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "inBottleTornado");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static void receiveBottling(char bottle, int index)
    {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            AbstractCard c = ((MokouKeine) AbstractDungeon.player).otherPlayerMasterDeck.group.get(index);
            switch (bottle)
            {
                case 'f':
                    chat.receiveMessage(partnerName + TEXT[0] + c.name + TEXT[1] + RelicLibrary.getRelic(BottledFlame.ID).name + TEXT[2]);
                    c.inBottleFlame = true;
                    break;
                case 'l':
                    chat.receiveMessage(partnerName + TEXT[0] + c.name + TEXT[1] + RelicLibrary.getRelic(BottledLightning.ID).name + TEXT[2]);
                    c.inBottleLightning = true;
                    break;
                case 't':
                    chat.receiveMessage(partnerName + TEXT[0] + c.name + TEXT[1] + RelicLibrary.getRelic(BottledTornado.ID).name + TEXT[2]);
                    c.inBottleTornado = true;
                    break;
            }
        }
    }
}
