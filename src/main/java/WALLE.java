import java.util.Scanner;

class  WAllE {

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

            // Code to exit
            if (input.equals("bye") || input.equals("Bye")) {
                System.out.println(LINE);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            // Shows all task to user
            if (input.equals("list")) {
                System.out.println(LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            // Code to mark task as done
            if (input.startsWith("mark ") || input.startsWith("Mark ")) {
                int idx = Integer.parseInt(input.substring(5).trim());
                tasks[idx - 1].Undone();

                System.out.println(LINE);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[idx - 1]);
                System.out.println(LINE);
                continue;
            }

            // Code to mark task as undone
            if (input.startsWith("unmark ") || input.startsWith("Unmark ")) {
                int idx = Integer.parseInt(input.substring(7).trim());
                tasks[idx - 1].Done();

                System.out.println(LINE);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[idx - 1]);
                System.out.println(LINE);
                continue;
            }

            // Creates todo task
            if (input.startsWith("todo ")) {
                String desc = input.substring(5);
                Task t = new Todo(desc);
                tasks[taskCount++] = t;
                printAdded(t, taskCount);
                continue;
            }

            // creates a deadline task
            if (input.startsWith("deadline ")) {
                Task t = parseDeadline(input);
                tasks[taskCount++] = t;
                printAdded(t, taskCount);
                continue;
            }

            //Create an Event task
            if (input.startsWith("event ")) {
                Task t = parseEvent(input);
                tasks[taskCount++] = t;
                printAdded(t, taskCount);
                continue;
            }

            // If not specified treat it as a normal todo task
            Task t = new Todo(input);
            tasks[taskCount++] = t;
            printAdded(t, taskCount);
        }

        in.close();
    }

    // Helper functions
    private static void printAdded(Task t, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    private static Deadline parseDeadline(String input) {
        // Format : deadline <desc> /by <by> , use keyword by to seperate task and date
        int byPos = input.indexOf(" /by ");
        String desc = input.substring("deadline ".length(), byPos);
        String by = input.substring(byPos + " /by ".length());
        return new Deadline(desc, by);
    }

    private static Event parseEvent(String input) {
        // Format: event <desc> /from <from> /to <to>, use keyword by to seperate task and date
        int fromPos = input.indexOf(" /from ");
        int toPos = input.indexOf(" /to ");

        String desc = input.substring("event ".length(), fromPos);
        String from = input.substring(fromPos + " /from ".length(), toPos);
        String to = input.substring(toPos + " /to ".length());

        return new Event(desc, from, to);
    }
}
