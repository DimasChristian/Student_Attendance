package com.example.student_attendance.repo;

import com.example.student_attendance.Models.Student_Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Student_AttendanceRepo extends JpaRepository<Student_Attendance, Long> {

    // Mencari history berdasarkan ID Siswa
    // Spring Data JPA otomatis tahu "StudentId" mengacu pada field "student" -> "id"
    List<Student_Attendance> findByStudentId(Long studentId);

    // Mencari data berdasarkan tanggal (untuk fitur "Lihat yang hadir hari ini")
    List<Student_Attendance> findByTanggal(LocalDate tanggal);

    // Cek apakah siswa (by ID) sudah absen pada tanggal tertentu (return true/false)
    boolean existsByStudentIdAndTanggal(Long studentId, LocalDate tanggal);
}