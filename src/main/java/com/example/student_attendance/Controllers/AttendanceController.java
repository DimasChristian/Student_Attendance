package com.example.student_attendance.Controllers;

import com.example.student_attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        // Copy paste kode addAttendance Anda yang lama disini (tidak ada perubahan)
        // ...
        // Agar tidak panjang, saya skip tulis ulang, pakai yg lama saja.
        return null; // (Hapus baris ini dan pakai kode lama Anda)
    }

    @GetMapping("/history/{studentId}")
    public ResponseEntity<Map<String, Object>> getHistoryByStudent(@PathVariable Long studentId) {
        // Perubahan disini: Tipe datanya sekarang List<Map<String, Object>>
        List<Map<String, Object>> history = attendanceService.getHistoryByStudent(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", history);
        return ResponseEntity.ok(response);
    }

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
}