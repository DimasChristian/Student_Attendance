package com.example.student_attendance.repo;

import com.example.student_attendance.Models.Student_Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface Student_AttendanceRepo extends JpaRepository<Student_Attendance, Long> {
    List<Student_Attendance> findByStudentId(Long studentId);
    List<Student_Attendance> findByTanggal(LocalDate tanggal);
    boolean existsByStudentIdAndTanggal(Long studentId, LocalDate tanggal);

    // Rekapan per Siswa
    List<Student_Attendance> findByStudentIdAndTanggalBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    // Rekapan Seluruh siswa
    List<Student_Attendance> findByTanggalBetween(LocalDate startDate, LocalDate endDate);
}
