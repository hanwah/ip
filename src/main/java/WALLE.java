import java.util.Scanner;
import java.util.ArrayList;

//Read/write files
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

// Localdate
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



class WALLE {

    // Max number of tasks to store
    private static final int MAX_TASKS = 100;
    private static final String LINE = "____________________________________________________________";



    public static void main(String[] args) {
        Ui ui = new Ui();

        // Stores all task (use ArrayList so delete is easy)
        ArrayList<Task> tasks = new ArrayList<>();

        // Loads existing saved task from data/walle.txt





        // Greeting
        ui.showWelcome();

        // Loop for chatbot, to exit user must input "bye"
        while (true) {
            String input = ui.readCommand();

            try {
                // Code to exit
                // EqualsIgnoreCase allow the code to work if user decides to use capital letters
                if (input.equalsIgnoreCase("bye")) {
                    ui.showGoodbye();
                    break;
                }

                // Command to show available commands to user
                // This helps user understand what commands are supported
                if (input.equalsIgnoreCase("help")) {
                    ui.showLine();
                    ui.showHelp();
                    ui.showLine();
                    continue;
                }

                // Shows all task to user
                // Use Array function
                if (input.equals("list")) {
                    System.out.println(LINE);
                    ui.showMessage("These are all your task:");

                    // If list is empty
                    if (tasks.isEmpty()) {
                        ui.showMessage("  (You have no task available)");
                    } else {
                        // Loops through the array for all available task
                        for (int i = 0; i < tasks.size(); i++) {
                           ui.showMessage((i + 1) + "." + tasks.get(i));
                        }
                    }

                    System.out.println(LINE);
                    continue;
                }

                // Command to find tasks by keyword
                // Format: find <keyword>
                if (input.toLowerCase().startsWith("find ")) {
                    String keyword = input.substring(5).trim();

                    // If keyword is empty, show error
                    if (keyword.isEmpty()) {
                        throw new WAllEException("Oops — please provide a keyword to find. (e.g., find meeting)");
                    }

                    System.out.println(LINE);
                    System.out.println("Here are the matching tasks in your list:");

                    boolean found = false;
                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).toString().toLowerCase().contains(keyword.toLowerCase())) {
                            System.out.println((i + 1) + "." + tasks.get(i));
                            found = true;
                        }
                    }

                    // If nothing is found
                    if (!found) {
                        System.out.println("  (no matching tasks found)");
                    }

                    System.out.println(LINE);
                    continue;
                }

