public class Task {

    private final String des;
    // Used enum instead of boolean
    private Status status;

    // des stands for description
    public Task(String des) {
        this.des = des;
        this.status = Status.NOT_DONE;
    }

    // When a task is done mark as true
    public void Done() {
        status = Status.DONE;
    }

    // when a task is not done mark as false
    public void Undone() {
        status = Status.NOT_DONE;
    }


    public boolean isDone() {
        return status == Status.DONE;
    }

    @Override
    public String toString() {
        String box = (status == Status.DONE) ? "[X] " : "[ ] ";
        return box + des;
    }

}
