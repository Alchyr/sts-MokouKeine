package もこけね.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AltOverlayMenu extends OverlayMenu {
    public AbstractPlayer p;

    public OtherDrawPilePanel otherPlayerDrawPile = new OtherDrawPilePanel();
    public OtherDiscardPilePanel otherDiscardPilePanel = new OtherDiscardPilePanel();

    public boolean wasOpen;

    public AltOverlayMenu(AbstractPlayer p)
    {
        super(p);
        this.p = p;
        wasOpen = false;
    }

    @Override
    public void update() {
        wasOpen = AbstractDungeon.isScreenUp;
        super.update();
    }

    @Override
    public void showCombatPanels() {
        otherPlayerDrawPile.show();
        otherDiscardPilePanel.show();
        super.showCombatPanels();
    }

    @Override
    public void hideCombatPanels() {
        otherPlayerDrawPile.hide();
        otherDiscardPilePanel.hide();
        super.hideCombatPanels();
    }

    @Override
    public void render(SpriteBatch sb) {
        this.endTurnButton.render(sb);
        this.proceedButton.render(sb);
        this.cancelButton.render(sb);
        if (!Settings.hideLowerElements) {
            this.energyPanel.render(sb);
            this.combatDeckPanel.render(sb);
            this.otherPlayerDrawPile.render(sb);
            this.discardPilePanel.render(sb);
            this.otherDiscardPilePanel.render(sb);
            this.exhaustPanel.render(sb);
        }

        this.p.renderHand(sb);
        this.p.hand.renderTip(sb);
    }
}
