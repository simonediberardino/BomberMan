package game.events;

import game.Bomberman;
import game.data.DataInputOutput;

public class RoundArenaPassedGameEvent extends RoundPassedGameEvent{
    @Override
    public void invoke(Object arg) {
        int nRound = (int) arg;

        if(nRound > 1)
            super.invoke(arg);

        System.out.println("NEXT ROUND ARENA");
        //Bomberman.getMatch().getInventoryElementControllerPoints().setNumItems((int) DataInputOutput.getScore());
    }
}
