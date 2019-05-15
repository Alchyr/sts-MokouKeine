package もこけね.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import もこけね.patch.energy_division.SetEnergyGain;
import もこけね.patch.energy_division.TrackCardSource;

import java.util.Set;

public class DontUseSpecificEnergyAction extends AbstractGameAction {
    public DontUseSpecificEnergyAction()
    {
        this.actionType = ActionType.DAMAGE; //damage actions aren't cleared post-combat.
    }

    @Override
    public void update() {
        TrackCardSource.useOtherEnergy = false;
        TrackCardSource.useMyEnergy = false;
        SetEnergyGain.otherPlayerGain = false;
        SetEnergyGain.myGain = false;
        this.isDone = true;
    }
}