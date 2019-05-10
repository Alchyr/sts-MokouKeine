package もこけね.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import もこけね.patch.energy_division.TrackCardSource;

public class DontUseSpecificEnergyAction extends AbstractGameAction {
    public DontUseSpecificEnergyAction()
    {
        this.actionType = ActionType.DAMAGE; //damage actions aren't cleared post-combat.
    }

    @Override
    public void update() {
        TrackCardSource.useOtherEnergy = false;
        TrackCardSource.useMyEnergy = false;
        this.isDone = true;
    }
}