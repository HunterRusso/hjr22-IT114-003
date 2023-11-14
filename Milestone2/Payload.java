package Milestone2;
import java.io.Serializable;
import java.util.List;
public class Payload implements Serializable {
    //read https://www.baeldung.com/java-serial-version-uid
    private static final long serialVersionUID = 1L;//change this if the class changes
    
    /**
     * Determines how to process the data on the receiver's side
     */
    private PayloadType payloadType;
    public PayloadType getPayloadType() {
        return payloadType;
    }
    public void setPayloadType(PayloadType payloadType) {
        this.payloadType = payloadType;
    }

    /**
     * Who the payload is from
     */
    private String clientName;
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Generic text based message
     */
    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * Generic number for example sake
     */
    private int number;
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    //hjr22, 11/13/23
    @Override
    public String toString() {
        return String.format("Type[%s], Category[%s], Question[%s], Options[%s], Number[%s], Message[%s]",
        getPayloadType().toString(), getCategory(), getQuestion(), getOptions(), getNumber(), getMessage());
    }
    private String category;
    private String question;
    private List<String> options;
    private int timeLimit;
    private int score;
    public String getCategory() {
        return category;
    }
    //these are all of the setters and getters
    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    private List<String> playerIds;

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<String> playerIds) {
        this.playerIds = playerIds;
    }
    
}