package bgu.spl.net.impl.stomp;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class ConnectionsImpl<T> implements Connections<T> {

    private final Map<Integer, ConnectionHandler<T>> connectionHandlers; // Maps connectionId to handlers
    private final Map<String, ConcurrentHashMap<Integer, ConnectionHandler<T>>> channels; // Maps channel names to subscribers

    public ConnectionsImpl() {
        this.connectionHandlers = new ConcurrentHashMap<>();
        this.channels = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler<T> handler = connectionHandlers.get(connectionId);
        System.out.println("The connection ID: " + connectionId + (handler == null));
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
        ConnectionHandler<T> handler = connectionHandlers.remove(connectionId);
        if (handler != null) {
            try {
                handler.close(); // Close the connection handler
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Remove the connectionId from all subscribed channels
        for (Map<Integer, ConnectionHandler<T>> subscribers : channels.values()) {
            subscribers.remove(connectionId);
        }
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
    


}
