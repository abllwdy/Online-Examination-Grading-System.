package model;

import java.util.List;

// This class is the blueprint for every single question in our exam.
// Think of this like a cookie cutter for creating questions.
public class Question {

    // ==========================
    // 1. THE DATA (Keep it private!)
    // ==========================
    // We keep these private so other parts of the app can't mess them up directly.
    private String questionText;       // The actual question string (e.g., "What is 1+1?")
    private List<String> options;      // A list of the 4 answer choices
    private int correctOptionIndex;    // Which number is the right answer? (0 for A, 1 for B, etc.)

    // ==========================
    // 2. THE SETUP (Constructor)
    // ==========================
    // This runs immediately when you type "new Question()".
    // It forces you to give us the data right away. No empty questions allowed.
    public Question(String questionText, List<String> options, int correctOptionIndex) {

        // SAFETY CHECK: Make sure they actually gave us 4 options.
        // If they gave 3 or 5, we crash the program here so we know there's a bug.
        if (options.size() != 4) {
            throw new IllegalArgumentException("Bro, you need exactly 4 options. Fix it.");
        }

        // SAFETY CHECK: Make sure the answer index makes sense (must be 0, 1, 2, or 3).
        if (correctOptionIndex < 0 || correctOptionIndex >= 4) {
            throw new IllegalArgumentException("Invalid answer index. It has to be 0-3.");
        }

        // If we passed the checks, save the data into the object.
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    // ==========================
    // 3. GETTERS (Read-Only Access)
    // ==========================
    // These let other files READ the info, but not change it.

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    // ==========================
    // 4. THE LOGIC
    // ==========================
    // This checks if the student's answer is right.
    // Pass in the number they clicked (selectedIndex). Returns true if they nailed it.
    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctOptionIndex;
    }
}