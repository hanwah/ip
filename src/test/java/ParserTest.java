package walle;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    void parseDeadline_validInput_success() throws Exception {
        Deadline d = Parser.parseDeadline("deadline return book /by 2019-10-15 1800");
        assertEquals("return book", d.getDescription());
        assertEquals(LocalDateTime.of(2019, 10, 15, 18, 0), d.getBy());
    }

    @Test
    void parseDeadline_missingBy_throws() {
        assertThrows(WalleException.class, () ->
                Parser.parseDeadline("deadline return book 2019-10-15 1800"));
    }

    @Test
    void parseDeadline_badDatetime_throws() {
        assertThrows(WalleException.class, () ->
                Parser.parseDeadline("deadline return book /by 2019/10/15 1800"));
    }

    @Test
    void parseEvent_validInput_success() throws Exception {
        Event e = Parser.parseEvent("event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        assertEquals("project meeting", e.getDescription());
        assertEquals(LocalDateTime.of(2019, 10, 15, 14, 0), e.getFrom());
        assertEquals(LocalDateTime.of(2019, 10, 15, 16, 0), e.getTo());
    }

    @Test
    void parseEvent_endBeforeStart_throws() {
        assertThrows(WalleException.class, () ->
                Parser.parseEvent("event meeting /from 2019-10-15 1600 /to 2019-10-15 1400"));
    }

    @Test
    void parseTodoDescription_empty_throws() {
        assertThrows(WalleException.class, () ->
                Parser.parseTodoDescription("todo"));
    }

    @Test
    void parseMarkIndex_valid_success() throws Exception {
        assertEquals(2, Parser.parseMarkIndex("mark 2", 5));
    }

    @Test
    void parseMarkIndex_outOfRange_throws() {
        assertThrows(WalleException.class, () ->
                Parser.parseMarkIndex("mark 9", 3));
    }
}
