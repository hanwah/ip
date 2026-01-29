package walle;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    // Creates an empty task list
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // Load existing task list from storage
    public TaskList(ArrayList<Task> initialTasks) {
        this.tasks = initialTasks == null ? new ArrayList<>() : initialTasks;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public int size() {
        return tasks.size();
    }


    public Task get(int index) throws WAllEException {
        int i = index - 1;
        if (i < 0 || i >= tasks.size()) {
            throw new WAllEException("Oops — task number " + index
                    + " is out of range. Use 'list' to see valid task numbers.");
        }
        return tasks.get(i);
    }

    // Add new task to the list
    public void add(Task t) throws WAllEException {
        if (tasks.size() >= 100) {
            throw new WAllEException("Oops — your task list is full. (max 100 tasks)");
        }
        tasks.add(t);
    }


    // Remove task from the list
    public Task remove(int index) throws WAllEException {
        Task t = get(index);
        tasks.remove(index - 1);
        return t;
    }

    // Marks a task as done
    public void mark(int index) throws WAllEException {
        get(index).Done();
    }

    // Unmarks a task
    public void unmark(int index) throws WAllEException {
        get(index).Undone();
    }


    // Finds a task which contain the keyword by looping through all existing task
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task t : tasks) {
            if (t.toString().toLowerCase().contains(keyword.toLowerCase())) {
                matches.add(t);
            }
        }
        return matches;
    }


    public ArrayList<Task> asArrayList() {
        return tasks;
    }
}
