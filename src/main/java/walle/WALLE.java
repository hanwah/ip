package walle;

public class WALLE {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public WALLE(String saveFilePath) {
        ui = new Ui();
        storage = new Storage(saveFilePath);

        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (WAllEException e) {
            // In GUI, we return a message instead of printing.
            loaded = new TaskList();
        }
        tasks = loaded;
    }

    // Returns the welcome message shown when app starts
    public String getWelcomeMessage() {
        return "Hello! my name is WALLE\n"
                + "What can I do for you?\n"
                + "If you are unfamiliar you can type 'help' to see all supported commands";
    }

    // Main API for GUI: give user input, get response text
    public String getResponse(String input) {
        try {
            if (Parser.isBye(input)) {
                return "Goodbye!";
            }

            if (Parser.isHelp(input)) {
                return "Here are the currently supported commands:\n"
                        + "list\n"
                        + "todo <description>\n"
                        + "deadline <description> /by <yyyy-MM-dd HHmm>\n"
                        + "event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n"
                        + "mark <task number>\n"
                        + "unmark <task number>\n"
                        + "find <keyword>\n"
                        + "delete <task number>\n"
                        + "bye";
            }

            if (Parser.isList(input)) {
                return tasks.toDisplayString();
            }

            if (Parser.isFind(input)) {
                String keyword = Parser.parseFindKeyword(input);
                return tasks.findToDisplayString(keyword);
            }

            if (Parser.isDelete(input)) {
                int idx = Parser.parseDeleteIndex(input, tasks.size());
                Task removed = tasks.delete(idx);
                storage.save(tasks.getTasks());
                return "Noted. I've removed this task:\n  " + removed
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            }

            if (Parser.isMark(input)) {
                int idx = Parser.parseMarkIndex(input, tasks.size());
                Task t = tasks.mark(idx);
                storage.save(tasks.getTasks());
                return "Nice! I've marked this task as done:\n  " + t;
            }

            if (Parser.isUnmark(input)) {
                int idx = Parser.parseUnmarkIndex(input, tasks.size());
                Task t = tasks.unmark(idx);
                storage.save(tasks.getTasks());
                return "OK, I've marked this task as not done yet:\n  " + t;
            }

            if (Parser.isTodo(input)) {
                String desc = Parser.parseTodoDescription(input);
                Task t = tasks.addTodo(desc);
                storage.save(tasks.getTasks());
                return "Got it. I've added this task:\n  " + t
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            }

            if (Parser.isDeadline(input)) {
                Deadline d = Parser.parseDeadline(input);
                tasks.add(d);
                storage.save(tasks.getTasks());
                return "Got it. I've added this task:\n  " + d
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            }

            if (Parser.isEvent(input)) {
                Event e = Parser.parseEvent(input);
                tasks.add(e);
                storage.save(tasks.getTasks());
                return "Got it. I've added this task:\n  " + e
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            }

            throw new WAllEException("I don't recognise that command. Type 'help' to see commands.");

        } catch (WAllEException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Oops — something went wrong: " + e.getMessage();
        }
    }

    // GUI uses this to know if should exit */
    public boolean isExitCommand(String input) {
        return Parser.isBye(input);
    }
}

