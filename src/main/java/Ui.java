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
        System.out.println("  bye");
        System.out.println("  find <keyword>");
        System.out.println("  delete <task number>");
    }
}
