package walle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {


    private static final DateTimeFormatter DATE_TIME_IN_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");


    public static boolean isBye(String input) {
        return input.equalsIgnoreCase("bye");
    }

    public static boolean isHelp(String input) {
        return input.equalsIgnoreCase("help");
    }

    public static boolean isList(String input) {
        return input.equals("list");
    }

    public static boolean isTodo(String input) {
        return input.trim().equals("todo") || input.startsWith("todo ");
    }

    public static boolean isDeadline(String input) {
        return input.startsWith("deadline ");
    }

    public static boolean isEvent(String input) {
        return input.startsWith("event ");
    }

    public static boolean isMark(String input) {
        return input.toLowerCase().startsWith("mark ");
    }

    public static boolean isUnmark(String input) {
        return input.toLowerCase().startsWith("unmark ");
    }

    public static boolean isDelete(String input) {
        return input.toLowerCase().startsWith("delete ");
    }

    public static boolean isFind(String input) {
        return input.toLowerCase().startsWith("find ");
    }


    public static String parseTodoDescription(String input) throws WAllEException {
        assert input != null : "input should not be null";
        assert isTodo(input) : "parseTodoDescription called when input is not a todo command";
        if (input.trim().equals("todo")) {
            throw new WAllEException("Oops — the description of a todo cannot be empty.");
        }

        String desc = input.substring(5).trim(); // after "todo "
        if (desc.isEmpty()) {
            throw new WAllEException("Oops — the description of a todo cannot be empty.");
        }
        return desc;
    }

    public static String parseFindKeyword(String input) throws WAllEException {
        assert input != null : "input should not be null";
        assert isFind(input) : "parseFindKeyword called when input is not a find command";
        String keyword = input.substring(5).trim(); // after "find "
        if (keyword.isEmpty()) {
            throw new WAllEException("Oops — please provide a keyword to find. (e.g., find meeting)");
        }
        return keyword;
    }

    // AI attribution, used ChatGPT to brainstorm error cases (missing /from, /to, /by, invalid indexes).
    // index parsing  exception cases inspired by ChatGPT
    private static int parseIndex(String s, String commandName, int taskCount) throws WAllEException {
        assert commandName != null && !commandName.isBlank() : "commandName must be set";
        assert taskCount >= 0 : "taskCount should never be negative";

        // If user did not provide a number
        if (s.isEmpty()) {
            throw new WAllEException("Oops — please provide a task number for " + commandName
                    + ". (e.g., " + commandName + " 2)");
        }

        int idx;

        // If user provided a non-integer
        try {
            idx = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new WAllEException("Oops — that task number must be an integer. (e.g., " + commandName + " 2)");
        }

        // If user inputs out of range index
        if (idx < 1 || idx > taskCount) {
            throw new WAllEException("Oops — task number " + idx
                    + " is out of range. Use 'list' to see valid task numbers.");
        }

        return idx;
    }

    // walle.Deadline helper function exception cases inspired by ChatGPT
    public static Deadline parseDeadline(String input) throws WAllEException {
        assert input != null : "input should not be null";
        assert isDeadline(input) : "parseDeadline called when input is not a deadline command";
        int byPos = input.indexOf(" /by ");

        if (byPos == -1) {
            throw new WAllEException("Oops — deadline format should be:\n  deadline <description> /by <yyyy-MM-dd HHmm>");
        }

        String desc = input.substring("deadline ".length(), byPos).trim();
        String byStr = input.substring(byPos + " /by ".length()).trim();

        if (desc.isEmpty()) {
            throw new WAllEException("Oops — the description of a deadline cannot be empty.");
        }
        if (byStr.isEmpty()) {
            throw new WAllEException("Oops — the /by part of a deadline cannot be empty.");
        }

        try {
            LocalDateTime by = LocalDateTime.parse(byStr, DATE_TIME_IN_FMT);
            return new Deadline(desc, by);
        } catch (DateTimeParseException e) {
            throw new WAllEException("Oops — use yyyy-MM-dd HHmm (e.g., 2019-10-15 1800).");
        }
    }


    // walle.Event helper function exception cases inspired by ChatGPT
    public static Event parseEvent(String input) throws WAllEException {
        assert input != null : "input should not be null";
        assert isEvent(input) : "parseEvent called when input is not an event command";
        int fromPos = input.indexOf(" /from ");
        int toPos = input.indexOf(" /to ");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new WAllEException("Oops — event format should be:\n  event <description> /from <start> /to <end>");
        }

        String desc = input.substring("event ".length(), fromPos).trim();
        String fromStr = input.substring(fromPos + " /from ".length(), toPos).trim();
        String toStr = input.substring(toPos + " /to ".length()).trim();

        if (desc.isEmpty()) {
            throw new WAllEException("Oops — the description of an event cannot be empty.");
        }
        if (fromStr.isEmpty()) {
            throw new WAllEException("Oops — the /from part of an event cannot be empty.");
        }
        if (toStr.isEmpty()) {
            throw new WAllEException("Oops — the /to part of an event cannot be empty.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(fromStr, DATE_TIME_IN_FMT);
            LocalDateTime to = LocalDateTime.parse(toStr, DATE_TIME_IN_FMT);

            if (to.isBefore(from)) {
                throw new WAllEException("Oops — event end time must be after start time.");
            }

            return new Event(desc, from, to);

        } catch (DateTimeParseException e) {
            throw new WAllEException("Oops — use yyyy-MM-dd HHmm (e.g., 2019-10-15 1800).");
        }
    }

    // while handling different commands such as "mark ", "unmark ", "delete "
    // AI attribution: Suggested using small wrapper methods to reuse parseIndex()
    public static int parseMarkIndex(String input, int taskCount) throws WAllEException {
        assert input != null : "input should not be null";
        assert isMark(input) : "parseMarkIndex called when input is not a mark command";
        return parseIndex(input.substring(5).trim(), "mark", taskCount);
    }



    public static int parseUnmarkIndex(String input, int taskCount) throws WAllEException {
        assert input != null : "input should not be null";
        assert isMark(input) : "parseMarkIndex called when input is not a unmark command";
        return parseIndex(input.substring(7).trim(), "unmark", taskCount);
    }


    public static int parseDeleteIndex(String input, int taskCount) throws WAllEException {
        assert input != null : "input should not be null";
        assert isMark(input) : "parseMarkIndex called when input is not a delete command";
        return parseIndex(input.substring(7).trim(), "delete", taskCount);
    }

}
