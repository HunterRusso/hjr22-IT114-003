package Milestone3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import Milestone3.common.Constants;

public enum Server {
    INSTANCE;
    int port = 3001;
    // connected clients
    // private List<ServerThread> clients = new ArrayList<ServerThread>();
    //hjr22, 11/13/23
    private List<Room> rooms = new ArrayList<Room>();
    private Room lobby = null;// default room
    private long nextClientId = 1;
    private Queue<ServerThread> incomingClients = new LinkedList<ServerThread>();
    // https://www.geeksforgeeks.org/killing-threads-in-java/
    private volatile boolean isRunning = false;
    //these are all of the catagories in a list
    private final List<String> categories = Arrays.asList("Random", "Presidents", "TV Shows", "Anime", "Videogames");
    //these are a list of each catagories questions
    private final List<String> RandomQuestions = Arrays.asList("What State has less water?", "Which is not a Kardashian?", "What is the national animal of Scotland?");
    private final List<String> PresidentsQuestions = Arrays.asList("What President had 15 children?", "What President invented their own sport?", "What President weighs the most?");
    private final List<String> TVShowQuestions = Arrays.asList("In Regular Show: What does G.B.F stand for?", "In Adventure Time: Who did the Ice King take care of during the Apocalypse?", "In ICarly: What was Sam obsessed with?");
    private final List<String> AnimeQuestions = Arrays.asList("In One Piece: What is Usopps first bounty?", "In Berserk: Who survives The Eclipse?", "In Dragon Ball: What alter ego does Master Roshi go by during Goku's first tournament?");
    private final List<String> VideogamesQuestions = Arrays.asList("In Devil May Cry: Dante's brother, Vergil, wields what sword?", "In Mortal Kombat: What character is not one of the ninjas?", "In The Legend of Zelda: What game splits the series into 3 different timelines?");
    //this sets the round duration to 60 seconds
    private final int roundDurationInSeconds = 60; // Sets the round to 1 minute

    
    class QuestionOptions {
        private String question;
        public List<String> potentialAnswers;
        private String correctOption;
    
        public QuestionOptions(String question, List<String> potentialAnswers, String correctOption) {
            this.question = question;
            this.potentialAnswers = potentialAnswers;
            this.correctOption = correctOption;
        }
    
        public String getQuestion() {
            return question;
        }
    
        public List<String> getPotentialAnswers() {
            return potentialAnswers;
        }
    
