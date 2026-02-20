package walle;

import java.util.ArrayList;

/**
 * Stores and manages the collection of tasks in memory.
 * Provides operations to add, remove, update, and search tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Creates an empty task list.
     */
    public TaskList() {}

    /**
     * Creates a task list initialized with tasks loaded from storage.
     *
     * @param loaded
     */
    public TaskList(ArrayList<Task> loaded) {
        tasks.addAll(loaded);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return Backing task list.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }


    /**
     * Adds a task to the list.
     *
     * @param t
     */
    public void add(Task t) {
        assert t != null : "cannot add null task";
        tasks.add(t);
    }

    /**
     * Adds a task to the list.
     *
     * @param desc Task to add.
     */
    public Task addTodo(String desc) {
        assert desc != null : "todo description should not be null";
        assert !desc.isBlank() : "todo description should not be blank";
        Task t = new Todo(desc);
        tasks.add(t);
        return t;
    }

    /**
     * Deletes a task by its 1-based index.
     *
     * @param idx1Based 1-based index of the task to delete.
     * @return The deleted task.
     * @throws WalleException If the index is invalid.
     */
    public Task delete(int idx1Based) {
        assert idx1Based >= 1 && idx1Based <= tasks.size() : "delete index out of bounds";
        return tasks.remove(idx1Based - 1);
    }

    /**
     * Marks a task as done by its 1-based index.
     *
     * @param idx1Based 1-based index of the task to mark.
     * @return The updated task.
     * @throws WalleException If the index is invalid.
     */
    public Task mark(int idx1Based) {
        assert idx1Based >= 1 && idx1Based <= tasks.size() : "mark index out of bounds";
        Task t = tasks.get(idx1Based - 1);
        t.markDone();
        return t;
    }

    /**
     * Unmarks a task as done by its 1-based index.
     *
     * @param idx1Based 1-based index of the task to mark.
     * @return The updated task.
     * @throws WalleException If the index is invalid.
     */
    public Task unmark(int idx1Based) {
        assert idx1Based >= 1 && idx1Based <= tasks.size() : "unmark index out of bounds";
        Task t = tasks.get(idx1Based - 1);
        t.markUndone();
        return t;
    }

    /**
     * Returns whether the task list is empty.
     *
     * @return True if empty; false otherwise.
     */
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
            return "(You have no task available now)";
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
        assert keyword != null : "keyword should not be null";
        assert !keyword.isBlank() : "keyword should not be blank";
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

    /**
     * Inserts a task at the given 1-based index.
     *
     * @param idx1Based 1-based index at which to insert.
     * @param task Task to insert.
     */
    public void addAt(int idx1Based, Task task) {
        tasks.add(idx1Based - 1, task);
    }

    /**
     * Returns the task at the given 1-based index.
     *
     * @param idx1Based 1-based index.
     * @return Task at the given index.
     */
    public Task get(int idx1Based) {
        return tasks.get(idx1Based - 1);
    }

}
