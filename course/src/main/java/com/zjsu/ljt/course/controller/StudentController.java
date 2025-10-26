package com.zjsu.ljt.course.controller;

import com.zjsu.ljt.course.dto.ApiResponse;
import com.zjsu.ljt.course.model.Student;
import com.zjsu.ljt.course.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(ApiResponse.success(students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable String id) {
        try {
            Optional<Student> student = studentService.getStudentById(id);
            if (student.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(student.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "学生不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<ApiResponse<Student>> getStudentByStudentId(@PathVariable String studentId) {
        try {
            Optional<Student> student = studentService.getStudentByStudentId(studentId);
            if (student.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(student.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "学生不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Student>> createStudent(@RequestBody Student student) {
        try {
            // 验证必填字段
            if (student.getStudentId() == null || student.getName() == null ||
                    student.getMajor() == null || student.getGrade() == null || student.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(400, "所有字段都是必填的"));
            }

            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> updateStudent(@PathVariable String id, @RequestBody Student studentDetails) {
        try {
            // 验证必填字段
            if (studentDetails.getStudentId() == null || studentDetails.getName() == null ||
                    studentDetails.getMajor() == null || studentDetails.getGrade() == null || studentDetails.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(400, "所有字段都是必填的"));
            }

            Optional<Student> updatedStudent = studentService.updateStudent(id, studentDetails);
            if (updatedStudent.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(updatedStudent.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "学生不存在"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        try {
            boolean deleted = studentService.deleteStudent(id);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ApiResponse.success(null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "学生不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, e.getMessage()));
        }
    }
}