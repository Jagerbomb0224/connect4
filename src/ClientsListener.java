import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientsListener implements Runnable
{
    //X is now Red
    //Y is now Blue
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private TTTFrame frame = null;

    public ClientsListener(ObjectInputStream is,
                           ObjectOutputStream os,
                           TTTFrame frame) {
        this.is = is;
        this.os = os;
        this.frame = frame;

    }

    @Override
    public void run() {
        try
        {
            while(true)
            {
                CommandFromServer cfs = (CommandFromServer)is.readObject();

                // processes the received command
                if(cfs.getCommand() == CommandFromServer.RED_TURN)
                    frame.setTurn('R');
                else if(cfs.getCommand() == CommandFromServer.BLUE_TURN)
                    frame.setTurn('B');
                else if(cfs.getCommand() == cfs.MOVE)
                {
                    String data = cfs.getData();
                    // pulls data for the move from the data field
                    int c = data.charAt(0) - '0';
                    int r = data.charAt(1) - '0';

                    // changes the board and redraw the screen
                    frame.makeMove(c,r,data.charAt(2));
                }
                // handles the various end game states
                else if(cfs.getCommand() == CommandFromServer.TIE)
                {
                    frame.setText("Tie game.");
                }
                else if(cfs.getCommand() == CommandFromServer.RED_WINS)
                {
                    frame.setText("RED wins!");
                }
                else if(cfs.getCommand() == CommandFromServer.BLUE_WINS)
                {
                    frame.setText("BLUE wins!");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}