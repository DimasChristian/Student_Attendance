package com.example.student_attendance.Models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendances") // Pastikan nama tabel ini sesuai database
public class Student_Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDate tanggal;

    @Column(nullable = false)
    private String keterangan;

    // --- Constructor Kosong ---
    public Student_Attendance() {}

    // --- Setter Getter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}