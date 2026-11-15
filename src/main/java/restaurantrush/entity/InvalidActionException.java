package restaurantrush.entity;

// Thrown when a manager choice breaks a game rule (wrong customer state, staff already
// acted, no free table, ...). The message is shown to the manager, who then chooses again
// (see Staff for why a rejected choice does not use up the task).
public class InvalidActionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidActionException(String message) {
        super(message);
    }
}
