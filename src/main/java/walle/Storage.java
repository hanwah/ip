package walle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;



/**
 * Handles loading tasks from disk and saving tasks to disk.
 * Responsible for persistence of the task list.
 */
public class Storage {

    private static final DateTimeFormatter SAVE_DATE_TIME_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Path savePath;


    /**
     * Creates a storage handler that reads from and writes to the specified file path.
     * @param filePath Path to the save file.
     */
    public Storage(String filePath) {
        this.savePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the save file.
     *
     * @return A task list containing tasks read from storage.
     * @throws WalleException If the file cannot be read or data format is invalid.
     */
    public ArrayList<Task> load() throws WalleException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(savePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(savePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                tasks.add(parseSavedLine(line));
            }
            return tasks;

        } catch (IOException e) {
            throw new WalleException("Warning: save file is unreadable. " + e.getMessage());
        }
    }

    /**
     * Saves tasks to the save file, overwriting existing content.
     *
     * @param tasks The tasks to be saved.
     * @throws WalleException If the file cannot be written.
     */
    public void save(ArrayList<Task> tasks) throws WalleException {
        try {
            if (savePath.getParent() != null) {
                Files.createDirectories(savePath.getParent());
            }

            ArrayList<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                lines.add(serializeTask(t));
            }
            Files.write(savePath, lines);

        } catch (IOException e) {
            throw new WalleException("Oops — couldn't save tasks: " + e.getMessage());
        }
    }

    /**
     * Serializes a task into a single line suitable for saving to a text file.
     * The output format uses a leading task type (T/D/E), a done flag (1/0),
     * and any required date/time fields.
     *
     * @param t Task to serialize.
     * @return A single-line string representation of the task for storage.
     */
    private String serializeTask(Task t) {
        String doneBit = t.isDone() ? "1" : "0";

        if (t instanceof Todo) {
            return "T | " + doneBit + " | " + t.getDescription();
        }
        if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D | " + doneBit + " | " + d.getDescription()
                    + " | " + d.getBy().format(SAVE_DATE_TIME_FMT);
        }
        if (t instanceof Event) {
            Event event = (Event) t;
            return "E | " + doneBit + " | " + event.getDescription()
                    + " | " + event.getFrom().format(SAVE_DATE_TIME_FMT)
                    + " | " + event.getTo().format(SAVE_DATE_TIME_FMT);
        }

        return "T | " + doneBit + " | " + t.getDescription();
    }

    /**
     * Parses a saved task line from the storage file and reconstructs the corresponding {@link Task}.
     * Expected formats:
     * <ul>
     *   <li>{@code T | 0/1 | <description>}</li>
     *   <li>{@code D | 0/1 | <description> | <yyyy-MM-dd HHmm>}</li>
     *   <li>{@code E | 0/1 | <description> | <start> | <end>}</li>
     * </ul>
     *
     * AI-assisted : Used ChatGPT to suggest validation checks and a parsing flow
     * for corrupted or incomplete saved lines.
     *
     * @param line A single line read from the save file.
     * @return The reconstructed task represented by the saved line.
     * @throws WalleException If the line format is invalid, incomplete, or contains invalid date/time data.
     */
    private static Task parseSavedLine(String line) throws WalleException {
        String[] parts = line.split("\\s*\\|\\s*");

        if (parts.length < 3) {
            throw new WalleException("Invalid save line: " + line);
        }

        String type = parts[0].trim();
        boolean done = parts[1].trim().equals("1");
        String desc = parts[2].trim();

        Task t;
        try {
            switch (type) {
            case "T":
                t = new Todo(desc);
                break;

            case "D":
                if (parts.length < 4) {
                    throw new WalleException("Invalid deadline line: " + line);
                }
                LocalDateTime by = LocalDateTime.parse(parts[3].trim(), SAVE_DATE_TIME_FMT);
                t = new Deadline(desc, by);
                break;

            case "E":
                if (parts.length < 5) {
                    throw new WalleException("Invalid event line: " + line);
                }
                LocalDateTime from = LocalDateTime.parse(parts[3].trim(), SAVE_DATE_TIME_FMT);
                LocalDateTime to = LocalDateTime.parse(parts[4].trim(), SAVE_DATE_TIME_FMT);
                t = new Event(desc, from, to);
                break;

            default:
                throw new WalleException("Unknown task type in save file: " + type);
            }
        } catch (DateTimeParseException e) {
            throw new WalleException("Invalid date/time in save line: " + line);
        }

        if (done) {
            t.markDone();
        } else {
            t.markUndone();
        }

        return t;
    }

}
