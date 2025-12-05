package com.example.student_attendance.service;

import com.example.student_attendance.Models.Student;
import com.example.student_attendance.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepository;

    // 1. Ambil semua data siswa
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    // 2. Ambil siswa berdasarkan ID
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siswa dengan ID " + id + " tidak ditemukan"));
    }

    // 3. Simpan siswa baru
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    // 4. Update data siswa (Disesuaikan dengan field NIM, Nama, Jurusan)
    public Student update(Long id, Student studentDetails) {
        // Cari data lama dulu
        Student existingStudent = findById(id);

        // Update data lama dengan data baru
        existingStudent.setNim(studentDetails.getNim());
        existingStudent.setNama(studentDetails.getNama());
        existingStudent.setJurusan(studentDetails.getJurusan());

        // Simpan perubahan ke database
        return studentRepository.save(existingStudent);
    }

    // 5. Hapus siswa
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}