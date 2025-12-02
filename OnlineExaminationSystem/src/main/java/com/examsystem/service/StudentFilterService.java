package com.examsystem.service;

import com.examsystem.model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for filtering and searching students.
 * Implements the instructor's ability to search and filter students by name, ID, and section.
 */
public class StudentFilterService {
    private List<Student> allStudents;
    private List<Student> filteredStudents;

    public StudentFilterService(List<Student> students) {
        this.allStudents = new ArrayList<>(students);
        this.filteredStudents = new ArrayList<>(allStudents);
    }

    /**
     * Searches students by name or ID.
     * Acceptance Criteria 1: Given I am on the student list page, when I type a student's name 
     * or ID into the search bar, then the list filters to show only matching students.
     * 
     * @param searchTerm The search term (name or ID) to filter by
     * @return List of students matching the search term
     */
    public List<Student> searchStudents(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            filteredStudents = new ArrayList<>(allStudents);
            return filteredStudents;
        }

        String lowerSearchTerm = searchTerm.toLowerCase().trim();
        filteredStudents = allStudents.stream()
                .filter(student -> 
                    student.getName().toLowerCase().contains(lowerSearchTerm) ||
                    student.getStudentId().toLowerCase().contains(lowerSearchTerm)
                )
                .collect(Collectors.toList());

        return filteredStudents;
    }

    /**
     * Filters students by section.
     * Acceptance Criteria 2: Given I want to view students by section, when I select a section 
     * from a dropdown filter, then the list updates to show only students enrolled in that section.
     * 
     * @param section The section to filter by
     * @return List of students in the specified section
     */
    public List<Student> filterBySection(String section) {
        if (section == null || section.trim().isEmpty()) {
            filteredStudents = new ArrayList<>(allStudents);
            return filteredStudents;
        }

        filteredStudents = allStudents.stream()
                .filter(student -> student.getSection().equalsIgnoreCase(section.trim()))
                .collect(Collectors.toList());

        return filteredStudents;
    }

    /**
     * Applies both search and section filter.
     * 
     * @param searchTerm The search term (name or ID)
     * @param section The section to filter by
     * @return List of students matching both criteria
     */
    public List<Student> applyFilters(String searchTerm, String section) {
        List<Student> result = new ArrayList<>(allStudents);

        // Apply search filter
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String lowerSearchTerm = searchTerm.toLowerCase().trim();
            result = result.stream()
                    .filter(student -> 
                        student.getName().toLowerCase().contains(lowerSearchTerm) ||
                        student.getStudentId().toLowerCase().contains(lowerSearchTerm)
                    )
                    .collect(Collectors.toList());
        }

        // Apply section filter
        if (section != null && !section.trim().isEmpty()) {
            result = result.stream()
                    .filter(student -> student.getSection().equalsIgnoreCase(section.trim()))
                    .collect(Collectors.toList());
        }

        filteredStudents = result;
        return filteredStudents;
    }

    /**
     * Clears all filters and returns the full student list.
     * Acceptance Criteria 3: Given I have applied a filter, when I click "Clear Filters," 
     * then the full, unfiltered student list is displayed.
     * 
     * @return The complete, unfiltered list of all students
     */
    public List<Student> clearFilters() {
        filteredStudents = new ArrayList<>(allStudents);
        return filteredStudents;
    }

    /**
     * Gets the current filtered list of students.
     * 
     * @return Current filtered students
     */
    public List<Student> getFilteredStudents() {
        return new ArrayList<>(filteredStudents);
    }

    /**
     * Gets all students without any filters.
     * 
     * @return All students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(allStudents);
    }

    /**
     * Adds a new student to the list.
     * 
     * @param student The student to add
     */
    public void addStudent(Student student) {
        allStudents.add(student);
        filteredStudents.add(student);
    }
}

