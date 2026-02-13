package walle;

/**
 * Represents a simple todo task with only a description.
 */

public class Todo extends Task {

    /**
     * Creates a todo task.
     *
     * @param description Description of the todo.
     */

    public Todo(String des) {
        super(des);
    }

    // Display the Tdodo task
    @Override
    public String toString() {
        return  "[T]" + super.toString();
    }
}

