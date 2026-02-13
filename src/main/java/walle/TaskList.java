package walle;

import java.util.ArrayList;

/**
 * Stores and manages the collection of tasks in memory.
 * Provides operations to add, remove, update, and search tasks.
 */
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
        tasks.add(t);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public Task addTodo(String desc) {
        Task t = new Todo(desc);
        tasks.add(t);
        return t;
    }

    /**
     * Deletes a task by its 1-based index.
     *
     * @param index 1-based index of the task to delete.
     * @return The deleted task.
     * @throws WAllEException If the index is invalid.
     */
    public Task delete(int idx1Based) {
        return tasks.remove(idx1Based - 1);
    }

    /**
     * Marks a task as done by its 1-based index.
     *
     * @param index 1-based index of the task to mark.
     * @return The updated task.
     * @throws WAllEException If the index is invalid.
     */

    public Task mark(int idx1Based) {
        Task t = tasks.get(idx1Based - 1);
        t.Done();
        return t;
    }

    /**
     * Unmarks a task as done by its 1-based index.
     *
     * @param index 1-based index of the task to mark.
     * @return The updated task.
     * @throws WAllEException If the index is invalid.
     */
    public Task unmark(int idx1Based) {
        Task t = tasks.get(idx1Based - 1);
        t.Undone();
        return t;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a formatted string representing all tasks for display.
     *
     * @return Display string of all tasks.
     */

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

    /**
     * Finds tasks whose string representation contains the given keyword.
     *
     * @param keyword Keyword to search for.
     * @return Formatted string containing matching tasks.
     */
    public String findToDisplayString(String keyword) {
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        boolean isFound = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).toString().toLowerCase().contains(keyword.toLowerCase())) {
                sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
                isFound = true;
            }
        }
        if (!isFound) {
            return "Here are the matching tasks in your list:\n(no matching tasks found)";
        }
        return sb.toString().trim();
    }
}
