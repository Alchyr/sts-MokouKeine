package もこけね.character;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BurningBlood;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.vfx.cardManip.CardDisappearEffect;
import もこけね.actions.character.OtherPlayerDeckShuffleAction;
import もこけね.patch.enums.CharacterEnums;
import もこけね.patch.lobby.HandleMatchmaking;
import もこけね.ui.AstrologerOrb;
import もこけね.util.AltHandCardgroup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import static もこけね.ui.OtherDrawPilePanel.OTHER_DRAW_OFFSET;
import static もこけね.もこけねは神の国.*;

public class MokouKeine extends CustomPlayer {
    public static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(makeID("MokouKeine"));

    private static final Color ARROW_COLOR = new Color(1.0F, 0.2F, 0.3F, 1.0F);

    // Base stats
    private static final int ENERGY_PER_TURN = 3;//2; //Total of 4 energy and 6 cards base. As such, strikes and defends are nerfed to 5 damage/4 block.
    private static final int STARTING_HP = 77;
    private static final int MAX_HP = 77;
    private static final int STARTING_GOLD = 100;
    private static final int CARD_DRAW = 6; //automatically split between two groups
    private static final int ORB_SLOTS = 0;

    private static final String SpritePath = assetPath("img/Character/spriter/Character.scml");

    public boolean isMokou;

    private AbstractCard.CardColor cardColor;
    private BitmapFont energyFont;

    private Color cardRenderColor;// = Color.WHITE.cpy();
    private Color cardTrailColor;// = Color.GOLD.cpy();
    private Color slashAttackColor;// = Color.GOLDENROD.cpy();

    private boolean mokouDraw;

    public CardGroup otherPlayerMasterDeck;
    public CardGroup otherPlayerHand;
    public CardGroup otherPlayerDraw;
    public CardGroup otherPlayerDiscard;
    public CardGroup fakeLimbo;

    private Vector2[] points;

