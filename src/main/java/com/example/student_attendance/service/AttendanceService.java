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
import java.util.LinkedHashMap;

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

    //Menambahkan Absensi
    public String addAttendance(Long studentId, String keterangan) {
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

    // Mengecek Absensi Hari ini (per Siswa)
    public List<Map<String, Object>> getHistoryByStudent(Long studentId) {
        List<Student_Attendance> rawData = attendanceRepo.findByStudentId(studentId);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Student_Attendance absen : rawData) {
            result.add(convertToDto(absen)); // Panggil helper kita tadi
        }
        return result;
    }

    // Mengecek Absensi Hari ini (semua siswa)
    public List<Map<String, Object>> getTodayAttendance() {
        List<Student_Attendance> rawData = attendanceRepo.findByTanggal(LocalDate.now());

        List<Map<String, Object>> result = new ArrayList<>();
        for (Student_Attendance absen : rawData) {
            result.add(convertToDto(absen)); // Panggil helper kita tadi
        }
        return result;
    }

    public Map<String, Object> getSummary14Days(Long studentId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(13); // Rentang 14 hari termasuk hari ini

        List<Student_Attendance> history = attendanceRepo.findByStudentIdAndTanggalBetween(studentId, startDate, endDate);

        long hadir = 0, sakit = 0, ijin = 0, alpa = 0;

        for (Student_Attendance record : history) {
            String ket = record.getKeterangan().toLowerCase();
            if (ket.contains("hadir")) hadir++;
            else if (ket.contains("sakit")) sakit++;
            else if (ket.contains("ijin") || ket.contains("izin")) ijin++;
            else if (ket.contains("alpa")) alpa++;
        }

        // Opsional: Jika Alpa dihitung dari hari yang tidak ada datanya sama sekali:
        // alpa = 14 - (hadir + sakit + ijin);

        Map<String, Object> summary = new HashMap<>();
        summary.put("periode", startDate + " s/d " + endDate);
        summary.put("total_hadir", hadir);
        summary.put("total_sakit", sakit);
        summary.put("total_ijin", ijin);
        summary.put("total_alpa", alpa);

        return summary;
    }

    // Fungsi Untuk Melihat Rekapan Absensi Siswa 14 Hari
    public List<Map<String, Object>> getAllStudentsSummary14Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(13);

        List<Student> allStudents = studentRepo.findAll();
        List<Student_Attendance> allAttendance = attendanceRepo.findByTanggalBetween(startDate, endDate);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Student s : allStudents) {
            long hadir = 0, sakit = 0, ijin = 0, alpa = 0;

            for (Student_Attendance a : allAttendance) {
                if (a.getStudent().getId().equals(s.getId())) {
                    String ket = a.getKeterangan().toLowerCase();
                    if (ket.contains("hadir")) hadir++;
                    else if (ket.contains("sakit")) sakit++;
                    else if (ket.contains("ijin") || ket.contains("izin")) ijin++;
                    else if (ket.contains("alpa")) alpa++;
                }
            }

            // MENGGUNAKAN LinkedHashMap agar urutan tetap terjaga
            Map<String, Object> sMap = new LinkedHashMap<>();
            sMap.put("nim", s.getNim());      // Pertama
            sMap.put("nama", s.getNama());    // Kedua
            sMap.put("hadir", hadir);
            sMap.put("sakit", sakit);
            sMap.put("ijin", ijin);
            sMap.put("alpa", alpa);

            result.add(sMap);
        }
        return result;
    }
}