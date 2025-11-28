import java.util.*;

// ==========================================
// 1. Enums & Models (The Data Structure)
// ==========================================

// Defines the specific states mentioned in the User Story
enum VisibilityStatus {
    HIDDEN,
    RELEASED
}

// Represents the Exam Entity
class Exam {
    private String examId;
    private String title;
    private VisibilityStatus visibilityStatus;

    public Exam(String examId, String title) {
        this.examId = examId;
        this.title = title;
        // Default to Hidden as per security best practices
        this.visibilityStatus = VisibilityStatus.HIDDEN; 
    }

    public String getExamId() { return examId; }
    public VisibilityStatus getVisibilityStatus() { return visibilityStatus; }
    public void setVisibilityStatus(VisibilityStatus visibilityStatus) { this.visibilityStatus = visibilityStatus; }
    public String getTitle() { return title; }
}

// Represents the Grade/Result
class ExamResult {
    private String studentId;
    private String examId;
    private double score;
    private String feedback;

    public ExamResult(String studentId, String examId, double score, String feedback) {
        this.studentId = studentId;
        this.examId = examId;
        this.score = score;
        this.feedback = feedback;
    }

    // --- FIX START: Added Getters to allow access ---
    public String getStudentId() { return studentId; }
    public String getExamId() { return examId; }
    // --- FIX END ---

    public double getScore() { return score; }
    public String getFeedback() { return feedback; }
    
    @Override
    public String toString() { return "Score: " + score + " | Feedback: " + feedback; }
}

// ==========================================
// 2. Stub Interfaces (Simulating the Database)
// ==========================================

interface ExamRepository {
    Optional<Exam> findById(String examId);
    void save(Exam exam);
}

interface ResultRepository {
    Optional<ExamResult> findByStudentAndExam(String studentId, String examId);
}

// ==========================================
// 3. Mock Database Implementation (For Standalone Testing)
// ==========================================

class InMemoryDatabase implements ExamRepository, ResultRepository {
    private Map<String, Exam> examTable = new HashMap<>();
    private List<ExamResult> resultTable = new ArrayList<>();

    // Stub: Save Exam
    public void save(Exam exam) {
        examTable.put(exam.getExamId(), exam);
    }

    // Stub: Find Exam
    public Optional<Exam> findById(String examId) {
        return Optional.ofNullable(examTable.get(examId));
    }

    // Stub: Seed Results (Helper for setup)
    public void addResult(ExamResult result) {
        resultTable.add(result);
    }

    // Stub: Find Result
    public Optional<ExamResult> findByStudentAndExam(String studentId, String examId) {
        return resultTable.stream()
                // --- FIX START: Using getters instead of direct field access ---
                .filter(r -> r.getStudentId().equals(studentId) && r.getExamId().equals(examId))
                // --- FIX END ---
                .findFirst();
    }
}

// ==========================================
// 4. Service Layer (The Business Logic)
// ==========================================

class ExamControlService {
    private ExamRepository examRepo;
    private ResultRepository resultRepo;

    public ExamControlService(ExamRepository examRepo, ResultRepository resultRepo) {
        this.examRepo = examRepo;
        this.resultRepo = resultRepo;
    }

    /**
     * INSTRUCTOR ACTION: Change visibility
     * Satisfies the "I want to control when exam results become visible" requirement.
     */
    public void setExamVisibility(String examId, VisibilityStatus status) {
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        
        exam.setVisibilityStatus(status);
        examRepo.save(exam);
        System.out.println("LOG: Exam '" + exam.getTitle() + "' visibility set to " + status);
    }

    /**
     * STUDENT ACTION: View Results
     * Contains the logic for AC1 and AC2.
     */
    public String viewExamResult(String studentId, String examId) {
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        // LOGIC FOR AC1 & AC3: 
        // If status is HIDDEN, deny access regardless of whether data exists.
        if (exam.getVisibilityStatus() == VisibilityStatus.HIDDEN) {
            return "ACCESS DENIED: Results for this exam are not yet published.";
        }

        // LOGIC FOR AC2:
        // If RELEASED, fetch and return data.
        return resultRepo.findByStudentAndExam(studentId, examId)
                .map(ExamResult::toString)
                .orElse("No result found for this student.");
    }
}

// ==========================================
// 5. Main Class (The Test Harness)
// ==========================================

public class ExamVisibilitySystem {
    
    // valid student ID for testing
    private static final String STUDENT_ID = "S12345"; 
    private static final String EXAM_ID = "CS101";

    public static void main(String[] args) {
        // 1. Setup Dependencies
        InMemoryDatabase db = new InMemoryDatabase();
        ExamControlService service = new ExamControlService(db, db);
        Scanner scanner = new Scanner(System.in);

        // 2. Setup Initial Data
        Exam midTerms = new Exam(EXAM_ID, "Agile Software Dev Mid-Term");
        db.save(midTerms); // Defaults to HIDDEN
        
        // Seed a grade
        db.addResult(new ExamResult(STUDENT_ID, EXAM_ID, 85.5, "Good understanding of Scrum."));

        boolean running = true;

        while (running) {
            // --- DASHBOARD UI ---
            System.out.println("\n==========================================");
            System.out.println("   ONLINE EXAM SYSTEM - SPRINT 1 DEMO");
            System.out.println("==========================================");
            
            // Peek at backend status for the demo context (cheat sheet for the tutor)
            Exam currentExam = db.findById(EXAM_ID).get();
            System.out.println(" [SYSTEM STATE] Exam: " + currentExam.getTitle());
            System.out.println(" [SYSTEM STATE] Visibility: " + currentExam.getVisibilityStatus());
            System.out.println("==========================================");
            System.out.println("Select your role:");
            System.out.println("1. Instructor (Manage Visibility)");
            System.out.println("2. Student (View Results)");
            System.out.println("3. Exit");
            System.out.print("> ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    handleInstructorFlow(service, scanner);
                    break;
                case "2":
                    handleStudentFlow(service);
                    break;
                case "3":
                    running = false;
                    System.out.println("Shutting down system...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    // --- INSTRUCTOR WORKFLOW ---
    private static void handleInstructorFlow(ExamControlService service, Scanner scanner) {
        System.out.println("\n--- INSTRUCTOR PANEL ---");
        System.out.println("1. Set Results to HIDDEN (Moderation in progress)");
        System.out.println("2. Set Results to RELEASED (Publish to students)");
        System.out.println("3. Cancel");
        System.out.print("Choose action > ");

        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                service.setExamVisibility(EXAM_ID, VisibilityStatus.HIDDEN);
                System.out.println("SUCCESS: Exam results are now HIDDEN from students.");
                break;
            case "2":
                service.setExamVisibility(EXAM_ID, VisibilityStatus.RELEASED);
                System.out.println("SUCCESS: Exam results are now LIVE/RELEASED.");
                break;
            case "3":
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
        
        // Pause so the user can read the message
        System.out.println("(Press Enter to continue)");
        scanner.nextLine();
    }

    // --- STUDENT WORKFLOW ---
    private static void handleStudentFlow(ExamControlService service) {
        System.out.println("\n--- STUDENT PORTAL ---");
        System.out.println("Logging in as: " + STUDENT_ID + "...");
        System.out.println("Attempting to view results for " + EXAM_ID + "...");
        
        String result = service.viewExamResult(STUDENT_ID, EXAM_ID);
        
        System.out.println("\n---------------------------------");
        System.out.println(result);
        System.out.println("---------------------------------");

        // Pause so the user can read the message
        System.out.println("(Press Enter to continue)");
        Scanner s = new Scanner(System.in); // Simple pause hack
        s.nextLine(); 
    }
}