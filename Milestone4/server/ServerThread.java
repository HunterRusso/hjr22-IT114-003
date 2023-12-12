package Milestone4.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Milestone4.common.Constants;
import Milestone4.common.Payload;
import Milestone4.common.Phase;
import Milestone4.common.PayloadType;
import Milestone4.common.RoomResultPayload;

/**
 * A server-side representation of a single client
 */
public class ServerThread extends Thread {
    private Socket client;
    private String clientName;
    private boolean isRunning = false;
    private ObjectOutputStream out;// exposed here for send()
    // private Server server;// ref to our server so we can call methods on it
    // more easily
    private Room currentRoom;
    private String correctOption;
    private static Logger logger = Logger.getLogger(ServerThread.class.getName());
    private long myId;
    
    

    public void setClientId(long id) {
        myId = id;
    }

    public long getClientId() {
        return myId;
    }
    public boolean isRunning() {
        return isRunning;
    }

    private void info(String message) {
        System.out.println(String.format("Thread[%s]: %s", getId(), message));
    }

    public ServerThread(Socket myClient, Room room) {
        info("Thread created");
        // get communication channels to single client
        this.client = myClient;
        this.currentRoom = room;

    }

    protected void setClientName(String name) {
        if (name == null || name.isBlank()) {
            System.err.println("Invalid client name being set");
            return;
        }
        clientName = name;
    }

    protected String getClientName() {
        return clientName;
    }

    private synchronized void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public synchronized String getCorrectOption() {
        return correctOption;
    }

    protected synchronized Room getCurrentRoom() {
        return currentRoom;
    }

    protected synchronized void setCurrentRoom(Room room) {
        if (room != null) {
            currentRoom = room;
        } else {
            info("Passed in room was null, this shouldn't happen");
        }
    }

    public void disconnect() {
        sendConnectionStatus(myId, getClientName(), false);
        info("Thread being disconnected by server");
        isRunning = false;
        cleanup();
    }

