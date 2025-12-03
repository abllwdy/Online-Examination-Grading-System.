import model.Question;
import model.StudentSession;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import data.DataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static final DateTimeFormatter CUSTOM_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")
                    .withZone(ZoneId.systemDefault());
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        if (DataStore.questionList.isEmpty()) {
            try {
                // Ensure a valid question is loaded for US07 testing
                DataStore.questionList.add(new Question("Q1: What is the capital of Malaysia?", List.of("Kuala Lumpur", "Penang", "Johor Bahru", "Melaka"), 0));
                System.out.println("SYSTEM: Added one test question for immediate Start Exam use.");
            } catch (Exception ignored) {}
        }

        while (true) {
            // Updated Menu Flow with 'View' restored
            System.out.println("\n=== OEGS MAIN MENU ===");
            System.out.println("1. Create New Question (US08)");
            System.out.println("2. Start Exam (US07)");
            System.out.println("3. View All Questions (Verify US08)"); // RESTORED OPTION
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            String choice = keyboard.nextLine().trim();

            if (choice.equals("1")) {
                createNewQuestion(keyboard);
            } else if (choice.equals("2") || choice.equalsIgnoreCase("START")) {
                startExam(keyboard); // New US07 Logic
            } else if (choice.equals("3") || choice.equalsIgnoreCase("VIEW")) { // RESTORED CALL
                viewQuestions();
            } else if (choice.equals("4") || choice.equalsIgnoreCase("EXIT")) {
                System.out.println("Exiting system...");
                break;
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    // The Logic for US08
    private static void createNewQuestion(Scanner keyboard) {
        try {
            System.out.println("\n--- Create MCQ ---");

            // 1. Get Text (Validates not empty later)
            System.out.print("Enter Question Text: ");
            String text = keyboard.nextLine();

            // 2. Get 4 Options
            List<String> options = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter Option " + (char)('A' + i) + ": ");
                String opt = keyboard.nextLine();
                options.add(opt);
            }

            // 3. Get Correct Answer (AC Negative Test: Non-numeric input)
            System.out.print("Enter Correct Option Number (0 for A, 1 for B...): ");
            int correctIndex = -1;

            try {
                correctIndex = Integer.parseInt(keyboard.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ ERROR: You must enter a number (0-3). Text input rejected.");
                return; // Stop function
            }

            // 4. Build & Save (Triggers all Question.java validations)
            Question q = new Question(text, options, correctIndex);

            // 5. Save to "Database"
            DataStore.addQuestion(q);

        } catch (IllegalArgumentException e) {
            // Catches: Empty text, Duplicates, Invalid Index
            System.out.println("❌ FAILED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ SYSTEM ERROR: " + e.getMessage());
        }
    }
    // Final commit to test Jira link
    private static void viewQuestions() {
        System.out.println("\n--- Current Exam Content (RAM) ---");
        List<Question> all = DataStore.getAllQuestions();

        if (all.isEmpty()) {
            System.out.println("No questions created yet.");
        } else {
            for (int i = 0; i < all.size(); i++) {
                Question q = all.get(i);

                // Print Separator and Question ID/Text
                System.out.println("\n--- ID " + i + " ---");
                System.out.println("Q: " + q.getQuestionText());

                // Print Options (A, B, C, D)
                List<String> options = q.getOptions();
                for (int j = 0; j < options.size(); j++) {
                    System.out.println("   " + (char)('A' + j) + ") " + options.get(j));
                }
            }
        }
    }

    // US07: Start Exam Logic (New Sprint 2 Task)
    private static void startExam(Scanner keyboard) {

        // US07 Pre-Checks
        if (DataStore.questionList.isEmpty()) {
            System.out.println("❌ Error: Exam has no questions. Create one first (Option 1).");
            return;
        }

        if (DataStore.currentSession.getStatus().equals(StudentSession.STATUS_SUBMITTED)) {
            System.out.println("❌ You have already completed this exam.");
            return;
        }

        try {
            // Start the session if it hasn't already (US07 logic)
            if (!DataStore.currentSession.getStatus().equals(StudentSession.STATUS_IN_PROGRESS)) {
                DataStore.currentSession.start();
                System.out.println("\n\n================================================");
                System.out.println("✅ Exam Session Started! Status: IN PROGRESS");
                System.out.println("================================================\n");
            } else {
                System.out.println("\n... Resuming Exam Session ...");
            }

            // Display Start Time (Using your preferred formatted output)
            // Note: If you have not implemented the DateTimeFormatter, remove the CUSTOM_FORMATTER.format part.
            System.out.println("Started at: " + CUSTOM_FORMATTER.format(DataStore.currentSession.getStartTime()));

            // --- US06: Answering Loop (Core Logic) ---
            int currentQIndex = 0;

            while (true) {
                Question q = DataStore.questionList.get(currentQIndex);

                // 1. Display Question
                System.out.println("\n------------------------------------------------");
                System.out.println("QUESTION " + (currentQIndex + 1) + " of " + DataStore.questionList.size() + ": " + q.getQuestionText());
                List<String> opts = q.getOptions();
                for (int i = 0; i < opts.size(); i++) {
                    System.out.println("   " + (char)('A' + i) + ") " + opts.get(i));
                }

                // Show currently saved answer (AC: Allow overwriting)
                String savedAns = DataStore.currentSession.getAnswer(currentQIndex);
                if (savedAns != null) {
                    System.out.println("   [Current Answer: " + savedAns + "]");
                }

                // 2. Prompt Input
                System.out.println("------------------------------------------------");
                System.out.print("Enter Answer (A-D), 'N' for Next, 'P' for Prev, or 'Exit': ");

                // AC: Accept input case-insensitively
                String input = keyboard.nextLine().trim().toUpperCase();

                // 3. Input Handling
                if (input.equals("EXIT")) {
                    System.out.println("Pausing exam... (Timer continues in background)");
                    break;
                }
                else if (input.equals("N")) {
                    if (currentQIndex < DataStore.questionList.size() - 1) currentQIndex++;
                    else System.out.println("⚠ This is the last question.");
                }
                else if (input.equals("N")) {
                    // Check if a next question exists
                    if (currentQIndex < DataStore.questionList.size() - 1) {
                        currentQIndex++; // Move to the next index
                    } else {
                        // AC: Clearer notification for the end of the list
                        System.out.println("=================================================");
                        System.out.println("✅ You have reached the **LAST QUESTION** (Q" + (currentQIndex + 1) + ").");
                        System.out.println("   Type 'Exit' to pause or implement 'Submit' (future feature) to finish.");
                        System.out.println("=================================================");
                    }
                }
                // AC: Valid Option Check (A, B, C, or D)
                else if (input.length() == 1 && input.matches("[A-D]")) {

                    // AC: Save Answer Instantly (No try-catch needed now)
                    DataStore.currentSession.saveAnswer(currentQIndex, input);
                    // AC: Print Confirmation
                    System.out.println("✅ Answer [" + input + "] Saved!");
                }
                // AC: Empty string check
                else if (input.isEmpty()) {
                    System.out.println("⚠ No answer provided.");
                }
                // AC: Multiple characters check and invalid input (e.g., "E")
                else {
                    System.out.println("❌ Invalid Option. Please enter a single letter (A-D).");
                }
            }

        } catch (IllegalStateException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        // AC: Handle null session crash (This is the most graceful way to handle it at this stage)
        catch (Exception e) {
            System.out.println("❌ FATAL ERROR: Session lost or invalid data access. Please restart the application.");
        }
    }
}