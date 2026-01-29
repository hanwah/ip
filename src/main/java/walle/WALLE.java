package walle;

import java.util.ArrayList;

public class WALLE {
    private static final String SAVE_FILE = "data/walle.txt";

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(SAVE_FILE);

        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (WAllEException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                if (Parser.isBye(input)) {
                    ui.showGoodbye();
                    break;
                }

                if (Parser.isHelp(input)) {
                    ui.showLine();
                    ui.showHelp();
                    ui.showLine();
                    continue;
                }

                if (Parser.isList(input)) {
                    ui.showTaskList(tasks);
                    continue;
                }

                if (Parser.isFind(input)) {
                    String keyword = Parser.parseFindKeyword(input);
                    ArrayList<Task> matches = tasks.find(keyword);
                    ui.showFindResults(matches);
                    continue;
                }

                if (Parser.isTodo(input)) {
                    String desc = Parser.parseTodoDescription(input);
                    Task t = new Todo(desc);
                    tasks.add(t);
                    storage.save(tasks.asArrayList());
                    ui.showAdded(t, tasks.size());
                    continue;
                }

                if (Parser.isDeadline(input)) {
                    Task t = Parser.parseDeadline(input);
                    tasks.add(t);
                    storage.save(tasks.asArrayList());
                    ui.showAdded(t, tasks.size());
                    continue;
                }

                if (Parser.isEvent(input)) {
                    Task t = Parser.parseEvent(input);
                    tasks.add(t);
                    storage.save(tasks.asArrayList());
                    ui.showAdded(t, tasks.size());
                    continue;
                }

                if (Parser.isMark(input)) {
                    int idx = Parser.parseMarkIndex(input, tasks.size());
                    tasks.mark(idx);
                    storage.save(tasks.asArrayList());
                    ui.showMarked(tasks.get(idx));
                    continue;
                }

                if (Parser.isUnmark(input)) {
                    int idx = Parser.parseUnmarkIndex(input, tasks.size());
                    tasks.unmark(idx);
                    storage.save(tasks.asArrayList());
                    ui.showUnmarked(tasks.get(idx));
                    continue;
                }

                if (Parser.isDelete(input)) {
                    int idx = Parser.parseDeleteIndex(input, tasks.size());
                    Task removed = tasks.remove(idx);
                    storage.save(tasks.asArrayList());
                    ui.showDeleted(removed, tasks.size());
                    continue;
                }

                throw new WAllEException("I don't recognise that command. Type 'help' to see supported commands.");

            } catch (WAllEException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}

