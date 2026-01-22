import java.util.Scanner;

class WAllE {

    // Max number of tasks to store
    private static final int MAX_TASKS = 100;
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Stores all task
        Task[] tasks = new Task[MAX_TASKS];

        // Tracks current number of task stored
        int taskCount = 0;

        // Greeting
        System.out.println("Hello! my name is WAllE");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        // Loop for chatbot, to exit user must input "bye"
        while (true) {
            String input = in.nextLine();

            try {
                // Code to exit
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println(LINE);
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(LINE);
                    break;
                }

                // Command to show available commands to user
                // This helps user understand what commands are supported
                if (input.equalsIgnoreCase("help")) {
                    System.out.println(LINE);
                    System.out.println("Here are the commands you can use:");
                    System.out.println("  list");
                    System.out.println("  todo <description>");
                    System.out.println("  deadline <description> /by <when>");
                    System.out.println("  event <description> /from <start> /to <end>");
                    System.out.println("  mark <task number>");
                    System.out.println("  unmark <task number>");
                    System.out.println("  bye");
                    System.out.println(LINE);
                    continue;
                }

                // Shows all task to user
                if (input.equals("list")) {
                    System.out.println(LINE);
                    System.out.println("Here are the tasks in your list:");

                    // If list is empty
                    if (taskCount == 0) {
                        System.out.println("  (no tasks yet)");
                    } else {
                        for (int i = 0; i < taskCount; i++) {
                            System.out.println((i + 1) + "." + tasks[i]);
                        }
                    }

                    System.out.println(LINE);
                    continue;
                }



                // Code to mark task as done
                if (input.toLowerCase().startsWith("mark ")) {
                    int idx = parseIndex(input.substring(5).trim(), "mark", taskCount);

                    tasks[idx - 1].Done();

                    System.out.println(LINE);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[idx - 1]);
                    System.out.println(LINE);
                    continue;
                }

                // Code to mark task as undone
                if (input.toLowerCase().startsWith("unmark ")) {
                    int idx = parseIndex(input.substring(7).trim(), "unmark", taskCount);

                    tasks[idx - 1].Undone();

                    System.out.println(LINE);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[idx - 1]);
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
                    if (taskCount >= MAX_TASKS) {
                        throw new WAllEException("Oops — your task list is full. (max " + MAX_TASKS + " tasks)");
                    }

                    Task t = new Todo(desc);
                    tasks[taskCount++] = t;
                    printAdded(t, taskCount);
                    continue;
                }

                // creates a deadline task
                if (input.startsWith("deadline ")) {
                    // If task list is full, show error
                    if (taskCount >= MAX_TASKS) {
                        throw new WAllEException("Oops — your task list is full. (max " + MAX_TASKS + " tasks)");
                    }

                    Task t = parseDeadline(input);
                    tasks[taskCount++] = t;
                    printAdded(t, taskCount);
                    continue;
                }

                // Create an Event task
                if (input.startsWith("event ")) {
                    // If task list is full, show error
                    if (taskCount >= MAX_TASKS) {
                        throw new WAllEException("Oops — your task list is full. (max " + MAX_TASKS + " tasks)");
                    }

                    Task t = parseEvent(input);
                    tasks[taskCount++] = t;
                    printAdded(t, taskCount);
                    continue;
                }

                // If command is unknown, treat it as an error
                // Provide user with the commands
                throw new WAllEException(
                        "Oops — I don't recognise that command.\n" +
                                "Try: help, list, todo <desc>, deadline <desc> /by <when>, " +
                                "event <desc> /from <start> /to <end>, mark <num>, unmark <num>, bye"
                );

            } catch (WAllEException e) {
                // Handles WAllE-specific errors
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

    // Parses index number for commands that need a task number + checks bounds
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

    private static Deadline parseDeadline(String input) throws WAllEException {
        // Format : deadline <desc> /by <by> , use keyword by to seperate task and date
        int byPos = input.indexOf(" /by ");

        // If user did not include /by
        if (byPos == -1) {
            throw new WAllEException("Oops — deadline format should be:\n  deadline <description> /by <when>");
        }

        String desc = input.substring("deadline ".length(), byPos).trim();
        String by = input.substring(byPos + " /by ".length()).trim();

        // If description is empty
        if (desc.isEmpty()) {
            throw new WAllEException("Oops — the description of a deadline cannot be empty.");
        }

        // If by is empty
        if (by.isEmpty()) {
            throw new WAllEException("Oops — the /by part of a deadline cannot be empty.");
        }

        return new Deadline(desc, by);
    }

    private static Event parseEvent(String input) throws WAllEException {
        // Format: event <desc> /from <from> /to <to>, use keyword from/to to seperate date/time
        int fromPos = input.indexOf(" /from ");
        int toPos = input.indexOf(" /to ");

        // If user did not include /from or /to properly
        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new WAllEException("Oops — event format should be:\n  event <description> /from <start> /to <end>");
        }

        String desc = input.substring("event ".length(), fromPos).trim();
        String from = input.substring(fromPos + " /from ".length(), toPos).trim();
        String to = input.substring(toPos + " /to ".length()).trim();

        // If description is empty
        if (desc.isEmpty()) {
            throw new WAllEException("Oops — the description of an event cannot be empty.");
        }

        // If from is empty
        if (from.isEmpty()) {
            throw new WAllEException("Oops — the /from part of an event cannot be empty.");
        }

        // If to is empty
        if (to.isEmpty()) {
            throw new WAllEException("Oops — the /to part of an event cannot be empty.");
        }

        return new Event(desc, from, to);
    }
}
