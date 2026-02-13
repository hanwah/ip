package walle;

import java.util.ArrayDeque;
import java.util.Deque;


public class WALLE {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();

    private interface UndoAction {
        String undo() throws WAllEException;
    }


    /**
     * Main chatbot logic controller.
     * Receives user input, coordinates parsing and task operations, and
     * returns the response string for the UI to display.
     */
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

    /**
     * Processes the user input and returns the chatbot response.
     *
     * @param input Raw user input.
     * @return Response text to be shown to the user.
     */

    public String getResponse(String input) {
        assert input != null : "UI should never pass null input";
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
                        + "bye\n"
                        + "undo";

            }

            if (Parser.isUndo(input)) {
                return undoLast();
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

                // record undo
                final int deletedIndex = idx;
                final Task deletedTask = removed;
                undoStack.push(() -> {
                    tasks.addAt(deletedIndex, deletedTask);
                    return "Undid delete. Restored this task:\n  " + deletedTask;
                });

                storage.save(tasks.getTasks());
                return "Noted. I've removed this task:\n  " + removed
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            }


            if (Parser.isMark(input)) {
                int idx = Parser.parseMarkIndex(input, tasks.size());
                Task t = tasks.mark(idx);

                final int markIndex = idx;
                undoStack.push(() -> {
                    Task undone = tasks.unmark(markIndex);
                    return "Undid mark. Task is not done:\n  " + undone;
                });

                storage.save(tasks.getTasks());
                return "Nice! I've marked this task as done:\n  " + t;
            }


            if (Parser.isUnmark(input)) {
                int idx = Parser.parseUnmarkIndex(input, tasks.size());
                Task t = tasks.unmark(idx);

                final int unmarkIndex = idx;
                undoStack.push(() -> {
                    Task redone = tasks.mark(unmarkIndex);
                    return "Undid unmark. Task is done:\n  " + redone;
                });

                storage.save(tasks.getTasks());
                return "OK, I've marked this task as not done yet:\n  " + t;
            }


            if (Parser.isTodo(input)) {
                String desc = Parser.parseTodoDescription(input);
                Task t = tasks.addTodo(desc);

                final int addedIndex = tasks.size(); // last
                undoStack.push(() -> {
                    Task removed = tasks.delete(addedIndex);
                    return "Undid add. Removed this task:\n  " + removed;
                });

                storage.save(tasks.getTasks());
                return "Got it. I've added this task:\n  " + t
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            }


            if (Parser.isDeadline(input)) {
                Deadline d = Parser.parseDeadline(input);
                tasks.add(d);

                final int addedIndex = tasks.size();
                undoStack.push(() -> {
                    Task removed = tasks.delete(addedIndex);
                    return "Undid add. Removed this task:\n  " + removed;
                });

                storage.save(tasks.getTasks());
                return "Got it. I've added this task:\n  " + d
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            }


            if (Parser.isEvent(input)) {
                Event e = Parser.parseEvent(input);
                tasks.add(e);

                final int addedIndex = tasks.size();
                undoStack.push(() -> {
                    Task removed = tasks.delete(addedIndex);
                    return "Undid add. Removed this task:\n  " + removed;
                });

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

    private String undoLast() throws WAllEException {
        if (undoStack.isEmpty()) {
            throw new WAllEException("Nothing to undo.");
        }

        UndoAction action = undoStack.pop();
        String msg = action.undo();
        storage.save(tasks.getTasks());
        return msg;
    }

    // GUI uses this to know if should exit */
    public boolean isExitCommand(String input) {
        return Parser.isBye(input);
    }
}

