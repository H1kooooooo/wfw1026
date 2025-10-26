package com.zjsu.ljt.course.service;

import com.zjsu.ljt.course.model.Course;
import com.zjsu.ljt.course.model.Enrollment;
import com.zjsu.ljt.course.model.Student;
import com.zjsu.ljt.course.repository.CourseRepository;
import com.zjsu.ljt.course.repository.EnrollmentRepository;
import com.zjsu.ljt.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;
    public boolean withdrawByCourseAndStudent(String courseId, String studentId) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId);
        if (enrollmentOpt.isPresent()) {
            enrollmentRepository.deleteById(enrollmentOpt.get().getId());
            return true;
        }
        return false;
    }
    public Enrollment enrollStudent(String courseId, String studentId) {
        // 1. 检查课程是否存在
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("课程不存在: " + courseId);
        }

        // 2. 检查学生是否存在
        Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("学生不存在: " + studentId);
        }

        // 3. 检查是否已选课
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new RuntimeException("该学生已选此课程");
        }

        // 4. 检查课程容量
        Course course = courseOpt.get();
        int currentEnrollment = enrollmentRepository.countByCourseId(courseId);
        if (currentEnrollment >= course.getCapacity()) {
            throw new RuntimeException("课程容量已满");
        }

        // 5. 创建选课记录
        return enrollmentRepository.create(courseId, studentId);
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> findByCourseId(String courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> findByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public void deleteEnrollment(String enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }

    public Optional<Enrollment> findById(String id) {
        return enrollmentRepository.findById(id);
    }
}