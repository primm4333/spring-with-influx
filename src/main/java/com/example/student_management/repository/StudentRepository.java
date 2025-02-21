package com.example.student_management.repository;

import com.example.student_management.model.Student;
import java.util.List;

public interface StudentRepository {
    void saveStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(String sID);
    void deleteStudent(String sID);
}