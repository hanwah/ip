package walle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Changed walle.Event class to work with LocalDate
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    private static final DateTimeFormatter OUT_FMT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"); // e.g. Oct 15 2019 18:00

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

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from.format(OUT_FMT)
                + " to: " + to.format(OUT_FMT) + ")";
    }
}
