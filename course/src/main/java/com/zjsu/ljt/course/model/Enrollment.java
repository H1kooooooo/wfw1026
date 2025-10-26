package com.zjsu.ljt.course.model;

import java.time.LocalDateTime;

public class Enrollment {
    private String id;
    private String courseId;
    private String studentId;
    private LocalDateTime enrolledAt;

    public Enrollment() {
        this.id = java.util.UUID.randomUUID().toString();
        this.enrolledAt = LocalDateTime.now();
    }

    public Enrollment(String courseId, String studentId) {
        this();
        this.courseId = courseId;
        this.studentId = studentId;
    }

    // Getter 和 Setter 方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}