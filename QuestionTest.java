import model.Question;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*; // Import the magic testing tools

class QuestionTest {

    @Test
    void testCreateValidQuestion() {
        // 1. SETUP: Create the inputs
        List<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        options.add("C");
        options.add("D");

        // 2. ACTION: Try to make the question (US08)
        Question q = new Question("Is this working?", options, 1);

        // 3. ASSERTION: The computer checks if it worked (Green Light condition)
        assertEquals("Is this working?", q.getQuestionText(), "Question text should match");
        assertTrue(q.isCorrect(1), "Option B (index 1) should be correct");
    }

    @Test
    void testInvalidOptionsCount() {
        // Negative Test: What happens if we only give 3 options?
        List<String> badOptions = new ArrayList<>();
        badOptions.add("A");
        badOptions.add("B");
        badOptions.add("C"); // Missing the 4th one!

        // This checks if the code properly crashes (Throws an error) as expected
        assertThrows(IllegalArgumentException.class, () -> {
            new Question("Bad Question", badOptions, 0);
        }, "Should throw error if options are not 4");
    }
}