        public String getCorrectOption() {
            return correctOption;
        }
    }
    private void start(int port) {
        this.port = port;
        // server listening
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            Socket incoming_client = null;
            System.out.println("Server is listening on port " + port);
            // Reference server statically
            Room.server = this;// all rooms will have the same reference
            isRunning = true;
            startQueueManager();
            // create a lobby on start
            lobby = new Room("Lobby");
            rooms.add(lobby);
            do {
                System.out.println("waiting for next client");
                if (incoming_client != null) {
                    System.out.println("Client connected");
                    ServerThread sClient = new ServerThread(incoming_client, lobby);
                    sClient.start();
                    incomingClients.add(sClient);
                    incoming_client = null;

                }
            } while ((incoming_client = serverSocket.accept()) != null);
        } catch (IOException e) {
            System.err.println("Error accepting connection");
            e.printStackTrace();
        } finally {
            System.out.println("closing server socket");
        }
    }
    void startQueueManager() {
        // Queue manager thread to wait for the ServerThread thread to start
        // before officially handing them off to a room and opening them for
        // communication
        new Thread() {
            @Override
            public void run() {
                // slight delay to let potentially new client to finish
                // binding input/output streams
                // comment out the Thread.sleep to see what happens
                while (isRunning) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (incomingClients.size() > 0) {
                        ServerThread ic = incomingClients.peek();
                        if (ic != null) {
                            //wait for the thread to start and for the client to send the client name (username)
                            if (ic.isRunning() && ic.getClientName() != null) {
                                handleIncomingClient(ic);
                                incomingClients.poll();
                            }
                        }
                    }
                }
            }
        }.start();
    }

    void handleIncomingClient(ServerThread client) {
        client.setClientId(nextClientId);// server reference
        client.sendClientId(nextClientId);// client reference
        nextClientId++;
        if (nextClientId < 0) {// will use overflow to reset our counter
            nextClientId = 1;
        }
        joinRoom("lobby", client);
    }

    /***
     * Helper function to check if room exists by case insensitive name
     * 
     * @param roomName The name of the room to look for
     * @return matched Room or null if not found
     */
    //hjr22, 11/13/23
    protected synchronized void startRound(Room room) {
        //this is the code to start a new round
        String category = getRandomCategory(); //this gets a random category
        String question = getRandomQuestion(category); //this gets a random question from the selected category
        QuestionOptions potentialAnswers = getPotentialAnswers(category, question);
        room.sendMessage(null, "Category: " + category); //this prints the category to the client from the room
        room.sendMessage(null, "Question: " + question); //this prints the question from the category to the client from the room
        room.setCorrectOption(potentialAnswers.getCorrectOption()); //this sets the anserws to the question
        for (String option : potentialAnswers.getPotentialAnswers()) {
           room.sendMessage(null, "Potential Answer: " + option); //this sends each answer to the client from the room
        }
        room.sendMessage(null, "Round starts now, you have 60 Seconds. Players, enter your answers!");
        //this sends the round starting message

        // this calls the start timer
        startRoundTimer(room);
    }
    
    //get random category method
    private String getRandomCategory() {
        Random random = new Random(); //uses the random method
        int index = random.nextInt(categories.size()); //uses the index of the categories to pick one
        return categories.get(index); //returns the selected category
    }

    //hjr22, 11/13/23
    private String getRandomQuestion(String category) {
        // uses random method
        Random random = new Random();
        switch (category) {
            case "Random": //these cases select a random question from the array if the category is set to a specific category
                return RandomQuestions.get(random.nextInt(RandomQuestions.size()));
            case "Presidents":
                return PresidentsQuestions.get(random.nextInt(PresidentsQuestions.size()));
            case "TV Shows":
                return TVShowQuestions.get(random.nextInt(TVShowQuestions.size()));
            case "Anime":
                return AnimeQuestions.get(random.nextInt(AnimeQuestions.size()));
            case "Videogames":
                return VideogamesQuestions.get(random.nextInt(VideogamesQuestions.size()));
            default: //the default if it can't get anything
                return "No questions available for this category.";
        }
    }

    //hjr22, 11/13/23
    private QuestionOptions getPotentialAnswers(String category, String question) {
        // this retrieves the potential answers for the given question and category
        List<String> potentialAnswers = new ArrayList<>();
        String correctOption = ""; //this sets correctOption to an empty string
        if (question.equals("What State has less water?")) { //Each if checks if the question is set to a specific question
            potentialAnswers = Arrays.asList("Utah", "New Jersey", "Nevada", "West Virginia"); //sets up a potentialAnswers list
            correctOption = "Utah"; //and sets the correctOption to the correct answer
        } else if (question.equals("Which is not a Kardashian?")) {//Then the else if does the same and so on
            potentialAnswers = Arrays.asList("Rob", "Khloe", "Kole", "Kim");
            correctOption = "Kole"; 
        } else if (question.equals("What is the national animal of Scotland?")) {
            potentialAnswers = Arrays.asList("Crow", "Osprey", "Boar", "Unicorn");
            correctOption = "Unicorn";
        } else if (question.equals("What President had 15 children?")) {
            potentialAnswers = Arrays.asList("Andrew Johnson", "Ulysses S. Grant", "Grover Cleveland", "John Tyler");
            correctOption = "John Tyler";
        } else if (question.equals("What President invented their own sport?")) {
            potentialAnswers = Arrays.asList("George Washington", "Herbert Hoover", "Theodore Roosevelt", "Thomas Jefferson");
            correctOption = "Herbert Hoover";
        } else if (question.equals("What President weighs the most?")) {
            potentialAnswers = Arrays.asList("William Howard Taft", "Donald Trump", "John Adams", "Millard Fillmore");
            correctOption = "William Howard Taft";
        } else if (question.equals("In Regular Show: What does G.B.F stand for?")) {
            potentialAnswers = Arrays.asList("Giant Bearded Face", "Garret Bobby Ferguson", "Garry Bobby Ferguson", "Gaming Baby Face");
            correctOption = "Garret Bobby Ferguson";
        } else if (question.equals("In Adventure Time: Who did the Ice King take care of during the Apocalypse?")) {
            potentialAnswers = Arrays.asList("Bubblegum", "Finn", "Marceline", "Fionna");
            correctOption = "Marceline";
        } else if (question.equals("In ICarly: What was Sam obsessed with?")) {
            potentialAnswers = Arrays.asList("Fat Cakes", "Boys", "Sleeping", "Pranking");
            correctOption = "Fat Cakes";
        } else if (question.equals("In One Piece: What is Usopps first bounty?")) {
            potentialAnswers = Arrays.asList("30 Million", "10 Million", "15 Million", "25 Thousand");
            correctOption = "30 Million";
        } else if (question.equals("In Berserk: Who survives The Eclipse?")) {
            potentialAnswers = Arrays.asList("No one", "Guts", "Guts and Griffith", "Guts, Griffith, and Casca");
            correctOption = "Guts and Casca";
        } else if (question.equals("In Dragon Ball: What alter ego does Master Roshi go by during Goku's first tournament?")) {
            potentialAnswers = Arrays.asList("Bruce Tree", "Williard", "Master Yoshi", "Jackie Chun");
            correctOption = "Jackie Chun";
        } else if (question.equals("In Devil May Cry: Dante's brother, Vergil, wields what sword?")) {
            potentialAnswers = Arrays.asList("Muramasa", "Masamune", "Yamato", "Rebellion");
            correctOption = "Yamato";
        } else if (question.equals("In Mortal Kombat: What character is not one of the ninjas?")) {
            potentialAnswers = Arrays.asList("Tremor", "Kenshi", "Reptile", "Rain");
            correctOption = "Kenshi";
        } else if (question.equals("In The Legend of Zelda: What game splits the series into 3 different timelines?")) {
            potentialAnswers = Arrays.asList("Ocarina of Time", "Skyward Sword", "Majora's Mask", "Link's Awakening");
            correctOption = "Ocarina of Time";
        } 
        return new QuestionOptions(question, potentialAnswers, correctOption);
        
        
    }
    //hjr22, 11/13/23
    private void startRoundTimer(Room room) {
        // starts a timer for the round
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        @Override
        public void run() {
            // calls the method to end the round when the timer expires
            //endRound(room);
        }
    }, roundDurationInSeconds * 1000); // Converts the seconds to milliseconds
}

    protected synchronized void endRound(Room room) {
        // this calculate the scores for the round
        calculateRoundScores(room);
        // this sends the round end message when endRound is called after the timer runs out (60 Seconds)
        room.sendMessage(null, "Round ended!");
        startRound(room);
    }
    private void calculateRoundScores(Room room) {
        List<ServerThread> players = room.getClientId();
        for (ServerThread player : players) {
            int score = player.getScore(); // Get the current score of the player
            boolean isCorrect = player.isCorrect(); // Check if the player's answer was correct
            if (isCorrect) {
                score++; // Increment the score if the answer was correct
            }
            player.setScore(score); // Update the player's score
        }
    }
    private Room getRoom(String roomName) {
        for (int i = 0, l = rooms.size(); i < l; i++) {
            if (rooms.get(i).getName().equalsIgnoreCase(roomName)) {
                return rooms.get(i);
            }
        }
        return null;
    }

    /***
     * Attempts to join a room by name. Will remove client from old room and add
     * them to the new room.
     * 
     * @param roomName The desired room to join
     * @param client   The client moving rooms
     * @return true if reassign worked; false if new room doesn't exist
     */
    protected synchronized boolean joinRoom(String roomName, ServerThread client) {
        Room newRoom = roomName.equalsIgnoreCase("lobby") ? lobby : getRoom(roomName);
        Room oldRoom = client.getCurrentRoom();
        if (newRoom != null) {
            if (oldRoom != null && oldRoom != newRoom) {
                System.out.println(client.getName() + " leaving room " + oldRoom.getName());
                oldRoom.removeClient(client);
                client.sendResetUserList();
            }
            System.out.println(client.getName() + " joining room " + newRoom.getName());
            newRoom.addClient(client);
            return true;
        }
        return false;
    }

    /***
     * Attempts to create a room with given name if it doesn't exist already.
     * 
     * @param roomName The desired room to create
     * @return true if it was created and false if it exists
     */
    protected synchronized boolean createNewRoom(String roomName) {
        if (getRoom(roomName) != null) {
            // TODO can't create room
            System.out.println(String.format("Room %s already exists", roomName));
            return false;
        } else {
            Room room = new Room(roomName);
            rooms.add(room);
            System.out.println("Created new room: " + roomName);
            return true;
        }
    }

    protected synchronized List<String> getRooms(String query){
        return getRooms(query, 10);
    }
    /**
     * Returns Rooms with names having a partial match with query.
     * @param query
     * @param limit The maximum records to return
     * @return
     */
    protected synchronized List<String> getRooms(String query, int limit){
        List<String> matchedRooms = new ArrayList<String>();
        synchronized(rooms){
            Iterator<Room> iter = rooms.iterator();
            while(iter.hasNext()){
                Room r = iter.next();
                if(r.isRunning() && r.getName().toLowerCase().contains(query.toLowerCase())){
                    matchedRooms.add(r.getName());
                    if(matchedRooms.size() >= limit){
                        break;
                    }
                }
            }
        }
        return matchedRooms;
    }
    protected synchronized void removeRoom(Room r) {
        if (rooms.removeIf(room -> room == r)) {
            System.out.println("Removed empty room " + r.getName());
        }
    }

    protected synchronized void broadcast(String message) {
        if (processCommand(message)) {

            return;
        }
        // loop over rooms and send out the message
        Iterator<Room> it = rooms.iterator();
        while (it.hasNext()) {
            Room room = it.next();
            if (room != null) {
                room.sendMessage(null, message);
            }
        }
    }

    private boolean processCommand(String message) {
        System.out.println("Checking command: " + message);
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Starting Server");
        Server server = Server.INSTANCE;
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // can ignore, will either be index out of bounds or type mismatch
            // will default to the defined value prior to the try/catch
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}
