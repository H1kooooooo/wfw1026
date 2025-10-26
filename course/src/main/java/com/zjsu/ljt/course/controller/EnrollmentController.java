package com.zjsu.ljt.course.controller;

import com.zjsu.ljt.course.model.Enrollment;
import com.zjsu.ljt.course.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> enrollStudent(@RequestBody Map<String, String> request) {
        try {
            String courseId = request.get("courseId");
            String studentId = request.get("studentId");

            if (courseId == null || studentId == null) {
                return ResponseEntity.badRequest().body(createErrorResponse(400, "课程ID和学生ID不能为空"));
            }

            Enrollment enrollment = enrollmentService.enrollStudent(courseId, studentId);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 201);
            response.put("message", "选课成功");
            response.put("data", enrollment);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(400, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("data", enrollments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> getEnrollmentsByCourse(@PathVariable String courseId) {
        List<Enrollment> enrollments = enrollmentService.findByCourseId(courseId);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("data", enrollments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getEnrollmentsByStudent(@PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.findByStudentId(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("data", enrollments);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEnrollment(@PathVariable String id) {
        Optional<Enrollment> enrollment = enrollmentService.findById(id);
        if (enrollment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(404, "选课记录不存在"));
        }

        enrollmentService.deleteEnrollment(id);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "退课成功");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> createErrorResponse(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("data", null);
        return response;
    }
}