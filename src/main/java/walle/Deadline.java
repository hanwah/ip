package walle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline date/time.
 * A {@code Deadline} is considered complete when its status is marked done.
 */

//Changed walle.Deadline task to work with localDate
public class Deadline extends Task {
    private static final DateTimeFormatter OUT_FMT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"); // e.g. Oct 15 2019 18:00

    private final LocalDateTime by;

    /**
     * Creates a deadline task.
     *
     * @param description Description of the task.
     * @param by Deadline date/time.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline date/time of this task.
     *
     * @return Deadline date/time.
     */
    public LocalDateTime getBy() {
        return by;
    }


    /**
     * Returns a user-friendly string representation of this deadline task.
     *
     * @return Formatted task string.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUT_FMT) + ")";
    }
}
