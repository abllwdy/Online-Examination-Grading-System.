package model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class StudentSession {
    public static final String STATUS_READY = "READY";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_SUBMITTED = "SUBMITTED";

    // forceMemoryError flag is REMOVED

    private Instant startTime;
    private String status;
    private Map<Integer, String> answers; // Stores {QuestionIndex : "A"}

    public StudentSession() {
        this.status = STATUS_READY;
        this.answers = new HashMap<>();
        this.startTime = null;
    }

    public void start() {
        if (this.status.equals(STATUS_READY)) {
            this.status = STATUS_IN_PROGRESS;
            this.startTime = Instant.now();
        } else {
            throw new IllegalStateException("Exam is already in progress or submitted.");
        }
    }

    // --- US06 LOGIC: SAVE ANSWER ---
    public void saveAnswer(int questionIndex, String answer) {
        // The forceMemoryError check is REMOVED
        answers.put(questionIndex, answer);
    }

    // --- GETTERS ---
    public String getAnswer(int questionIndex) {
        return answers.get(questionIndex);
    }

    public String getStatus() { return status; }
    public Instant getStartTime() { return startTime; }
    public Map<Integer, String> getAnswersMap() { return answers; }
}