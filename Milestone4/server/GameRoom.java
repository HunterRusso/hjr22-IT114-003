package Milestone4.server;
import Milestone4.common.Utils;
import Milestone4.server.Server.QuestionOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

import Milestone4.common.Constants;
import Milestone4.common.Phase;
import Milestone4.common.TimedEvent;

public class GameRoom extends Room {
    Phase currentPhase = Phase.READY;
    private static Logger logger = Logger.getLogger(GameRoom.class.getName());
    private TimedEvent readyTimer = null;
    private ConcurrentHashMap<Long, ServerPlayer> players = new ConcurrentHashMap<Long, ServerPlayer>();
    private Character currentTurnCharacter = null;
    Random rand = new Random();
    private List<Character> turnOrder = new ArrayList<Character>();
    private final List<String> categories = Arrays.asList("Random", "Presidents", "TV Shows", "Anime", "Videogames");
    //these are a list of each catagories questions
    private final List<String> RandomQuestions = Arrays.asList("What State has less water?", "Which is not a Kardashian?", "What is the national animal of Scotland?");
    private final List<String> PresidentsQuestions = Arrays.asList("What President had 15 children?", "What President invented their own sport?", "What President weighs the most?");
    private final List<String> TVShowQuestions = Arrays.asList("In Regular Show: What does G.B.F stand for?", "In Adventure Time: Who did the Ice King take care of during the Apocalypse?", "In ICarly: What was Sam obsessed with?");
    private final List<String> AnimeQuestions = Arrays.asList("In One Piece: What is Usopps first bounty?", "In Berserk: Who survives The Eclipse?", "In Dragon Ball: What alter ego does Master Roshi go by during Goku's first tournament?");
    private final List<String> VideogamesQuestions = Arrays.asList("In Devil May Cry: Dante's brother, Vergil, wields what sword?", "In Mortal Kombat: What character is not one of the ninjas?", "In The Legend of Zelda: What game splits the series into 3 different timelines?");
    //this sets the round duration to 60 seconds
    private final int roundDurationInSeconds = 60;