                // Delete task from the list
                // Format: delete <task number>
                if (input.toLowerCase().startsWith("delete ")) {
                    int idx = parseIndex(input.substring(7).trim(), "delete", tasks.size());

                    // Remove task and shift list automatically
                    Task removed = tasks.remove(idx - 1);
                    saveTasks(tasks);

                    System.out.println(LINE);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removed);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LINE);
                    continue;
                }




                // Code to mark task as done
                if (input.toLowerCase().startsWith("mark ")) {
                    int idx = parseIndex(input.substring(5).trim(), "mark", tasks.size());

                    tasks.get(idx - 1).Done();
                    saveTasks(tasks);

                    System.out.println(LINE);
                    System.out.println("Nice! The task you have selected is now mark as done:");
                    System.out.println("  " + tasks.get(idx - 1));
                    System.out.println(LINE);
                    continue;
                }



                // Code to mark task as undone
                if (input.toLowerCase().startsWith("unmark ")) {
                    int idx = parseIndex(input.substring(7).trim(), "unmark", tasks.size());

                    tasks.get(idx - 1).Undone();
                    saveTasks(tasks);

                    System.out.println(LINE);
                    System.out.println("OK, the task you have selected is now marked as undone:");
                    System.out.println("  " + tasks.get(idx - 1));
                    System.out.println(LINE);
                    continue;
                }

                // Creates todo task
                // If user types "todo" with no description, it will result in an error
                if (input.trim().equals("todo")) {
                    throw new WAllEException("Oops — the description of a todo cannot be empty.");
                }

                if (input.startsWith("todo ")) {
                    String desc = input.substring(5).trim();

                    // If user types "todo    " (only spaces), it is an error
                    if (desc.isEmpty()) {
                        throw new WAllEException("Oops — the description of a todo cannot be empty.");
                    }

                    // If task list is full, show error
                    if (tasks.size() >= MAX_TASKS) {
                        throw new WAllEException("Oops — your task list is full. (max " + MAX_TASKS + " tasks)");
                    }

                    Task t = new Todo(desc);
                    tasks.add(t);
                    saveTasks(tasks);
                    printAdded(t, tasks.size());
                    continue;
                }

                // creates a deadline task
                if (input.startsWith("deadline ")) {
                    // If task list is full, show error
                    if (tasks.size() >= MAX_TASKS) {
                        throw new WAllEException("Oops — your task list is full. (max " + MAX_TASKS + " tasks)");
                    }

                    Task t = parseDeadline(input);
                    tasks.add(t);
                    saveTasks(tasks);
                    printAdded(t, tasks.size());
                    continue;
                }

                // Create an Event task
                if (input.startsWith("event ")) {
                    // If task list is full, show error
                    if (tasks.size() >= MAX_TASKS) {
                        throw new WAllEException("Oops — your task list is full. (max " + MAX_TASKS + " tasks)");
                    }

                    Task t = parseEvent(input);
                    tasks.add(t);
                    saveTasks(tasks);
                    printAdded(t, tasks.size());
                    continue;
                }

                // If command is unknown, treat it as an error
                // Provide user with the commands
                throw new WAllEException(
                        "I don't recognise that command.\n" +
                                "Try: help, list, todo <desc>, deadline <desc> /by <when>, " +
                                "event <desc> /from <start> /to <end>, mark <num>, unmark <num>, bye"
                );

            } catch (WAllEException e) {
                System.out.println(LINE);
                System.out.println(e.getMessage());
                System.out.println(LINE);

            } catch (Exception e) {
                // Safety net for unexpected errors
                System.out.println(LINE);
                System.out.println("Oops — something went wrong. " + e.getMessage());
                System.out.println(LINE);
            }
        }

        in.close();
    }

    // Helper functions

    // Prints when a task is added
    private static void printAdded(Task t, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }


    // AI attribution, used ChatGPT to brainstorm error cases (missing /from, /to, /by, invalid indexes).
    // index parsing  exception cases inspired by ChatGPT
    private static int parseIndex(String s, String commandName, int taskCount) throws WAllEException {
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

    // Deadline helper function exception cases inspired by ChatGPT
    private static Deadline parseDeadline(String input) throws WAllEException {
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
            // deadline now accepts date+time
            LocalDateTime by = LocalDateTime.parse(byStr, EVENT_IN_FMT);
            return new Deadline(desc, by);
        } catch (DateTimeParseException e) {
            throw new WAllEException("Oops — use yyyy-MM-dd HHmm (e.g., 2019-10-15 1800).");
        }
    }


    // Event helper function exception cases inspired by ChatGPT
    private static Event parseEvent(String input) throws WAllEException {
        // Format: event <desc> /from <from> /to <to>, use keyword from/to to seperate date/time
        int fromPos = input.indexOf(" /from ");
        int toPos = input.indexOf(" /to ");

        // If user did not include /from or /to properly
        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new WAllEException("Oops — event format should be:\n  event <description> /from <start> /to <end>");
        }

        String desc = input.substring("event ".length(), fromPos).trim();
        String fromStr = input.substring(fromPos + " /from ".length(), toPos).trim();
        String toStr = input.substring(toPos + " /to ".length()).trim();

        // If description is empty
        if (desc.isEmpty()) {
            throw new WAllEException("Oops — the description of an event cannot be empty.");
        }

        // If from is empty
        if (fromStr.isEmpty()) {
            throw new WAllEException("Oops — the /from part of an event cannot be empty.");
        }

        // If to is empty
        if (toStr.isEmpty()) {
            throw new WAllEException("Oops — the /to part of an event cannot be empty.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(fromStr, EVENT_IN_FMT);
            LocalDateTime to = LocalDateTime.parse(toStr, EVENT_IN_FMT);

            if (to.isBefore(from)) {
                throw new WAllEException("Oops — event end time must be after start time.");
            }

            return new Event(desc, from, to);

        } catch (DateTimeParseException e) {
            throw new WAllEException("Oops — use yyyy-MM-dd HHmm (e.g., 2019-10-15 1800).");
        }

    }






}
