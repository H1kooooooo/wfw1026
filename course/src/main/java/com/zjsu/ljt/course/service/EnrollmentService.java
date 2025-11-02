package com.zjsu.ljt.course.service;

import com.zjsu.ljt.course.model.Course;
import com.zjsu.ljt.course.model.Enrollment;
import com.zjsu.ljt.course.model.EnrollmentStatus;
import com.zjsu.ljt.course.model.Student;
import com.zjsu.ljt.course.repository.CourseRepository;
import com.zjsu.ljt.course.repository.EnrollmentRepository;
import com.zjsu.ljt.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public Enrollment enrollStudent(Long courseId, String studentId) {
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
        if (enrollmentRepository.existsByCourseIdAndStudentIdAndStatus(courseId, studentId, EnrollmentStatus.ACTIVE)) {
            throw new RuntimeException("该学生已选此课程");
        }

        // 4. 检查课程容量
        Course course = courseOpt.get();
        int currentEnrollment = enrollmentRepository.countActiveEnrollmentsByCourseId(courseId);
        if (currentEnrollment >= course.getCapacity()) {
            throw new RuntimeException("课程容量已满");
        }

        // 5. 创建选课记录
        Enrollment enrollment = new Enrollment(courseId, studentId);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public boolean withdrawByCourseAndStudent(Long courseId, String studentId) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId);
        if (enrollmentOpt.isPresent()) {
            Enrollment enrollment = enrollmentOpt.get();
            enrollment.setStatus(EnrollmentStatus.DROPPED);
            enrollmentRepository.save(enrollment);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteEnrollment(Long enrollmentId) {
        if (enrollmentRepository.existsById(enrollmentId)) {
            enrollmentRepository.deleteById(enrollmentId);
            return true;
        }
        return false;
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> findByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> findByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> findActiveEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findActiveEnrollmentsByCourseId(courseId);
    }

    public List<Enrollment> findActiveEnrollmentsByStudentId(String studentId) {
        return enrollmentRepository.findActiveEnrollmentsByStudentId(studentId);
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepository.findById(id);
    }

    public int countActiveEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.countActiveEnrollmentsByCourseId(courseId);
    }
}