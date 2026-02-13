package walle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// Localdate
import java.time.format.DateTimeFormatter;

/**
 * Handles loading tasks from disk and saving tasks to disk.
 * Responsible for persistence of the task list.
 */

public class Storage {

    // Location of the saved file
    private final Path savePath;

    private static final DateTimeFormatter SAVE_DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter SAVE_DATE_TIME_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    public Storage(String filePath) {
        this.savePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the save file.
     *
     * @return A task list containing tasks read from storage.
     * @throws WAllEException If the file cannot be read or data format is invalid.
     */
    public ArrayList<Task> load() throws WAllEException {
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
            throw new WAllEException("Warning: save file is unreadable. " + e.getMessage());
        }
    }

    /**
     * Saves tasks to the save file, overwriting existing content.
     *
     * @param tasks The tasks to be saved.
     * @throws WAllEException If the file cannot be written.
     */

    public void save(ArrayList<Task> tasks) throws WAllEException {
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
            throw new WAllEException("Oops — couldn't save tasks: " + e.getMessage());
        }
    }

    // Helper function to save task into designated .txt file
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
            Event e = (Event) t;
            return "E | " + doneBit + " | " + e.getDescription()
                    + " | " + e.getFrom().format(SAVE_DATE_TIME_FMT)
                    + " | " + e.getTo().format(SAVE_DATE_TIME_FMT);
        }

        return "T | " + doneBit + " | " + t.getDescription();
    }

    // AI attribution (ChatGPT): Suggested validation + parsing flow for saved lines
    private static Task parseSavedLine(String line) throws WAllEException {
        String[] parts = line.split("\\s*\\|\\s*");

        if (parts.length < 3) {
            throw new WAllEException("Invalid save line: " + line);
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
                    throw new WAllEException("Invalid deadline line: " + line);
                }
                LocalDateTime by = LocalDateTime.parse(parts[3].trim(), SAVE_DATE_TIME_FMT);
                t = new Deadline(desc, by);
                break;

            case "E":
                if (parts.length < 5) {
                    throw new WAllEException("Invalid event line: " + line);
                }
                LocalDateTime from = LocalDateTime.parse(parts[3].trim(), SAVE_DATE_TIME_FMT);
                LocalDateTime to = LocalDateTime.parse(parts[4].trim(), SAVE_DATE_TIME_FMT);
                t = new Event(desc, from, to);
                break;

            default:
                throw new WAllEException("Unknown task type in save file: " + type);
            }
        } catch (DateTimeParseException e) {
            throw new WAllEException("Invalid date/time in save line: " + line);
        }

        if (done) t.Done();
        else t.Undone();

        return t;
    }

}
