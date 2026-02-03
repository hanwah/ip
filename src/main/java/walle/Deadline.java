package walle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Changed walle.Deadline task to work with localDate
public class Deadline extends Task {
    private final LocalDateTime by;

    private static final DateTimeFormatter OUT_FMT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"); // e.g. Oct 15 2019 18:00

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    // Dispaly the strng for Deadline task
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUT_FMT) + ")";
    }
}
