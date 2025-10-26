package com.zjsu.ljt.course.repository;

import com.zjsu.ljt.course.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EnrollmentRepository {
    private final Map<String, Enrollment> enrollments = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> courseEnrollments = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> studentEnrollments = new ConcurrentHashMap<>();

    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }

    public Optional<Enrollment> findById(String id) {
        return Optional.ofNullable(enrollments.get(id));
    }

    public List<Enrollment> findByCourseId(String courseId) {
        Set<String> enrollmentIds = courseEnrollments.getOrDefault(courseId, new HashSet<>());
        return enrollmentIds.stream()
                .map(enrollments::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Enrollment> findByStudentId(String studentId) {
        Set<String> enrollmentIds = studentEnrollments.getOrDefault(studentId, new HashSet<>());
        return enrollmentIds.stream()
                .map(enrollments::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Optional<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId) {
        return enrollments.values().stream()
                .filter(e -> e.getCourseId().equals(courseId) && e.getStudentId().equals(studentId))
                .findFirst();
    }

    public Enrollment save(Enrollment enrollment) {
        // 确保 enrollment 有 ID
        if (enrollment.getId() == null || enrollment.getId().trim().isEmpty()) {
            enrollment.setId(UUID.randomUUID().toString());
        }
        if (enrollment.getEnrolledAt() == null) {
            enrollment.setEnrolledAt(LocalDateTime.now());
        }

        enrollments.put(enrollment.getId(), enrollment);

        // 维护课程-选课关系
        courseEnrollments.computeIfAbsent(enrollment.getCourseId(), k -> new HashSet<>())
                .add(enrollment.getId());

        // 维护学生-选课关系
        studentEnrollments.computeIfAbsent(enrollment.getStudentId(), k -> new HashSet<>())
                .add(enrollment.getId());

        return enrollment;
    }

    public void deleteById(String id) {
        Enrollment enrollment = enrollments.get(id);
        if (enrollment != null) {
            enrollments.remove(id);

            // 清理课程-选课关系
            Set<String> courseEnrollmentSet = courseEnrollments.get(enrollment.getCourseId());
            if (courseEnrollmentSet != null) {
                courseEnrollmentSet.remove(id);
                if (courseEnrollmentSet.isEmpty()) {
                    courseEnrollments.remove(enrollment.getCourseId());
                }
            }

            // 清理学生-选课关系
            Set<String> studentEnrollmentSet = studentEnrollments.get(enrollment.getStudentId());
            if (studentEnrollmentSet != null) {
                studentEnrollmentSet.remove(id);
                if (studentEnrollmentSet.isEmpty()) {
                    studentEnrollments.remove(enrollment.getStudentId());
                }
            }
        }
    }

    public boolean existsByCourseIdAndStudentId(String courseId, String studentId) {
        return findByCourseIdAndStudentId(courseId, studentId).isPresent();
    }

    public int countByCourseId(String courseId) {
        return courseEnrollments.getOrDefault(courseId, new HashSet<>()).size();
    }

    public int countByStudentId(String studentId) {
        return studentEnrollments.getOrDefault(studentId, new HashSet<>()).size();
    }

    // 便捷创建方法
    public Enrollment create(String courseId, String studentId) {
        Enrollment enrollment = new Enrollment(courseId, studentId);
        return save(enrollment);
    }
}