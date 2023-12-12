package Milestone3.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Milestone3.common.Payload;
import Milestone3.common.PayloadType;
import Milestone3.common.RoomResultPayload;
import Milestone3.common.Constants;

//Enum Singleton: https://www.geeksforgeeks.org/advantages-and-disadvantages-of-using-enum-as-singleton-in-java/
public enum Client {
    INSTANCE;

    Socket server = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    final String ipAddressPattern = "/connect\\s+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{3,5})";
    final String localhostPattern = "/connect\\s+(localhost:\\d{3,5})";
    boolean isRunning = false;
    private Thread inputThread;
    private Thread fromServerThread;
    private String clientName = "";
    private long myClientId = Constants.DEFAULT_CLIENT_ID;
    private static Logger logger = Logger.getLogger(Client.class.getName());
    private static IClientEvents events;
    private Hashtable<Long, String> userList = new Hashtable<Long, String>();

    public boolean isConnected() {
        if (server == null) {
            return false;
        }
        // https://stackoverflow.com/a/10241044
        // Note: these check the client's end of the socket connect; therefore they
        // don't really help determine
        // if the server had a problem
        return server.isConnected() && !server.isClosed() && !server.isInputShutdown() && !server.isOutputShutdown();

    }

    /**
     * Takes an ip address and a port to attempt a socket connection to a server.
     * 
     * @param address
     * @param port
     * @return true if connection was successful
     */
    public boolean connect(String address, int port, String username, IClientEvents callback) {
        // TODO validate
        this.clientName = username;
        Client.events = callback;
        try {
            server = new Socket(address, port);
            // channel to send to server
            out = new ObjectOutputStream(server.getOutputStream());
            // channel to listen to server
            in = new ObjectInputStream(server.getInputStream());
            logger.info("Client connected");
            listenForServerMessage();
            sendConnect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isConnected();
    }
    

    // Send methods TODO add other utility methods for sending here
    // NOTE: Can change this to protected or public if you plan to separate the
    // sendConnect action and the socket handshake
    private boolean isConnection(String text) {
        // https://www.w3schools.com/java/java_regex.asp
        return text.matches(ipAddressPattern)
                || text.matches(localhostPattern);
    }

    private void sendReadyPayload() throws IOException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.READY);
        p.setPlayerIds(Collections.singletonList(myClientId));
        out.writeObject(p);
    }

    private void sendPickPayload(String option) throws IOException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.PICK);
        p.setPlayerIds(Collections.singletonList(myClientId));
        // Set option chosen by the player
        p.setMessage(option);
        out.writeObject(p);
    }

    private void sendPassPayload() throws IOException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.PASS);
        p.setPlayerIds(Collections.singletonList(myClientId));
        out.writeObject(p);
    }

    private boolean isQuit(String text) {
        return text.equalsIgnoreCase("/quit");
    }

    /**
     * Controller for handling various text commands.
     * <p>
     * Add more here as needed
     * </p>
     * 
     * @param text
     * @return true if a text was a command or triggered a command
     * @throws IOException
     */
    
     public void sendReadyCommand() {
        Payload payload = new Payload();
        payload.setPayloadType(PayloadType.READY);
    }
    private boolean isPickCommand(String text) {
        return text.startsWith("/pick");
    }
    
    private boolean isPassCommand(String text) {
        return text.equalsIgnoreCase("/pass");
    }
    
    private String extractOptionFromCommand(String text) {
        // Assuming the user enters "/pick option", this extracts the option
        return text.trim().substring(6).trim();
    }

    // end send methods
    public void sendCreateRoom(String room) throws IOException, NullPointerException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.CREATE_ROOM);
        p.setMessage(room);
        send(p);
    }

    public void sendJoinRoom(String room) throws IOException, NullPointerException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.JOIN_ROOM);
        p.setMessage(room);
        send(p);
    }
    protected void sendReadyStatus() throws IOException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.READY);
        out.writeObject(p);
    }

    public void sendListRooms(String query) throws IOException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.GET_ROOMS);
        p.setMessage(query);
        out.writeObject(p);
    }

    public void sendGetRooms(String query) throws IOException, NullPointerException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.GET_ROOMS);
        p.setMessage(query);
        send(p);
    }

    private void sendConnect() throws IOException, NullPointerException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.CONNECT);
        p.setClientName(clientName);
        send(p);
    }
    public void sendDisconnect() throws IOException, NullPointerException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.DISCONNECT);
        send(p);
    }

    public void sendMessage(String message) throws IOException, NullPointerException {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.MESSAGE);
        p.setMessage(message);
        p.setClientName(clientName);
        send(p);
    }

    // keep this private as utility methods should be the only Payload creators
    private void send(Payload p) throws IOException, NullPointerException {
        logger.log(Level.FINE, "Sending Payload: " + p);
        out.writeObject(p);// TODO force throw each
        logger.log(Level.INFO, "Sent Payload: " + p);
    }

    // end send methods

    private void listenForServerMessage() {
        fromServerThread = new Thread() {
            @Override
            public void run() {
                try {
                    Payload fromServer;
                    logger.log(Level.INFO, "Listening for server messages");
                    // while we're connected, listen for strings from server
                    while (!server.isClosed() && !server.isInputShutdown()
                            && (fromServer = (Payload) in.readObject()) != null) {

                        System.out.println("Debug Info: " + fromServer);
                        processPayload(fromServer);

                    }
                    System.out.println("Loop exited");
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!server.isClosed()) {
                        System.out.println("Server closed connection");
                    } else {
                        System.out.println("Connection closed");
                    }
                } finally {
                    close();
                    System.out.println("Stopped listening to server input");
                }
            }
        };
        fromServerThread.start();// start the thread
    }

    private void processPayload(Payload p) {
        logger.log(Level.FINE, "Received Payload: " + p);
        if (events == null) {
            logger.log(Level.FINER, "Events not initialize/set" + p);
            return;
        }
        switch (p.getPayloadType()) {
            case CONNECT:
                events.onClientConnect(p.getClientId(), p.getClientName(), p.getMessage());
                break;
            case DISCONNECT:
                events.onClientDisconnect(p.getClientId(), p.getClientName(), p.getMessage());
                break;
            case MESSAGE:
                events.onMessageReceive(p.getClientId(), p.getMessage());
                break;
            case CLIENT_ID:
                events.onReceiveClientId(p.getClientId());
                break;
            case RESET_USER_LIST:
                events.onResetUserList();
                break;
            case SYNC_CLIENT:
                events.onSyncClient(p.getClientId(), p.getClientName());
                break;
            case GET_ROOMS:
                RoomResultPayload rp = (RoomResultPayload) p;
                System.out.println("Received Room List:");
                if (rp.getMessage() != null) {
                    System.out.println(rp.getMessage());
                } else {
                    for (int i = 0, l = rp.getRooms().length; i < l; i++) {
                        System.out.println(String.format("%s) %s", (i + 1), rp.getRooms()[i]));
                    }
                }
                events.onReceiveRoomList(rp.getRooms(), rp.getMessage());
                break;
            case JOIN_ROOM:
                events.onRoomJoin(p.getMessage());
                break;
            case QUESTION:
                System.out.println(String.format("Category: %s", p.getCategory()));
                System.out.println(String.format("Question: %s", p.getQuestion()));
                for (int i = 0; i < p.getOptions().size(); i++) {
                    System.out.println((i + 1) + ". " + p.getOptions().get(i));
                }
                break;
            default:
                logger.log(Level.WARNING, "Unhandled payload type");
                break;

        }
    }

    private void close() {
        try {
            inputThread.interrupt();
        } catch (Exception e) {
            System.out.println("Error interrupting input");
            e.printStackTrace();
        }
        try {
            fromServerThread.interrupt();
        } catch (Exception e) {
            System.out.println("Error interrupting listener");
            e.printStackTrace();
        }
        try {
            System.out.println("Closing output stream");
            out.close();
        } catch (NullPointerException ne) {
            System.out.println("Server was never opened so this exception is ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Closing input stream");
            in.close();
        } catch (NullPointerException ne) {
            System.out.println("Server was never opened so this exception is ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Closing connection");
            server.close();
            System.out.println("Closed socket");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            System.out.println("Server was never opened so this exception is ok");
        }
    }
}