    // send methods
    public boolean sendRoomName(String name) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.JOIN_ROOM);
        p.setMessage(name);
        return send(p);
    }

    public boolean sendRoomsList(String[] rooms, String message) {
        RoomResultPayload payload = new RoomResultPayload();
        payload.setRooms(rooms);
        //Fixed in Module7.Part9
        if(message != null){
            payload.setMessage(message);
        }
        return send(payload);
    }
    public boolean sendExistingClient(long clientId, String clientName) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.SYNC_CLIENT);
        p.setClientId(clientId);
        p.setClientName(clientName);
        return send(p);
    }

    public boolean sendResetUserList() {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.RESET_USER_LIST);
        return send(p);
    }

    public boolean sendClientId(long id) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.CLIENT_ID);
        p.setClientId(id);
        return send(p);
    }
    public boolean sendPhaseSync(Phase phase) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.PHASE);
        p.setMessage(phase.name());
        return send(p);
    }

    public boolean sendReadyStatus(long clientId) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.READY);
        p.setClientId(clientId);
        return send(p);
    }

    public boolean sendMessage(long clientId, String message) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.MESSAGE);
        p.setClientId(clientId);
        p.setMessage(message);
        return send(p);
    }
    public boolean sendConnectionStatus(long clientId, String who, boolean isConnected) {
        Payload p = new Payload();
        p.setPayloadType(isConnected ? PayloadType.CONNECT : PayloadType.DISCONNECT);
        p.setClientId(clientId);
        p.setClientName(who);
        p.setMessage(isConnected ? "connected" : "disconnected");
        return send(p);
    }

    private boolean send(Payload payload) {
        // added a boolean so we can see if the send was successful
        try {
            // TODO add logger
            logger.log(Level.FINE, "Outgoing payload: " + payload);
            out.writeObject(payload);
            logger.log(Level.INFO, "Sent payload: " + payload);
            return true;
        } catch (IOException e) {
            info("Error sending message to client (most likely disconnected)");
            // comment this out to inspect the stack trace
            // e.printStackTrace();
            cleanup();
            return false;
        } catch (NullPointerException ne) {
            info("Message was attempted to be sent before outbound stream was opened: " + payload);
            return true;// true since it's likely pending being opened
        }
    }

    private void sendPlayerScore() {
        Payload scorePayload = new Payload();
        scorePayload.setPayloadType(PayloadType.SCORE);
        scorePayload.setScore(getScore());
        send(scorePayload);
    }
    // end send methods
    @Override
    public void run() {
        info("Thread starting");
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());) {
            this.out = out;
            isRunning = true;
            Payload fromClient;
            while (isRunning && // flag to let us easily control the loop
                    (fromClient = (Payload) in.readObject()) != null // reads an object from inputStream (null would
                                                                     // likely mean a disconnect)
            ) {

                info("Received from client: " + fromClient);
                processMessage(fromClient);

            } // close while loop
        } catch (Exception e) {
            // happens when client disconnects
            e.printStackTrace();
            info("Client disconnected");
        } finally {
            isRunning = false;
            info("Exited thread loop. Cleaning up connection");
            cleanup();
        }
    }
    private int score = 0;
    private boolean correct = false;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    void processMessage(Payload p) {
        switch (p.getPayloadType()) {
            case CONNECT:
                setClientName(p.getClientName());
                break;
            case DISCONNECT:
                Room.disconnectClient(this, getCurrentRoom());
                break;
            case MESSAGE:
                if (currentRoom != null) {
                    currentRoom.sendMessage(this, p.getMessage());
                } else {
                    logger.log(Level.INFO, "Migrating to lobby on message with null room");
                    Room.joinRoom("lobby", this);
                }
                break;
            case READY:
                if (currentRoom != null) {
                    currentRoom.startRound();
                } else {
                    System.out.println("Cannot start round. No current room.");
                }
                break;
                case PICK: //hjr22, 12/11/23
                    if (currentRoom != null) { //this shows the case for the /pick command which allows players to pick an option
                        String option = p.getMessage(); //this if checks in the current room is not null
                        String correctOption = currentRoom.getCorrectOption();

                        if (correctOption != null && option.equals(correctOption)) { //this if checks if the player selected the correct option
                            sendMessage("Server", "Congratulations! You chose the correct option.");
                            // Increase the score for this player
                            int currentScore = getScore(); //this calls get score
                            setScore(currentScore + 1); //this increments the score
                            System.out.println(getClientName() + " chose the correct option and earned 1 point."); //this is what the server sees
                        } else {
                            sendMessage("Server", "Oops! You chose the wrong option. The correct option was: " + correctOption);
                            //if the player didn't choose the correct option, this is displayed to the player
                            System.out.println(getClientName() + " chose the wrong option."); //this is displayed to the server
                        }

                        System.out.println(getClientName() + " picked option: " + option);//this shown to the server if they just pick an option
                    } else {
                        System.out.println("Cannot pick option. No current room."); //this is if there is no room
                    }
                    break;
            case AWAY:
                if (currentRoom != null) {
                    // Logic to handle the pass payload
                    System.out.println(getClientName() + " turn skiped.");
                } else {
                    System.out.println("cannot skip.");
                }
                break;
            case SCORE:
                sendPlayerScore();
                break;
            case QUESTION:
                String category = p.getCategory();
                String question = p.getQuestion();
                List<String> options = p.getOptions();
    
                // Display the question information on the client side
                System.out.println("Category: " + category);
                System.out.println("Question: " + question);
                System.out.println("Options: " + options);
    
                // Set the correct option for the current question
                setCorrectOption(correctOption);
                break;
            default:
                break;
        }
    }

    private void cleanup() {
        info("Thread cleanup() start");
        try {
            client.close();
        } catch (IOException e) {
            info("Client already closed");
        }
        info("Thread cleanup() complete");
    }
}