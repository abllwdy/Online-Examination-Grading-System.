package com.examsystem.demo;

import com.examsystem.model.Student;
import com.examsystem.service.StudentFilterService;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive demo class for student search and filter functionality.
 * Allows users to interactively search and filter students.
 */
public class StudentFilterDemo {
    
    private static StudentFilterService filterService;
    private static Scanner scanner;
    private static String currentSearchTerm = "";
    private static String currentSection = "";
    
    public static void main(String[] args) {
        System.out.println("=== Online Examination & Grading System ===");
        System.out.println("=== Student Search and Filter Application ===\n");
        
        // Create sample students
        List<Student> students = createSampleStudents();
        filterService = new StudentFilterService(students);
        scanner = new Scanner(System.in);
        
        System.out.println("Initial Student List (" + students.size() + " students):");
        displayStudents(students);
        System.out.println();
        
        // Main interactive loop
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice.toLowerCase()) {
                case "1":
                    searchStudents();
                    break;
                case "2":
                    filterBySection();
                    break;
                case "3":
                    applyCombinedFilters();
                    break;
                case "4":
                    clearFilters();
                    break;
                case "5":
                    displayAllStudents();
                    break;
                case "6":
                case "exit":
                case "quit":
                    running = false;
                    System.out.println("\nThank you for using the Student Search and Filter System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Displays the main menu.
     */
    private static void displayMenu() {
        System.out.println("==========================================");
        System.out.println("MAIN MENU");
        System.out.println("==========================================");
        System.out.println("1. Search by Name or ID");
        System.out.println("2. Filter by Section");
        System.out.println("3. Apply Combined Filters (Search + Section)");
        System.out.println("4. Clear All Filters");
        System.out.println("5. Show All Students");
        System.out.println("6. Exit");
        System.out.println("==========================================");
        System.out.print("Enter your choice: ");
    }
    
    /**
     * Interactive search by name or ID.
     * Acceptance Criteria 1: Given I am on the student list page, when I type a student's name 
     * or ID into the search bar, then the list filters to show only matching students.
     */
    private static void searchStudents() {
        System.out.println("\n--- Search by Name or ID ---");
        System.out.print("Enter search term (name or ID): ");
        String searchTerm = scanner.nextLine().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty. Showing all students.\n");
            currentSearchTerm = "";
            displayStudents(filterService.clearFilters());
            return;
        }
        
        currentSearchTerm = searchTerm;
        List<Student> results = filterService.searchStudents(searchTerm);
        
        System.out.println("\nSearch Results for '" + searchTerm + "':");
        System.out.println("Found " + results.size() + " student(s)\n");
        displayStudents(results);
        System.out.println();
    }
    
    /**
     * Interactive filter by section.
     * Acceptance Criteria 2: Given I want to view students by section, when I select a section 
     * from a dropdown filter, then the list updates to show only students enrolled in that section.
     */
    private static void filterBySection() {
        System.out.println("\n--- Filter by Section ---");
        System.out.print("Enter section (A, B, or C): ");
        String section = scanner.nextLine().trim().toUpperCase();
        
        if (section.isEmpty()) {
            System.out.println("Section cannot be empty. Showing all students.\n");
            currentSection = "";
            displayStudents(filterService.clearFilters());
            return;
        }
        
        currentSection = section;
        List<Student> results = filterService.filterBySection(section);
        
        System.out.println("\nStudents in Section '" + section + "':");
        System.out.println("Found " + results.size() + " student(s)\n");
        displayStudents(results);
        System.out.println();
    }
    
    /**
     * Apply both search and section filters together.
     */
    private static void applyCombinedFilters() {
        System.out.println("\n--- Apply Combined Filters ---");
        System.out.print("Enter search term (name or ID, or press Enter to skip): ");
        String searchTerm = scanner.nextLine().trim();
        
        System.out.print("Enter section (A, B, or C, or press Enter to skip): ");
        String section = scanner.nextLine().trim().toUpperCase();
        
        if (searchTerm.isEmpty() && section.isEmpty()) {
            System.out.println("No filters applied. Showing all students.\n");
            currentSearchTerm = "";
            currentSection = "";
            displayStudents(filterService.clearFilters());
            return;
        }
        
        currentSearchTerm = searchTerm;
        currentSection = section;
        List<Student> results = filterService.applyFilters(
            searchTerm.isEmpty() ? null : searchTerm,
            section.isEmpty() ? null : section
        );
        
        System.out.println("\nCombined Filter Results:");
        if (!searchTerm.isEmpty()) {
            System.out.println("  Search: '" + searchTerm + "'");
        }
        if (!section.isEmpty()) {
            System.out.println("  Section: '" + section + "'");
        }
        System.out.println("Found " + results.size() + " student(s)\n");
        displayStudents(results);
        System.out.println();
    }
    
    /**
     * Clear all filters and show all students.
     * Acceptance Criteria 3: Given I have applied a filter, when I click "Clear Filters," 
     * then the full, unfiltered student list is displayed.
     */
    private static void clearFilters() {
        System.out.println("\n--- Clear All Filters ---");
        currentSearchTerm = "";
        currentSection = "";
        List<Student> allStudents = filterService.clearFilters();
        
        System.out.println("All filters cleared. Showing full student list:");
        System.out.println("Total: " + allStudents.size() + " student(s)\n");
        displayStudents(allStudents);
        System.out.println();
    }
    
    /**
     * Display all students without applying any filters.
     */
    private static void displayAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> allStudents = filterService.getAllStudents();
        System.out.println("Total: " + allStudents.size() + " student(s)\n");
        displayStudents(allStudents);
        System.out.println();
    }
    
    /**
     * Creates sample student data for demonstration.
     */
    private static List<Student> createSampleStudents() {
        return Arrays.asList(
            new Student("S001", "John Smith", "A"),
            new Student("S002", "Alice Johnson", "A"),
            new Student("S003", "Bob Williams", "B"),
            new Student("S004", "John Doe", "B"),
            new Student("S005", "Carol Brown", "A"),
            new Student("S006", "David Miller", "C"),
            new Student("S007", "Eva Davis", "A"),
            new Student("S008", "Frank Wilson", "C"),
            new Student("S009", "Grace Moore", "B"),
            new Student("S010", "Henry Taylor", "C")
        );
    }
    
    /**
     * Displays a list of students in a formatted table.
     */
    private static void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("  No students found.");
            return;
        }
        
        System.out.println("┌──────────┬──────────────────────┬──────────┐");
        System.out.println("│ StudentID│ Name                  │ Section  │");
        System.out.println("├──────────┼──────────────────────┼──────────┤");
        
        for (Student student : students) {
            System.out.printf("│ %-8s │ %-20s │ %-8s │%n",
                student.getStudentId(),
                student.getName(),
                student.getSection());
        }
        
        System.out.println("└──────────┴──────────────────────┴──────────┘");
    }
}
