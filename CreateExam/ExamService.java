public class ExamService {
    public ExamCreationResult createExam(String title, String instructions) {
        // Validation
        if (title == null || title.trim().isEmpty()) {
            return new ExamCreationResult(false, "Error: Exam title cannot be empty", null);
        }
        
        // Create exam
        Exam newExam = new Exam(title.trim(), instructions.trim());
        examList.add(newExam);
        
        return new ExamCreationResult(true, "Exam created successfully", newExam);
    }
}
