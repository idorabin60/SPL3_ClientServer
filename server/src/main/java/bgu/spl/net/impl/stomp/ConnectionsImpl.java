package bgu.spl.net.impl.stomp;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.print.DocFlavor.STRING;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class ConnectionsImpl<T> implements Connections<T> {

    private final ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionHandlers; // Maps connectionId to handlers
    //אני צריכה למחוק את הטבלה הזאת 
    private final ConcurrentHashMap<String, ConcurrentHashMap<Integer, ConnectionHandler<T>>> channels; // Maps channel names to subscribers    
    private final ConcurrentHashMap<String, Integer> MapClientsNameAndId;
    private final ConcurrentHashMap<String, ConcurrentHashMap<Integer,String>> topics; 
    private final ConcurrentHashMap<String, String> mapAllUsers;
    private AtomicInteger counterMassageId; 

    public ConnectionsImpl() {
        this.connectionHandlers = new ConcurrentHashMap<>();
        this.channels = new ConcurrentHashMap<>();
        this.MapClientsNameAndId=new ConcurrentHashMap<>();
        this.topics=new ConcurrentHashMap<>();
        this.mapAllUsers = new ConcurrentHashMap<>();
        this.counterMassageId = new AtomicInteger(0); 
    }

    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler<T> handler = connectionHandlers.get(connectionId);
        if (handler != null) {
            handler.send(msg); // Use the send method of the ConnectionHandler
            return true;
        }
        return false; // Return false if the connectionId is not found
    }

    @Override
    public void send(String channel, T msg) {
        // Broadcast the message to all subscribers of the channel
        ConcurrentHashMap<Integer, ConnectionHandler<T>> subscribers = channels.get(channel);
        if (subscribers != null) {
            for (ConnectionHandler<T> handler : subscribers.values()) {
                handler.send(msg);
            }
        }
    }

    @Override
    public void disconnect(int connectionId) {
        // אני צריכה למחוק אותו מכל הטבלאות 
        ConnectionHandler<T> handler = connectionHandlers.remove(connectionId);
        for (Map.Entry<String, Integer> entry : MapClientsNameAndId.entrySet()) {
            if (entry.getValue().equals(connectionId)) {
                MapClientsNameAndId.remove(entry.getKey()); 
            }
        }

        if (handler != null) {
            System.out.println("i reach her handler!=null");
            try {
                handler.close(); // Close the connection handler
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // // Remove the connectionId from all subscribed channels
        // for (Map<Integer, ConnectionHandler<T>> subscribers : channels.values()) {
        //     subscribers.remove(connectionId);
        // }
    }

    /**
     * Registers a new connection handler for a connection ID.
     *
     * @param connectionId the connection ID
     * @param handler the connection handler
     */
    public void addConnection(int connectionId, ConnectionHandler<T> handler) {
        connectionHandlers.put(connectionId, handler);
    }

    /**
     * Subscribes a connection to a channel.
     *
     * @param connectionId the connection ID
     * @param channel the channel name
     */
    public void subscribe(int connectionId, String channel) {
        channels.computeIfAbsent(channel, k -> new ConcurrentHashMap<>())
                .put(connectionId, connectionHandlers.get(connectionId));
    }

    /**
     * Unsubscribes a connection from a channel.
     *
     * @param connectionId the connection ID
     * @param channel the channel name
     */
    public void unsubscribe(int connectionId, String channel) {
        Map<Integer, ConnectionHandler<T>> subscribers = channels.get(channel);
        if (subscribers != null) {
            subscribers.remove(connectionId);
        }
    }

    public boolean isClientSubscribeToTheChannel (String channelName, int connectionId){
       return (topics.get(channelName).get(connectionId)!=null);
    }
   
    public void addSubscription (String channelName, String subscriptionId, int connectionId){
        // if (topics.get(channelName)==null){
        //     topics.put(channelName, new ConcurrentHashMap<>());
        //     topics.get(channelName).put(connectionId, subscriptionId);
        // }
        // else {
        //     topics.get(channelName).put(connectionId, subscriptionId);
        // }
    }

    public boolean isUserExists(String userName) {
        return mapAllUsers.containsKey(userName);
    }
    public boolean isClientExists(String userName){
        return MapClientsNameAndId.containsKey(userName); 
    }
    public String getPassward (String userName){
        return mapAllUsers.get(userName);
    }

    public void addClientToMapClientsNameAndId(String userName, int connectionId){
        this.MapClientsNameAndId.put(userName,connectionId);
    }
    public void addClientToMapAllUsers(String userName, String passward){
        this.mapAllUsers.put(userName, passward);
    }


}
