package walle;

/**
 * Represents a task with a description and completion status.
 * Concrete task types (e.g., Todo, Deadline, Event) extend this class.
 */

public class Task {

    private final String description;
    // Used enum instead of boolean
    private Status status;

    /**
     * Creates a task with the given description.
     *
     * @param description Task description.
     */

    public Task(String description) {
        assert description != null : "description should not be null";
        assert !description.isBlank() : "description should not be blank";
        this.description = description;
        this.status = Status.NOT_DONE;
    }

    /**
     * Marks this task as done.
     */

    public void Done() {
        status = Status.DONE;
    }

    /**
     * Marks this task as not done.
     */
    public void Undone() {
        status = Status.NOT_DONE;
    }

    /**
     * checks if a task is done or not done.
     */
    public boolean isDone() {
        return status == Status.DONE;
    }

    /**
     * Returns a user-friendly string representation of this task.
     *
     * @return Formatted task string.
     */

    @Override
    public String toString() {
        String box = (status == Status.DONE) ? "[X] " : "[ ] ";
        return box + description;
    }

    public String getDescription() {
        return description; // change if your field name differs
    }



}
