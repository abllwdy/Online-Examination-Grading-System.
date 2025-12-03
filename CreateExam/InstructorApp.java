import java.util.List;
import java.util.Scanner;

public class InstructorApp {
    private static ExamService examService = new ExamService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Instructor Dashboard ===");
            System.out.println("1. Create New Exam");
            System.out.println("2. View All Exams");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createExam();
                    break;
                case "2":
                    viewAllExams();
                    break;
                case "3":
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    /**
     * AC1 & AC2: Handle exam creation
     */
    private static void createExam() {
    System.out.println("\n=== Create New Exam ===");
    
    System.out.print("Enter exam title: ");
    String title = scanner.nextLine();

    System.out.print("Enter exam instructions: ");
    String instructions = scanner.nextLine();

    ExamCreationResult result = examService.createExam(title, instructions);
    System.out.println(result.getMessage());

    if (result.isSuccess()) {
        System.out.println("Exam ID: " + result.getExam().getExamId());
        System.out.println("Total exams: " + examService.getExamCount());
    }
}


    /**
     * AC3: Display all created exams
     */
    private static void viewAllExams() {
        System.out.println("\n=== All Exams ===");
        List<Exam> exams = examService.getAllExams();

        if (exams.isEmpty()) {
            System.out.println("No exams available.");
        } else {
            for (Exam exam : exams) {
                System.out.println(exam);
            }
            System.out.println("\nTotal exams: " + exams.size());
        }
    }
}
