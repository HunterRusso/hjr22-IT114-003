package Milestone2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private synchronized void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public synchronized String getCorrectOption() {
        return correctOption;
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
        info("Thread being disconnected by server");
        isRunning = false;
        cleanup();
    }

    // send methods
    
    public boolean sendMessage(String from, String message) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.MESSAGE);
        p.setClientName(from);
        p.setMessage(message);
        return send(p);
    }
    public boolean sendConnectionStatus(String who, boolean isConnected){
        Payload p = new Payload();
        p.setPayloadType(PayloadType.MESSAGE);
        p.setClientName(who);
        p.setMessage(isConnected?"connected":"disconnected");
        return send(p);
    }

    boolean send(Payload payload) {
        // added a boolean so we can see if the send was successful
        try {
            out.writeObject(payload);
            return true;
        } catch (IOException e) {
            info("Error sending message to client (most likely disconnected)");
            cleanup();
            return false;
        } catch (NullPointerException ne) {
            info("Message was attempted to be sent before outbound stream was opened");
            return true; // true since it's likely pending being opened
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
                // TBD
                break;
            case MESSAGE:
                if (currentRoom != null) {
                    currentRoom.sendMessage(this, p.getMessage());
                } else {
                    // TODO migrate to lobby
                    Room.joinRoom("lobby", this);
                }
                break;
            case READY:
                if (currentRoom != null) {
                    currentRoom.startRound();
                } else {
                    System.out.println("cannot start round.");
                }
                break;
                case PICK: //hjr22, 11/13/23
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
            case PASS:
                if (currentRoom != null) {
                    // Logic to handle the pass payload
                    System.out.println(getClientName() + " passed.");
                } else {
                    System.out.println("cannot pass.");
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