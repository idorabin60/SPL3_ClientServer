package bgu.spl.net.impl.stomp;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolImpl implements StompMessagingProtocol<Frame> {

    private int connectionId;
    private Connections<Frame> connections;
    private boolean shouldTerminate = false;

    @Override
    public void start(int connectionId, Connections<Frame> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Frame frame) {
        if (frame.getCommand().equalsIgnoreCase("CONNECT")) {
            // Create a CONNECTED frame
            Frame connectedFrame = new Frame("CONNECTED", null,null);
            connectedFrame.addHeader("version", "1.2");
            System.out.println("you create a CONNECTED frame~~ (: (; ))");
            System.out.println(connectedFrame.toString());
            // Use the connections object to send the response
           if (!connections.send(connectionId, connectedFrame)){
            System.out.println("ERROR????????????");
           }
        } else {
            System.out.println("Unknown command: " + frame.getCommand());
        }
    }
    
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
