package walle;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles user-facing messages and formatting for console output.
 * Responsible for displaying prompts, responses, and separators.
 */

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner in = new Scanner(System.in);

    /**
     * Returns the welcome message shown when the program starts.
     */
    public void showWelcome() {
        System.out.println("Hello! my name is Walle");
        System.out.println("What can I do for you? "
                + "If you are unfamiliar you can type 'help' "
                + "to see all supported commands");
        System.out.println(LINE);
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Reads the next line of user input.
     *
     * @return Raw user input.
     */
    public String readCommand() {
        return in.nextLine();
    }

    /**
     * Displays a normal message to the user.
     *
     * @param msg Message to show.
     */
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg Error message.
     */
    public void showError(String msg) {
        showLine();
        System.out.println(msg);
        showLine();
    }

    /**
     * Displays a standard loading error message.
     */
    public void showLoadingError() {
        showLine();
        System.out.println("Warning: save file is corrupted/unreadable, starting with empty list.");
        showLine();
    }

    /**
     * Returns the goodbye message shown when the program exits.
     */
    public void showGoodbye() {
        showLine();
        System.out.println("Goodbye!");
        showLine();
    }

    /**
     * Displays a list of all supported commands and their formats.
     */
    public void showHelp() {
        System.out.println("Here are the currently supported commands that you can use =>:");
        System.out.println("  list");
        System.out.println("  todo <description>");
        System.out.println("  deadline <description> /by <yyyy-MM-dd HHmm>");
        System.out.println("  event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        System.out.println("  mark <task number>");
        System.out.println("  unmark <task number>");
        System.out.println("  delete <task number>");
        System.out.println("  find <keyword>");
        System.out.println("  bye");
    }


    /**
     * Displays all tasks currently stored in the task list.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks) {
        showLine();
        System.out.println("These are all your tasks:");

        if (tasks.size() == 0) {
            System.out.println("  (You have no task available)");
        } else {
            System.out.println(tasks.getTasks());
        }

        showLine();
    }


    /**
     * Displays the tasks that match a search keyword.
     *
     * @param matches List of tasks that match the search criteria.
     */
    public void showFindResults(ArrayList<Task> matches) {
        showLine();
        System.out.println("Here are the matching tasks in your list:");

        if (matches.isEmpty()) {
            System.out.println("  (no matching tasks found)");
        } else {
            for (int i = 0; i < matches.size(); i++) {
                System.out.println((i + 1) + "." + matches.get(i));
            }
        }

        showLine();
    }

    /**
     * Displays a confirmation message after a task is added.
     *
     * @param t         The task that was added.
     * @param taskCount The total number of tasks after addition.
     */
    public void showTaskAdded(Task t, int taskCount) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a confirmation message after a task is deleted.
     *
     * @param removed   The task that was removed.
     * @param taskCount The total number of tasks after deletion.
     */
    public void showTaskDeleted(Task removed, int taskCount) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a confirmation message after a task is marked as done.
     *
     * @param t The task that was marked as done.
     */
    public void showTaskMarked(Task t) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + t);
        showLine();
    }

    /**
     * Displays a confirmation message after a task is marked as not done.
     *
     * @param t The task that was unmarked.
     */
    public void showTaskUnmarked(Task t) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + t);
        showLine();
    }

    /**
     * Closes the input scanner and releases associated resources.
     */
    public void close() {
        in.close();
    }
}