    public GameRoom(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void addClient(ServerThread client) {
        logger.info("Adding client as player");
        players.computeIfAbsent(client.getClientId(), id -> {
            ServerPlayer player = new ServerPlayer(client);
            super.addClient(client);
            logger.info(String.format("Total clients %s", clients.size()));// change visibility to protected
            return player;
        });
    }

    protected void setReady(ServerThread client) {
        logger.info("Ready check triggered");
        if (currentPhase != Phase.READY) {
            logger.warning(String.format("readyCheck() incorrect phase: %s", Phase.READY.name()));
            return;
        }
        if (readyTimer == null) {
            sendMessage(null, "Ready Check Initiated, 30 seconds to join");
            readyTimer = new TimedEvent(30, () -> {
                readyTimer = null;
                readyCheck(true);
            });
        }
        // Hashmaps allow fast lookup by keys
        if (players.containsKey(client.getClientId())) {
            ServerPlayer sp = players.get(client.getClientId());
            sp.setReady(true);
            logger.info(String.format("Marked player %s[%s] as ready", sp.getClient().getClientName(), sp
                    .getClient().getClientId()));
            syncReadyStatus(sp.getClient().getClientId());
        }
        /*
         * Example demonstrating stream api and filters (not ideal in this scenario
         * since a hashmap has a more officient approach)
         * This concept may be beneficial in the future for other lookup data
         * players.values().stream().filter(p -> p.getClient().getClientId() ==
         * client.getClientId()).findFirst()
         * .ifPresent(p -> {
         * p.setReady(true);
         * logger.info(String.format("Marked player %s[%s] as ready",
         * p.getClient().getClientName(), p
         * .getClient().getClientId()));
         * syncReadyStatus(p.getClient().getClientId());
         * });
         */
        readyCheck(false);
    }

    //hjr22 12/11/23
    private void readyCheck(boolean timerExpired) {
        if (currentPhase != Phase.READY) {
            return;
        }
        // two examples for the same result
        // int numReady = players.values().stream().mapToInt((p) -> p.isReady() ? 1 :
        // 0).sum();
        long numReady = players.values().stream().filter(ServerPlayer::isReady).count();
        if (numReady >= Constants.MINIMUM_PLAYERS) {

            if (timerExpired) {
                sendMessage(null, "Ready Timer expired, starting session");
                start();
            } else if (numReady >= players.size()) {
                sendMessage(null, "Everyone in the room marked themselves ready, starting session");
                if (readyTimer != null) {
                    readyTimer.cancel();
                    readyTimer = null;
                }
                start();
            }

        } else {
            if (timerExpired) {
                resetSession();
                sendMessage(null, "Ready Timer expired, not enough players. Resetting ready check");
            }
        }
    }

    private void start() {
        updatePhase(Phase.SELECTION);
        // TODO example
        sendMessage(null,
                "Session started: ");

    //hjr22 12/11/23
    }
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

    //hjr22, 12/11/23
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
    //hjr22, 12/11/23
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

//hjr22 12/11/23
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

    // start handle next turn
    private void nextTurn() {
        updatePhase(Phase.TURN);
        if (currentTurnCharacter == null) {
            currentTurnCharacter = turnOrder.get(0);
        } else {
            int currentIndex = turnOrder.indexOf(currentTurnCharacter);
            currentIndex++;
            if (currentIndex >= turnOrder.size()) {
                currentIndex = 0;
            }
            currentTurnCharacter = turnOrder.get(currentIndex);
        }
        if (currentTurnCharacter != null) {
            ServerPlayer sp = ((ServerPlayer) currentTurnCharacter.getController());
            syncCurrentTurn(sp.getClient().getClientId());
            sendMessage(null, String.format("It's %s's turn", sp.getClient().getClientName()));
            cancelReadyTimer();
            // TODO set back to lower number after debugging
            readyTimer = new TimedEvent(3000, () -> {
                sendMessage(null,
                        String.format("%s took too long and has been skipped", sp.getClient().getClientName()));
                nextTurn();
            });
        }
    }

    private synchronized void syncCurrentTurn(long clientId) {
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendCurrentTurn(clientId);
            if (!success) {
                handleDisconnect(client);
            }
        }
    }

    // end handle next turn
    private void cancelReadyTimer() {
        if (readyTimer != null) {
            readyTimer.cancel();
            readyTimer = null;
        }
    }
    private synchronized void resetSession() {
        players.values().stream().forEach(p -> p.setReady(false));
        updatePhase(Phase.READY);
        sendMessage(null, "Session ended, please intiate ready check to begin a new one");
    }

    private void updatePhase(Phase phase) {
        if (currentPhase == phase) {
            return;
        }
        currentPhase = phase;
        // NOTE: since the collection can yield a removal during iteration, an iterator
        // is better than relying on forEach
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendPhaseSync(currentPhase);
            if (!success) {
                handleDisconnect(client);
            }
        }
    }

    protected void handleDisconnect(ServerPlayer player) {
        if (players.containsKey(player.getClient().getClientId())) {
            players.remove(player.getClient().getClientId());
            super.handleDisconnect(null, player.getClient()); // change visibility to protected
            logger.info(String.format("Total clients %s", clients.size()));
            sendMessage(null, player.getClient().getClientName() + " disconnected");
            if (players.isEmpty()) {
                close();
            }
        }
    }

    private void syncReadyStatus(long clientId) {
        Iterator<ServerPlayer> iter = players.values().stream().iterator();
        while (iter.hasNext()) {
            ServerPlayer client = iter.next();
            boolean success = client.getClient().sendReadyStatus(clientId);
            if (!success) {
                handleDisconnect(client);
            }
        }
    }
    @Override
    public void close(){
        super.close();
        players.clear();
        players = null;
        currentTurnCharacter = null;
        //turnOrder.clear(); // this is actually an immutable array so can't clear it
        turnOrder = null;
    }
}
