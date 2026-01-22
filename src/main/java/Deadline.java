public class Deadline extends Task {
    private final String date;

    public Deadline(String des, String date) {
        super(des);
        this.date = date;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + date + ")";
    }
}
