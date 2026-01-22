public class Task {

    private final String des;
    private boolean isDone;

    // des stands for description
    public Task(String des) {
        this.des = des;
        this.isDone = false;
    }

    // When a task is done mark as true
    public void Done() {
        this.isDone = true;
    }

    // when a task is not done mark as false
    public void Undone() {
        this.isDone = false;

    }

    // String to print status
    @Override
    public String toString() {
        return (isDone? "[X]" : "[ ]") + des;
    }

}