    public MokouKeine(boolean mokou) {
        super(mokou ? characterStrings.NAMES[1] : characterStrings.NAMES[2], CharacterEnums.MOKOUKEINE, new AstrologerOrb(),
                new SpriterAnimation(SpritePath));

        this.points = (Vector2[])ReflectionHacks.getPrivate(this, AbstractPlayer.class, "points");

        this.hand = new AltHandCardgroup(true);
        otherPlayerHand = new AltHandCardgroup(false);
        otherPlayerDraw = new CardGroup(CardGroup.CardGroupType.DRAW_PILE);
        otherPlayerDiscard = new CardGroup(CardGroup.CardGroupType.DISCARD_PILE);
        otherPlayerMasterDeck = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
        fakeLimbo = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        this.isMokou = mokou;
        this.mokouDraw = true;
        if (mokou)
        {
            this.cardColor = CharacterEnums.MOKOU;
            this.energyFont = FontHelper.energyNumFontRed;
            this.cardTrailColor = Color.RED.cpy();
            this.cardRenderColor = MOKOU_COLOR.cpy();
            this.slashAttackColor = MOKOU_COLOR.cpy();
        }
        else
        {
            this.cardColor = CharacterEnums.KEINE;
            this.energyFont = FontHelper.energyNumFontBlue;
            this.cardTrailColor = Color.BLUE.cpy();
            this.cardRenderColor = KEINE_COLOR.cpy();
            this.slashAttackColor = KEINE_COLOR.cpy();
        }

        // =============== TEXTURES, ENERGY, LOADOUT =================

        initializeClass(null, // required call to load textures and setup energy/loadout
                assetPath("img/Character/shoulder.png"), // campfire pose
                assetPath("img/Character/shoulder2.png"), // another campfire pose
                assetPath("img/Character/corpse.png"), // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== TEXT BUBBLE LOCATION =================

        this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
        this.dialogY = (this.drawY + 120.0F * Settings.scale);
    }

    public void setMokou(boolean isMokou)
    {
        this.isMokou = isMokou;

        if (isMokou)
        {
            this.name = characterStrings.NAMES[1];
            this.cardColor = CharacterEnums.MOKOU;
            this.energyFont = FontHelper.energyNumFontRed;
            this.cardTrailColor = Color.RED.cpy();
            this.cardRenderColor = MOKOU_COLOR.cpy();
            this.slashAttackColor = MOKOU_COLOR.cpy();
        }
        else
        {
            this.name = characterStrings.NAMES[2];
            this.cardColor = CharacterEnums.KEINE;
            this.energyFont = FontHelper.energyNumFontBlue;
            this.cardTrailColor = Color.BLUE.cpy();
            this.cardRenderColor = KEINE_COLOR.cpy();
            this.slashAttackColor = KEINE_COLOR.cpy();
        }
    }

    @Override
    public void preBattlePrep() {
        this.otherPlayerHand.clear();
        this.otherPlayerDiscard.clear();
        this.mokouDraw = true;
        super.preBattlePrep();
    }


    @Override
    public void draw() {
        if (otherPlayerHand.size() >= BaseMod.MAX_HAND_SIZE && this.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            this.createHandIsFullDialog();
        }
        else
        {
            CardCrawlGame.sound.playAV("CARD_DRAW_8", -0.12F, 0.25F);
            this.draw(1);
            this.onCardDrawOrDiscard();
        }
    }

    public boolean tryDraw()
    {
        if (otherPlayerHand.size() >= BaseMod.MAX_HAND_SIZE && this.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            this.createHandIsFullDialog();
            return false;
        }
        else
        {
            if (!drawPileValid() || isHandFull())
            {
                mokouDraw = !mokouDraw;
                return false;
            }
            CardCrawlGame.sound.playAV("CARD_DRAW_8", -0.12F, 0.25F);
            this.draw(1);
            this.onCardDrawOrDiscard();
            return true;
        }
    }

    public boolean drawPileValid()
    {
        if (mokouDraw ^ isMokou)
        {
            return !otherPlayerDraw.isEmpty();
        }
        else
        {
            return !drawPile.isEmpty();
        }
    }
    public boolean isHandFull()
    {
        if (mokouDraw ^ isMokou)
        {
            return otherPlayerHand.size() >= BaseMod.MAX_HAND_SIZE;
        }
        else
        {
            return hand.size() >= BaseMod.MAX_HAND_SIZE;
        }
    }
    public boolean discardPileEmpty()
    {
        if (mokouDraw ^ isMokou)
        {
            return otherPlayerDiscard.isEmpty();
        }
        else
        {
            return discardPile.isEmpty();
        }
    }
    public AbstractGameAction getShuffleAction()
    {
        if (mokouDraw ^ isMokou)
        {
            return new OtherPlayerDeckShuffleAction();
        }
        else
        {
            return new EmptyDeckShuffleAction();
        }
    }
    public String getOtherPlayerName()
    {
        return isMokou ? characterStrings.NAMES[2] : characterStrings.NAMES[1];
    }

    @Override
    public void draw(int numCards) {
        boolean failedDraw = false;
        for(int i = 0; i < numCards; ++i) {
            if (isHandFull() || !drawPileValid())
            {
                i--; //this draw does nothing, decrement counter.
                if (failedDraw) //If draw is failed twice in a row (Both characters hands are full) break draw loop
                    break;
                failedDraw = true;
            }

            if (mokouDraw ^ isMokou) //One is true, one is false. So, either mokou is drawing and this isn't mokou, or this is mokou and it's keine's draw.
            {
                if (!this.otherPlayerDraw.isEmpty()) {
                    AbstractCard c = this.otherPlayerDraw.getTopCard();
                    c.current_x = CardGroup.DRAW_PILE_X;
                    c.current_y = CardGroup.DRAW_PILE_Y + OTHER_DRAW_OFFSET;
                    c.setAngle(0.0F, true);
                    c.lighten(false);
                    c.drawScale = 0.12F;
                    c.targetDrawScale = 0.75F;
                    if (this.hasPower(ConfusionPower.POWER_ID) && c.cost >= 0) {
                        int newCost = AbstractDungeon.cardRandomRng.random(3);
                        if (c.cost != newCost) {
                            c.cost = newCost;
                            c.costForTurn = c.cost;
                            c.isCostModified = true;
                        }
                    }
                    c.triggerWhenDrawn();
                    this.otherPlayerHand.addToHand(c);
                    this.otherPlayerDraw.removeTopCard();
                    if (this.hasPower("Corruption") && c.type == AbstractCard.CardType.SKILL) {
                        c.setCostForTurn(-99);
                    }
                    for (AbstractRelic r : this.relics)
                    {
                        r.onCardDraw(c);
                    }
                    this.otherPlayerHand.refreshHandLayout();
                    failedDraw = false;
                } else {
                    logger.info("ERROR: How did this happen? No cards in draw pile?? Player.java");
                }
            }
            else //Both are true or both are false.
            {
                if (!this.drawPile.isEmpty()) {
                    AbstractCard c = this.drawPile.getTopCard();
                    c.current_x = CardGroup.DRAW_PILE_X;
                    c.current_y = CardGroup.DRAW_PILE_Y;
                    c.setAngle(0.0F, true);
                    c.lighten(false);
                    c.drawScale = 0.12F;
                    c.targetDrawScale = 0.75F;
                    if (this.hasPower("Confusion") && c.cost >= 0) {
                        int newCost = AbstractDungeon.cardRandomRng.random(3);
                        if (c.cost != newCost) {
                            c.cost = newCost;
                            c.costForTurn = c.cost;
                            c.isCostModified = true;
                        }
                    }
                    c.triggerWhenDrawn();
                    this.hand.addToHand(c);
                    this.drawPile.removeTopCard();
                    if (this.hasPower("Corruption") && c.type == AbstractCard.CardType.SKILL) {
                        c.setCostForTurn(-99);
                    }
                    for (AbstractRelic r : this.relics)
                    {
                        r.onCardDraw(c);
                    }
                    failedDraw = false;
                } else {
                    logger.info("ERROR: How did this happen? No cards in draw pile?? Player.java");
                }
            }
            mokouDraw = !mokouDraw;
        }
    }


    @Override
    public void renderHand(SpriteBatch sb) {
        AbstractMonster hoveredMonster = (AbstractMonster)ReflectionHacks.getPrivate(this, AbstractPlayer.class, "hoveredMonster");

        if (Settings.SHOW_CARD_HOTKEYS) {
            int index = 0;

            for(Iterator var3 = this.hand.group.iterator(); var3.hasNext(); ++index) {
                AbstractCard card = (AbstractCard)var3.next();
                if (index < InputActionSet.selectCardActions.length) {
                    float width = AbstractCard.IMG_WIDTH * card.drawScale / 2.0F;
                    float height = AbstractCard.IMG_HEIGHT * card.drawScale / 2.0F;
                    float topOfCard = card.current_y + height;
                    float textSpacing = 50.0F * Settings.scale;
                    float textY = topOfCard + textSpacing;
                    float sin = (float)Math.sin((double)(card.angle / 180.0F) * 3.141592653589793D);
                    float xOffset = sin * width;
                    FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, InputActionSet.selectCardActions[index].getKeyString(), card.current_x - xOffset, textY, Settings.CREAM_COLOR);
                }
            }
        }

        if (this.inspectMode && this.inspectHb != null) {
            this.renderReticle(sb, this.inspectHb);
        }

        if (this.hoveredCard != null) {
            int aliveMonsters = 0;
            this.hand.renderHand(sb, this.hoveredCard);
            this.hoveredCard.renderHoverShadow(sb);
            if ((this.isDraggingCard || this.inSingleTargetMode) && this.isHoveringDropZone) {
                if (this.isDraggingCard && !this.inSingleTargetMode) {
                    AbstractMonster theMonster = null;
                    Iterator var4 = AbstractDungeon.getMonsters().monsters.iterator();

                    while(var4.hasNext()) {
                        AbstractMonster m = (AbstractMonster)var4.next();
                        if (!m.isDying && m.currentHealth > 0) {
                            ++aliveMonsters;
                            theMonster = m;
                        }
                    }

                    if (aliveMonsters == 1 && hoveredMonster == null) {
                        this.hoveredCard.calculateCardDamage(theMonster);
                        this.hoveredCard.render(sb);
                        this.hoveredCard.applyPowers();
                    } else {
                        this.hoveredCard.render(sb);
                    }
                }

                if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
                    try
                    {
                        Method hoverReticleMethod = AbstractPlayer.class.
                                getDeclaredMethod("renderHoverReticle", SpriteBatch.class);

                        hoverReticleMethod.setAccessible(true);

                        hoverReticleMethod.invoke(this, sb);
                    }
                    catch (Exception e)
                    {
                        logger.error(e.getMessage());
                    }
                }
            }

            if (hoveredMonster != null) {
                this.hoveredCard.calculateCardDamage(hoveredMonster);
                this.hoveredCard.render(sb);
                this.hoveredCard.applyPowers();
            } else if (aliveMonsters != 1) {
                this.hoveredCard.render(sb);
            }
            this.otherPlayerHand.renderHand(sb, this.cardInUse);
        } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
            this.hand.render(sb);
        } else {
            this.hand.renderHand(sb, this.cardInUse);
            this.otherPlayerHand.renderHand(sb, this.cardInUse);
        }

        if (this.cardInUse != null && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT) {
            this.cardInUse.render(sb);
            if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                AbstractDungeon.effectList.add(new CardDisappearEffect(this.cardInUse.makeCopy(), this.cardInUse.current_x, this.cardInUse.current_y));
                this.cardInUse = null;
            }
        }

        this.limbo.render(sb);

        if (this.inSingleTargetMode && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getCurrRoom().isBattleEnding()) {
            float arrowX = (float) ReflectionHacks.getPrivate(this, AbstractPlayer.class, "arrowX");
            float arrowY = (float) ReflectionHacks.getPrivate(this, AbstractPlayer.class, "arrowY");
            float arrowScale = Settings.scale;

            arrowX = MathHelper.mouseLerpSnap(arrowX, (float)InputHelper.mX);
            arrowY = MathHelper.mouseLerpSnap(arrowY, (float)InputHelper.mY);
            ReflectionHacks.setPrivate(this, AbstractPlayer.class, "arrowX", arrowX);
            ReflectionHacks.setPrivate(this, AbstractPlayer.class, "arrowY", arrowY);
            Vector2 controlPoint = new Vector2(this.hoveredCard.current_x - (arrowX - this.hoveredCard.current_x) / 4.0F, arrowY + (arrowY - this.hoveredCard.current_y) / 2.0F);
            if (hoveredMonster == null) {
                ReflectionHacks.setPrivate(this, AbstractPlayer.class, "arrowScale", Settings.scale);
                ReflectionHacks.setPrivate(this, AbstractPlayer.class, "arrowScaleTimer", 0.0F);
                sb.setColor(Color.WHITE);
            } else {
                float timer = (float)ReflectionHacks.getPrivate(this, AbstractPlayer.class, "arrowScaleTimer") + Gdx.graphics.getDeltaTime();
                if (timer > 1.0F)
                    timer = 1.0F;
                ReflectionHacks.setPrivate(this, AbstractPlayer.class, "arrowScaleTimer", timer);

                arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, timer);
                ReflectionHacks.setPrivate(this, AbstractPlayer.class, "arrowScale", arrowScale);
                sb.setColor(ARROW_COLOR);
            }

            Vector2 tmp = new Vector2(controlPoint.x - arrowX, controlPoint.y - arrowY);
            tmp.nor();
            drawCurvedLine(sb, controlPoint, new Vector2(this.hoveredCard.current_x, this.hoveredCard.current_y), new Vector2(arrowX, arrowY), controlPoint);
            sb.draw(ImageMaster.TARGET_UI_ARROW, arrowX - 128.0F, arrowY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, arrowScale, arrowScale, tmp.angle() + 90.0F, 0, 0, 256, 256, false, false);

        }

        float hoverTimer = (float)ReflectionHacks.getPrivate(this, AbstractPlayer.class, "hoverTimer");

        if (this.isHoveringDropZone && this.hoveredCard != null && Settings.isInfo) {
            if (this.hoveredCard.target == AbstractCard.CardTarget.ENEMY && hoveredMonster != null) {
                hoverTimer = MathHelper.fadeLerpSnap(hoverTimer, 1.0F);

                this.hoveredCard.calculateDamageDisplay(hoveredMonster);
                FontHelper.renderFontCentered(sb, FontHelper.bannerFont, Integer.toString(this.hoveredCard.damage), hoveredMonster.hb.cX, hoveredMonster.hb.cY + hoveredMonster.hb.height / 2.0F + 80.0F * Settings.scale + hoverTimer * 30.0F * Settings.scale, new Color(1.0F, 0.9F, 0.4F, hoverTimer));
            } else if (this.hoveredCard.target == AbstractCard.CardTarget.ALL_ENEMY) {
                hoverTimer = MathHelper.fadeLerpSnap(hoverTimer, 1.0F);

                this.hoveredCard.calculateDamageDisplay(null);
                float tmpY = 80.0F * Settings.scale + hoverTimer * 30.0F * Settings.scale;
                int i = 0;

                for(Iterator var4 = AbstractDungeon.getMonsters().monsters.iterator(); var4.hasNext(); ++i) {
                    AbstractMonster m = (AbstractMonster)var4.next();
                    FontHelper.renderFontCentered(sb, FontHelper.bannerFont, Integer.toString(this.hoveredCard.multiDamage[i]), m.hb.cX, m.hb.cY + m.hb.height / 2.0F + tmpY, new Color(1.0F, 0.9F, 0.4F, hoverTimer));
                }
            } else {
                hoverTimer = MathHelper.fadeLerpSnap(hoverTimer, 0.0F);
            }
        } else {
            hoverTimer = MathHelper.fadeLerpSnap(hoverTimer, 0.0F);
        }

        ReflectionHacks.setPrivate(this, AbstractPlayer.class, "hoverTimer", hoverTimer);
    }

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> startDeck = new ArrayList<>();

        otherPlayerMasterDeck.clear();

        if (isMokou)
        {
            startDeck.add(Strike_Red.ID);
            startDeck.add(Strike_Red.ID);
            startDeck.add(Strike_Red.ID);
            startDeck.add(Defend_Red.ID);
            startDeck.add(Defend_Red.ID);
            startDeck.add(Defend_Red.ID);

            otherPlayerMasterDeck.group.add(new Strike_Blue());
            otherPlayerMasterDeck.group.add(new Strike_Blue());
            otherPlayerMasterDeck.group.add(new Strike_Blue());
            otherPlayerMasterDeck.group.add(new Defend_Blue());
            otherPlayerMasterDeck.group.add(new Defend_Blue());
            otherPlayerMasterDeck.group.add(new Defend_Blue());
        }
        else
        {
            startDeck.add(Strike_Blue.ID);
            startDeck.add(Strike_Blue.ID);
            startDeck.add(Strike_Blue.ID);
            startDeck.add(Defend_Blue.ID);
            startDeck.add(Defend_Blue.ID);
            startDeck.add(Defend_Blue.ID);

            otherPlayerMasterDeck.group.add(new Strike_Red());
            otherPlayerMasterDeck.group.add(new Strike_Red());
            otherPlayerMasterDeck.group.add(new Strike_Red());
            otherPlayerMasterDeck.group.add(new Defend_Red());
            otherPlayerMasterDeck.group.add(new Defend_Red());
            otherPlayerMasterDeck.group.add(new Defend_Red());
        }

        return startDeck;
    }

    // Starting Relics
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> startingRelics = new ArrayList<>();

        startingRelics.add(BurningBlood.ID);

        return startingRelics;
    }

    // Character select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.play("CARD_BURN", 0.1f);
            CardCrawlGame.sound.playA("ATTACK_FLAME_BARRIER", -0.3f);
        }
        else {
            CardCrawlGame.sound.play("ATTACK_MAGIC_FAST_1", 0.1f);
        }
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // Character select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return MathUtils.randomBoolean() ? "ATTACK_MAGIC_FAST_1" : "ATTACK_FLAME_BARRIER";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 11;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return this.cardColor;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return this.energyFont;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return null;//return new NorthStar();
    }
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }
    @Override
    public String getTitle(PlayerClass playerClass) {
        return (isMokou ? characterStrings.NAMES[1] : characterStrings.NAMES[2]);
    }


    @Override
    public AbstractPlayer newInstance() {
        return new MokouKeine(isMokou);
    }

    @Override
    public Color getCardTrailColor() {
        return cardTrailColor;
    }
    @Override
    public Color getCardRenderColor() {
        return cardRenderColor;
    }
    @Override
    public Color getSlashAttackColor() {
        return slashAttackColor;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.POISON
        };
    }

    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }
    @Override
    public String getVampireText() {
        return characterStrings.TEXT[2];
    }





    private void drawCurvedLine(SpriteBatch sb, Vector2 controlPoint, Vector2 start, Vector2 end, Vector2 control) {
        float radius = 7.0F * Settings.scale;

        for(int i = 0; i < this.points.length - 1; ++i) {
            this.points[i] = Bezier.quadratic(this.points[i], (float)i / 20.0F, start, control, end, new Vector2());
            radius += 0.4F * Settings.scale;
            Vector2 tmp;
            float angle;
            if (i != 0) {
                tmp = new Vector2(this.points[i - 1].x - this.points[i].x, this.points[i - 1].y - this.points[i].y);
                angle = tmp.nor().angle() + 90.0F;
            } else {
                tmp = new Vector2(controlPoint.x - this.points[i].x, controlPoint.y - this.points[i].y);
                angle = tmp.nor().angle() + 270.0F;
            }

            sb.draw(ImageMaster.TARGET_UI_CIRCLE, this.points[i].x - 64.0F, this.points[i].y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, angle, 0, 0, 128, 128, false, false);
        }
    }
}
