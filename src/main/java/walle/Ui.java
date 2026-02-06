package walle;

import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner in = new Scanner(System.in);

    public void showWelcome() {
        System.out.println("Hello! my name is WALLE");
        System.out.println("What can I do for you? If you are unfamiliar you can type 'help' to see all supported commands");
        System.out.println(LINE);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public String readCommand() {
        return in.nextLine();
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void showError(String msg) {
        showLine();
        System.out.println(msg);
        showLine();
    }

    public void showLoadingError() {
        showLine();
        System.out.println("Warning: save file is corrupted/unreadable, starting with empty list.");
        showLine();
    }

    public void showGoodbye() {
        showLine();
        System.out.println("Goodbye!");
        showLine();
    }

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

    // Shows task list
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

    // Shows tasks that matched keyword
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

    public void showTaskAdded(Task t, int taskCount) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    public void showTaskDeleted(Task removed, int taskCount) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    public void showTaskMarked(Task t) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + t);
        showLine();
    }

    public void showTaskUnmarked(Task t) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + t);
        showLine();
    }

    public void close() {
        in.close();
    }
}
