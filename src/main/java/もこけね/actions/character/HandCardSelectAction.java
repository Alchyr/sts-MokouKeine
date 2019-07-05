package もこけね.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

//Action for reordering hand when other player opens a HandCardSelectScreen
public class HandCardSelectAction extends AbstractGameAction {
    private static ArrayList<Integer> finalPositions = new ArrayList<>();
    private static ArrayList<String[]> queuedPositions = new ArrayList<>();

    public static void setFinalPositions(String[] data)
    {
        if (!finalPositions.isEmpty())
        {
            queuedPositions.add(data);
        }
        else
        {
            for (String index : data)
            {
                finalPositions.add(Integer.parseInt(index));
            }
        }
    }

    public HandCardSelectAction()
    {
        this.duration = 1.0f;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (!finalPositions.isEmpty())
        {


            finalPositions.clear();
            if (!queuedPositions.isEmpty())
            {
                setFinalPositions(queuedPositions.remove(0));
            }
            this.isDone = true;
        }
    }
}
