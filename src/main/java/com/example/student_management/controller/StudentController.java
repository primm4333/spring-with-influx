package com.example.student_management.controller;

import com.example.student_management.model.Student;
import com.example.student_management.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        try {
            studentService.saveStudent(student);
            return ResponseEntity.ok("Student added successfully!");
        } catch (Exception e) {
            logger.error("Error adding student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            return ResponseEntity.ok(studentService.getAllStudents());
        } catch (Exception e) {
            logger.error("Error fetching students", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        try {
            Student student = studentService.getStudentById(id);
            return student != null ? ResponseEntity.ok(student) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Error fetching student by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
