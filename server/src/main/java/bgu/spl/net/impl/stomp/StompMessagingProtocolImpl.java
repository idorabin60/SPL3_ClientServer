package bgu.spl.net.impl.stomp;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolImpl implements StompMessagingProtocol<Frame> {

    private int connectionId;
    private ConnectionsImpl<Frame> connections;
    private boolean shouldTerminate = false;

    @Override
    public void start(int connectionId, Connections<Frame> connections) {
        this.connectionId = connectionId;
       // Cast to ConnectionsImpl<Frame>
     if (connections instanceof ConnectionsImpl) {
        this.connections = (ConnectionsImpl<Frame>) connections;
     } else {
        throw new IllegalArgumentException("Connections is not an instance of ConnectionsImpl");
     }
    }

    @Override
    public void process(Frame frame) {
        if (frame.getCommand().equalsIgnoreCase("CONNECT")) {
            handleConnectFrame(frame);
           }
           else if (frame.getCommand().equalsIgnoreCase("DISCONNECT")) {
            System.out.println("I RECIVE THE FRAME");
            handleDisconnectFrame(frame);
           }
           else if (frame.getCommand().equalsIgnoreCase("SUBSCRIBE")) {
            handleSubscribeFrame(frame);
           }
         else {
            System.out.println("Unknown command: " + frame.getCommand());
        }
    }

    private void handleSubscribeFrame(Frame frame){
        String channelName = frame.getHeader("destination");
        String subscribtionId = frame.getHeader("id");
        sendReceiptFrameIfNeeded(frame);
        // if (connections.isClientSubscribeToTheChannel(channelName, connectionId)){
        //     sendReceiptFrameIfNeeded(frame);
        // }
        // else {
        //     connections.addSubscription(channelName, subscribtionId, connectionId);
        //     sendReceiptFrameIfNeeded(frame);
        // }
    }

    private void handleDisconnectFrame (Frame frame){
        sendReceiptFrameIfNeeded(frame);
        gracefulShoutDown();
    }
    
    private void handleConnectFrame (Frame frame){
        String userName = frame.getHeader("login"); 
        String passward = frame.getHeader("passcode");
        boolean existsUser = connections.isUserExists(userName);
        boolean loginTestsSucced = true; 
        if (existsUser) {
            if (connections.isClientExists(userName)){
                loginTestsSucced=false; 
                sendErrorAndClose(frame, "User is already logged in.");
            }
            else { 
                if (passward!=connections.getPassward(userName)){
                    loginTestsSucced=false; 
                   sendErrorAndClose(frame, "Wrong password.");
                }
                else {
                    connections.addClientToMapClientsNameAndId(userName, connectionId);
                }
            }
        }
        else {
            connections.addClientToMapAllUsers(userName, passward);
            connections.addClientToMapClientsNameAndId(userName, connectionId);
        }
        if (loginTestsSucced){
        Frame connectedFrame = new Frame("CONNECTED", null,null);
                    connectedFrame.addHeader("version", "1.2");
                    System.out.println(connectedFrame.toString());
                    if (!connections.send(connectionId, connectedFrame)){
                        System.out.println("ERROR: network disconnection");
                       }
                    }

    }
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private void sendErrorAndClose (Frame frame, String errorDescription){
        Frame errorFrame = new Frame("ERROR", null,null);
        errorFrame.addHeader("Error Description:", errorDescription);
        errorFrame.setBody("----------\n" + frame.toString() + "----------\n");
        if (!connections.send(connectionId, errorFrame)){
            System.out.println("ERROR????????????");
           }
        gracefulShoutDown();
    }

    private void gracefulShoutDown(){
        shouldTerminate=true;
        connections.disconnect(connectionId);
    }

    private void sendReceiptFrameIfNeeded(Frame frame){
        String receiptId= frame.getHeader("receipt");
        if (receiptId!=null){
            Frame receiptFrame = new Frame("RECEIPT", null, null);
            receiptFrame.addHeader("receipt-id ", receiptId);
            if (!connections.send(connectionId, receiptFrame)){
                System.out.println("ERROR: network disconnection");
               }
            else{
                System.out.println(receiptFrame.toString());
            }
        }
    }
}
