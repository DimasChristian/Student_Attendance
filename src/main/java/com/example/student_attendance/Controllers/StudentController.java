package com.example.student_attendance.Controllers; // Sesuaikan package

import com.example.student_attendance.Models.Student;
import com.example.student_attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 1. READ
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    // 2. READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.findById(id);
        return ResponseEntity.ok(student);
    }

    // 3. CREATE (Dengan pesan sukses)
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody Student student) {
        Student savedStudent = studentService.save(student);

        // Bungkus respon dalam Map
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data siswa berhasil ditambahkan");
        response.put("data", savedStudent);

        return ResponseEntity.ok(response);
    }

    // 4. UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.update(id, studentDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data siswa berhasil diperbarui"); // Pesan Update
        response.put("data", updatedStudent);

        return ResponseEntity.ok(response);
    }

    // 5. DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data siswa berhasil dihapus"); // Pesan Delete

        return ResponseEntity.ok(response);
    }
}