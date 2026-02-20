package walle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input into command components.
 * Provides helper methods to identify commands and extract arguments.
 */
public class Parser {


    private static final DateTimeFormatter DATE_TIME_IN_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");


    /**
     * Returns whether the input is a {@code bye} command.
     *
     * @param input Raw user input.
     * @return True if bye; false otherwise.
     */
    public static boolean isBye(String input) {
        return input.equalsIgnoreCase("bye");
    }

    /**
     * Returns whether the input is a {@code help} command.
     *
     * @param input Raw user input.
     * @return True  help; false otherwise.
     */
    public static boolean isHelp(String input) {
        return input.equalsIgnoreCase("help");
    }

    /**
     * Returns whether the input is a {@code list} command.
     *
     * @param input Raw user input.
     * @return True list; false otherwise.
     */
    public static boolean isList(String input) {
        return input.equals("list");
    }

    /**
     * Returns whether the input is a {@code todo} command.
     *
     * @param input Raw user input.
     * @return True if todo; false otherwise.
     */
    public static boolean isTodo(String input) {
        return input.trim().equals("todo") || input.startsWith("todo ");
    }

    /**
     * Returns whether the input is a {@code deadline} command.
     *
     * @param input Raw user input.
     * @return True if deadline; false otherwise.
     */
    public static boolean isDeadline(String input) {
        return input.startsWith("deadline ");
    }

    /**
     * Returns whether the input is an {@code event} command.
     *
     * @param input Raw user input.
     * @return True if event; false otherwise.
     */
    public static boolean isEvent(String input) {
        return input.startsWith("event ");
    }

    /**
     * Returns whether the input is a {@code mark} command.
     *
     * @param input Raw user input.
     * @return True if mark; false otherwise.
     */
    public static boolean isMark(String input) {
        return input.toLowerCase().startsWith("mark ");
    }

    /**
     * Returns whether the input is a {@code unmark} command.
     *
     * @param input Raw user input.
     * @return True if unmark; false otherwise.
     */
    public static boolean isUnmark(String input) {
        return input.toLowerCase().startsWith("unmark ");
    }

    /**
     * Returns whether the input is a {@code delete} command.
     *
     * @param input Raw user input.
     * @return True if delete; false otherwise.
     */
    public static boolean isDelete(String input) {
        return input.toLowerCase().startsWith("delete ");
    }

    /**
     * Returns whether the input is a {@code find} command.
     *
     * @param input Raw user input.
     * @return True if find; false otherwise.
     */
    public static boolean isFind(String input) {
        return input.toLowerCase().startsWith("find ");
    }


    /**
     * Extracts the description portion of a {@code todo} command.
     *
     * @param input Raw user input beginning with {@code todo}.
     * @return The todo description.
     * @throws WalleException If the description is missing/invalid.
     */
    public static String parseTodoDescription(String input) throws WalleException {
        assert input != null : "input should not be null";
        assert isTodo(input) : "parseTodoDescription called when input is not a todo command";
        if (input.trim().equals("todo")) {
            throw new WalleException("Oops — the description of a todo cannot be empty.");
        }

        String desc = input.substring(5).trim(); // after "todo "
        if (desc.isEmpty()) {
            throw new WalleException("Oops — the description of a todo cannot be empty.");
        }
        return desc;
    }

    /**
     * Returns the keyword portion of a {@code find} command.
     *
     * @param input Raw user input beginning with {@code find}.
     * @return Keyword to search for.
     * @throws WalleException If the keyword is missing.
     */
    public static String parseFindKeyword(String input) throws WalleException {
        assert input != null : "input should not be null";
        assert isFind(input) : "parseFindKeyword called when input is not a find command";
        String keyword = input.substring(5).trim(); // after "find "
        if (keyword.isEmpty()) {
            throw new WalleException("Oops — please provide a keyword to find. (e.g., find meeting)");
        }
        return keyword;
    }

    /**
     * Parses and validates a 1-based task index from user input.
     * Ensures the index is present, numeric, and within valid bounds.
     *
     * AI-assisted : Used ChatGPT to identify common error cases
     * such as missing indices, non-integer input, and out-of-range values.
     *
     * @param s Raw index string provided by the user.
     * @param commandName Name of the command using the index (for error messages).
     * @param taskCount Total number of tasks currently stored.
     * @return Parsed valid task index.
     * @throws WalleException If the index is missing, invalid, or out of range.
     */
    private static int parseIndex(String s, String commandName, int taskCount) throws WalleException {
        assert commandName != null && !commandName.isBlank() : "commandName must be set";
        assert taskCount >= 0 : "taskCount should never be negative";

        // If user did not provide a number
        if (s.isEmpty()) {
            throw new WalleException("Oops — please provide a task number for " + commandName
                    + ". (e.g., " + commandName + " 2)");
        }

        int idx;

        // If user provided a non-integer
        try {
            idx = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new WalleException("Oops — that task number must be an integer. (e.g., " + commandName + " 2)");
        }

        // If user inputs out of range index
        if (idx < 1 || idx > taskCount) {
            throw new WalleException("Oops — task number " + idx
                    + " is out of range. Use 'list' to see valid task numbers.");
        }

        return idx;
    }

