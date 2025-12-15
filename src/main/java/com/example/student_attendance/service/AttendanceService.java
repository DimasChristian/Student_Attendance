package com.example.student_attendance.service;

import com.example.student_attendance.Models.Student;
import com.example.student_attendance.Models.Student_Attendance;
import com.example.student_attendance.repo.StudentRepo;
import com.example.student_attendance.repo.Student_AttendanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AttendanceService {

    @Autowired
    private Student_AttendanceRepo attendanceRepo;

    @Autowired
    private StudentRepo studentRepo;

    // --- HELPER METHOD (Fungsi Bantuan) ---
    private Map<String, Object> convertToDto(Student_Attendance absen) {
        Map<String, Object> data = new HashMap<>();
        // Kita PILIH field mana saja yang mau ditampilkan
        data.put("tanggal", absen.getTanggal());
        data.put("keterangan", absen.getKeterangan());
        data.put("student", absen.getStudent()); // Data siswa tetap ikut
        return data;
    }
    // --------------------------------------

    public String addAttendance(Long studentId, String keterangan) {
        // ... (Kode bagian addAttendance TETAP SAMA seperti sebelumnya, tidak perlu diubah)
        LocalDate today = LocalDate.now();
        if (attendanceRepo.existsByStudentIdAndTanggal(studentId, today)) {
            throw new RuntimeException("Gagal: Siswa ini sudah absen hari ini.");
        }
        Student siswa = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));

        Student_Attendance absen = new Student_Attendance();
        absen.setStudent(siswa);
        absen.setTanggal(today);
        absen.setKeterangan(keterangan);
        attendanceRepo.save(absen);
        return "Absensi berhasil dicatat";
    }

    // Ubah tipe return menjadi List<Map<String, Object>>
    public List<Map<String, Object>> getHistoryByStudent(Long studentId) {
        List<Student_Attendance> rawData = attendanceRepo.findByStudentId(studentId);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Student_Attendance absen : rawData) {
            result.add(convertToDto(absen)); // Panggil helper kita tadi
        }
        return result;
    }

    // Ubah tipe return menjadi List<Map<String, Object>>
    public List<Map<String, Object>> getTodayAttendance() {
        List<Student_Attendance> rawData = attendanceRepo.findByTanggal(LocalDate.now());

        List<Map<String, Object>> result = new ArrayList<>();
        for (Student_Attendance absen : rawData) {
            result.add(convertToDto(absen)); // Panggil helper kita tadi
        }
        return result;
    }
}