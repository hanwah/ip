package walle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a start and end date/time.
 * An {@code Event} is considered complete when its status is marked done.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    private static final DateTimeFormatter OUT_FMT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"); // e.g. Oct 15 2019 18:00

    /**
     * Creates an event task.
     *
     * @param description Description of the event.
     * @param from Start date/time as a formatted string.
     * @param to End date/time as a formatted string.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns a user-friendly string representation of this event task.
     *
     * @return Formatted task string.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from.format(OUT_FMT)
                + " to: " + to.format(OUT_FMT) + ")";
    }
}
