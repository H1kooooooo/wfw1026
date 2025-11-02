package com.zjsu.ljt.course.controller;

import com.zjsu.ljt.course.dto.ApiResponse;
import com.zjsu.ljt.course.model.Enrollment;
import com.zjsu.ljt.course.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Enrollment>> enrollStudent(@RequestBody EnrollmentRequest request) {
        try {
            Long courseId = request.getCourseId();
            String studentId = request.getStudentId();

            if (courseId == null || studentId == null || studentId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "课程ID和学生ID不能为空"));
            }

            Enrollment enrollment = enrollmentService.enrollStudent(courseId, studentId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(enrollment, "选课成功"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Enrollment>>> getAllEnrollments() {
        try {
            List<Enrollment> enrollments = enrollmentService.findAll();
            return ResponseEntity.ok(ApiResponse.success(enrollments, "选课成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            List<Enrollment> enrollments = enrollmentService.findByCourseId(courseId);
            return ResponseEntity.ok(ApiResponse.success(enrollments, "选课成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByStudent(@PathVariable String studentId) {
        try {
            List<Enrollment> enrollments = enrollmentService.findByStudentId(studentId);
            return ResponseEntity.ok(ApiResponse.success(enrollments, "选课成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id) {
        try {
            Optional<Enrollment> enrollment = enrollmentService.findById(id);
            if (enrollment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "选课记录不存在"));
            }

            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(null, "退课成功"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawCourse(@RequestBody EnrollmentRequest request) {
        try {
            Long courseId = request.getCourseId();
            String studentId = request.getStudentId();

            if (courseId == null || studentId == null || studentId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "课程ID和学生ID不能为空"));
            }

            boolean withdrawn = enrollmentService.withdrawByCourseAndStudent(courseId, studentId);
            if (withdrawn) {
                return ResponseEntity.ok(ApiResponse.success(null, "退课成功"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "选课记录不存在"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    // 内部请求类
    public static class EnrollmentRequest {
        private Long courseId;
        private String studentId;

        // Getters and Setters
        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
    }
}