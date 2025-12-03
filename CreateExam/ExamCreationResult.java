public class ExamCreationResult {
    private boolean success;
    private String message;
    private Exam exam;
    
    public ExamCreationResult(boolean success, String message, Exam exam) {
        this.success = success;
        this.message = message;
        this.exam = exam;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Exam getExam() {
        return exam;
    }
}
