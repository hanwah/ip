package walle;

public class Todo extends Task {

    public Todo(String des) {
        super(des);
    }

    // Display the Tdodo task
    @Override
    public String toString() {
        return  "[T]" + super.toString();
    }
}

