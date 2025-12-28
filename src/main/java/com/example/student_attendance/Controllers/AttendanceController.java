package com.example.student_attendance.Controllers;

import com.example.student_attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // ... (Method POST addAttendance TETAP SAMA, tidak perlu diubah) ...
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addAttendance(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long studentId = Long.parseLong(payload.get("student_id").toString());
            String keterangan = payload.get("keterangan").toString();

            // Panggil Service
            String message = attendanceService.addAttendance(studentId, keterangan);

            response.put("status", "success");
            response.put("message", message);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Menangkap error dari Service (misal: sudah absen atau siswa tidak ada)
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Rekapan Absensi per Siswa (hari ini)
    @GetMapping("/history/{studentId}")
    public ResponseEntity<Map<String, Object>> getHistoryByStudent(@PathVariable Long studentId) {
        // Perubahan disini: Tipe datanya sekarang List<Map<String, Object>>
        List<Map<String, Object>> history = attendanceService.getHistoryByStudent(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", history);
        return ResponseEntity.ok(response);
    }

    // Rekapan Absensi Seluruh Siswa (hari ini)
    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodayAttendance() {
        // Perubahan disini: Tipe datanya sekarang List<Map<String, Object>>
        List<Map<String, Object>> listToday = attendanceService.getTodayAttendance();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("total_hadir", listToday.size());
        response.put("data", listToday);
        return ResponseEntity.ok(response);
    }

    // Rekapan Absensi per Siswa (14 hari)
    @GetMapping("/summary/{studentId}")
    public ResponseEntity<Map<String, Object>> getAttendanceSummary(@PathVariable Long studentId) {
        Map<String, Object> summary = attendanceService.getSummary14Days(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", summary);

        return ResponseEntity.ok(response);
    }

    // Rekapan Absensi Seluruh Siswa (14 hari)
    @GetMapping("/summary/all")
    public ResponseEntity<Map<String, Object>> getAllSummary() {
        List<Map<String, Object>> allData = attendanceService.getAllStudentsSummary14Days();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("periode", "14 Hari Terakhir");
        response.put("data", allData);

        return ResponseEntity.ok(response);
    }
}