    /**
     * Parses a {@code deadline} command and constructs a {@link Deadline} task.
     * Expected format: {@code deadline <description> /by <yyyy-MM-dd HHmm>}.
     *
     * AI-assisted : Used ChatGPT to identify input error cases such as
     * missing {@code /by}, empty description, empty date/time, and invalid date/time formats.
     *
     * @param input Raw user input beginning with {@code deadline}.
     * @return A {@link Deadline} task containing the description and due date/time.
     * @throws WalleException If the command format is invalid or the date/time cannot be parsed.
     */
    public static Deadline parseDeadline(String input) throws WalleException {
        assert input != null : "input should not be null";
        assert isDeadline(input) : "parseDeadline called when input is not a deadline command";
        int byPos = input.indexOf(" /by ");
        if (byPos == -1) {
            throw new WalleException("Oops — deadline format should be:\n"
                                        + "  deadline <description> /by <yyyy-MM-dd HHmm>");
        }
        String desc = input.substring("deadline ".length(), byPos).trim();
        String byStr = input.substring(byPos + " /by ".length()).trim();
        if (desc.isEmpty()) {
            throw new WalleException("Oops — the description of a deadline cannot be empty.");
        }
        if (byStr.isEmpty()) {
            throw new WalleException("Oops — the /by part of a deadline cannot be empty.");
        }
        try {
            LocalDateTime by = LocalDateTime.parse(byStr, DATE_TIME_IN_FMT);
            return new Deadline(desc, by);
        } catch (DateTimeParseException e) {
            throw new WalleException("Oops — use yyyy-MM-dd HHmm (e.g., 2019-10-15 1800).");
        }
    }


    /**
     * Parses an {@code event} command and constructs an {@link Event} task.
     * Expected format: {@code event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>}.
     *
     * AI-assisted : Used ChatGPT to identify input error cases such as
     * missing {@code /from} or {@code /to}, empty fields, invalid date/time formats,
     * and ensuring the end time is not before the start time.
     *
     * @param input Raw user input beginning with {@code event}.
     * @return An {@link Event} task containing the description, start time, and end time.
     * @throws WalleException If the command format is invalid, date/time cannot be parsed,
     *                        or the end time is before the start time.
     */
    public static Event parseEvent(String input) throws WalleException {
        assert input != null : "input should not be null";
        assert isEvent(input) : "parseEvent called when input is not an event command";
        int fromPos = input.indexOf(" /from ");
        int toPos = input.indexOf(" /to ");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new WalleException("Oops — event format should be:\n  event <description> /from <start> /to <end>");
        }

        String desc = input.substring("event ".length(), fromPos).trim();
        String fromStr = input.substring(fromPos + " /from ".length(), toPos).trim();
        String toStr = input.substring(toPos + " /to ".length()).trim();

        if (desc.isEmpty()) {
            throw new WalleException("Oops — the description of an event cannot be empty.");
        }
        if (fromStr.isEmpty()) {
            throw new WalleException("Oops — the /from part of an event cannot be empty.");
        }
        if (toStr.isEmpty()) {
            throw new WalleException("Oops — the /to part of an event cannot be empty.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(fromStr, DATE_TIME_IN_FMT);
            LocalDateTime to = LocalDateTime.parse(toStr, DATE_TIME_IN_FMT);

            if (to.isBefore(from)) {
                throw new WalleException("Oops — event end time must be after start time.");
            }

            return new Event(desc, from, to);

        } catch (DateTimeParseException e) {
            throw new WalleException("Oops — use yyyy-MM-dd HHmm (e.g., 2019-10-15 1800).");
        }
    }

    /**
     * Extracts the task index from a {@code mark} command.
     *
     *  AI assisted: Used ChatGPT to use small wrapper methods to reuse parseIndex()
     *
     * @param input Raw user input beginning with {@code mark}.
     * @param taskCount Current number of tasks (for bounds validation).
     * @return The 1-based task index.
     * @throws WalleException If the index is missing, non-numeric, or out of range.
     */
    public static int parseMarkIndex(String input, int taskCount) throws WalleException {
        assert input != null : "input should not be null";
        assert isMark(input) : "parseMarkIndex called when input is not a mark command";
        return parseIndex(input.substring(5).trim(), "mark", taskCount);
    }


    /**
     * Parses the task index from an {@code unmark} command.
     *
     * Expected format: {@code unmark <task number>}.
     *
     * @param input Raw user input beginning with {@code unmark}.
     * @param taskCount Total number of tasks currently stored.
     * @return Parsed valid task index (1-based).
     * @throws WalleException If the index is missing, invalid, or out of range.
     */
    public static int parseUnmarkIndex(String input, int taskCount) throws WalleException {
        assert input != null : "input should not be null";
        assert isMark(input) : "parseMarkIndex called when input is not a unmark command";
        return parseIndex(input.substring(7).trim(), "unmark", taskCount);
    }

    /**
     * Parses the task index from a {@code delete} command.
     *
     * Expected format: {@code delete <task number>}.
     *
     * @param input Raw user input beginning with {@code delete}.
     * @param taskCount Total number of tasks currently stored.
     * @return Parsed valid task index (1-based).
     * @throws WalleException If the index is missing, invalid, or out of range.
     */
    public static int parseDeleteIndex(String input, int taskCount) throws WalleException {
        assert input != null : "input should not be null";
        assert isMark(input) : "parseMarkIndex called when input is not a delete command";
        return parseIndex(input.substring(7).trim(), "delete", taskCount);
    }

    public static boolean isUndo(String input) {
        return input.equalsIgnoreCase("undo");
    }


}
