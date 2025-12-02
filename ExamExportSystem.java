import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExamExportSystem {

    // ==========================================
    // 1. Data Model (Student Result)
    // ==========================================
    static class StudentResult {
        private String studentId;
        private String name;
        private double score;
        private String grade;

        public StudentResult(String studentId, String name, double score) {
            this.studentId = studentId;
            this.name = name;
            this.score = score;
            this.grade = calculateGrade(score);
        }

        private String calculateGrade(double score) {
            if (score >= 80) return "A";
            if (score >= 70) return "B";
            if (score >= 60) return "C";
            if (score >= 50) return "D";
            return "F";
        }

        // For CSV export
        public String toCSVRow() {
            return studentId + "," + name + "," + score + "," + grade;
        }

        @Override
        public String toString() {
            return String.format("%-10s | %-15s | %-5.1f | %s",
                    studentId, name, score, grade);
        }
    }

    // ==========================================
    // 2. Export Logic
    // ==========================================
    public static void exportDataToCSV(List<StudentResult> results, String filename) {
        System.out.println("\n[SYSTEM] Exporting data...");

        try (FileWriter writer = new FileWriter(filename)) {

            writer.write("Student ID,Name,Score,Grade\n");

            for (StudentResult result : results) {
                writer.write(result.toCSVRow() + "\n");
            }

            System.out.println("[SUCCESS] Exported to file: " + filename);

        } catch (IOException e) {
            System.out.println("[ERROR] Could not write to file: " + e.getMessage());
        }
    }

    // ==========================================
    // 3. Main Program
    // ==========================================
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<StudentResult> database = new ArrayList<>();
        database.add(new StudentResult("S001", "Alice Tan", 85.5));
        database.add(new StudentResult("S002", "Bob Lee", 62.0));
        database.add(new StudentResult("S003", "Charlie Wu", 45.0));
        database.add(new StudentResult("S004", "Diana Lim", 78.0));
        database.add(new StudentResult("S005", "Ethan Ng", 92.5));

        boolean running = true;
        String examName = "CS102_Finals";

        while (running) {
            System.out.println("\n==========================================");
            System.out.println("   INSTRUCTOR DASHBOARD - EXPORT TOOL");
            System.out.println("==========================================");
            System.out.println("Exam: " + examName);
            System.out.println("Total Students: " + database.size());
            System.out.println("------------------------------------------");
            System.out.println("1. View Grade List");
            System.out.println("2. Export Grades to CSV");
            System.out.println("3. Exit");
            System.out.print("> Select an option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("\n--- Grade List ---");
                    System.out.printf("%-10s | %-15s | %-5s | %s\n",
                            "ID", "Name", "Score", "G");
                    System.out.println("------------------------------------------");

                    for (StudentResult r : database) {
                        System.out.println(r);
                    }

                    pressEnterToContinue(scanner);
                    break;

                case "2":
                    String filename = examName + "_Grades.csv";
                    System.out.println("\n[INFO] Exporting to: " + filename);
                    exportDataToCSV(database, filename);
                    pressEnterToContinue(scanner);
                    break;

                case "3":
                    running = false;
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("[ERROR] Invalid option.");
            }
        }

        scanner.close();
    }

    private static void pressEnterToContinue(Scanner s) {
        System.out.println("\n(Press Enter to continue)");
        s.nextLine();
    }
}
