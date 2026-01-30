package walle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    void newlyCreatedTask_notDone() {
        Task t = new Task("read book");
        assertFalse(t.isDone());
        assertTrue(t.toString().startsWith("[ ]"));
    }

    @Test
    void doneAndUndone_updatesStatus() {
        Task t = new Task("read book");
        t.Done();
        assertTrue(t.isDone());
        assertTrue(t.toString().startsWith("[X]"));

        t.Undone();
        assertFalse(t.isDone());
        assertTrue(t.toString().startsWith("[ ]"));
    }
}
