package com.examsystem.model;

/**
 * Student model class representing a student in the system.
 */
public class Student {
    private String studentId;
    private String name;
    private String section;

    public Student(String studentId, String name, String section) {
        this.studentId = studentId;
        this.name = name;
        this.section = section;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", section='" + section + '\'' +
                '}';
    }
}

