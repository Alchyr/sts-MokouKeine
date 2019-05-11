package もこけね.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import static もこけね.もこけねは神の国.chat;

public class WaitForSignalAction extends AbstractGameAction {
    public static int signal = 0;

    private String msg;
    private boolean sent;

    public WaitForSignalAction(String waitMessage)
    {
        this.actionType = ActionType.WAIT;
        this.msg = waitMessage;
        this.sent = false;
    }

    @Override
    public void update() {
        if (!sent) {
            sent = true;
            if (!msg.isEmpty()) {
                chat.receiveMessage(msg);
            }
        }
        if (signal > 0) {
            --signal;
            this.isDone = true;
        }
    }
}
