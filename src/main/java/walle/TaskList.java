package walle;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskList() {}

    public TaskList(ArrayList<Task> loaded) {
        tasks.addAll(loaded);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void add(Task t) {
        assert t != null : "cannot add null task";
        tasks.add(t);
    }

    public Task addTodo(String desc) {
        assert desc != null : "todo description should not be null";
        assert !desc.isBlank() : "todo description should not be blank";
        Task t = new Todo(desc);
        tasks.add(t);
        return t;
    }

    public Task delete(int idx1Based) {
        assert idx1Based >= 1 && idx1Based <= tasks.size() : "delete index out of bounds";
        return tasks.remove(idx1Based - 1);
    }

    public Task mark(int idx1Based) {
        assert idx1Based >= 1 && idx1Based <= tasks.size() : "mark index out of bounds";
        Task t = tasks.get(idx1Based - 1);
        t.Done();
        return t;
    }

    public Task unmark(int idx1Based) {
        assert idx1Based >= 1 && idx1Based <= tasks.size() : "unmark index out of bounds";
        Task t = tasks.get(idx1Based - 1);
        t.Undone();
        return t;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }


    public String toDisplayString() {
        if (tasks.isEmpty()) {
            return "(You have no task available)";
        }
        StringBuilder sb = new StringBuilder("These are all your tasks:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    public String findToDisplayString(String keyword) {
        assert keyword != null : "keyword should not be null";
        assert !keyword.isBlank() : "keyword should not be blank";
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).toString().toLowerCase().contains(keyword.toLowerCase())) {
                sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
                found = true;
            }
        }
        if (!found) {
            return "Here are the matching tasks in your list:\n(no matching tasks found)";
        }
        return sb.toString().trim();
    }
}
