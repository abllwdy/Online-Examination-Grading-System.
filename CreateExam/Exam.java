public class Exam {
    private int examId;
    private String title;
    private String instructions;
    private LocalDateTime createdAt;
    private List<Question> questions;

    // Constructor for new exams (service assigns ID)
    public Exam(int examId, String title, String instructions) {
        this.examId = examId;
        this.title = title;
        this.instructions = instructions;
        this.createdAt = LocalDateTime.now();
        this.questions = new ArrayList<>();
    }
    
    // Constructor for loading existing exams (e.g., from database)
    public Exam(int examId, String title, String instructions, LocalDateTime createdAt) {
        this.examId = examId;
        this.title = title;
        this.instructions = instructions;
        this.createdAt = createdAt;
        this.questions = new ArrayList<>();
    }
    
    // Getters/setters remain the same
}
