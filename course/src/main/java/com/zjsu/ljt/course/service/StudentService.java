package com.zjsu.ljt.course.service;

import com.zjsu.ljt.course.model.Student;
import com.zjsu.ljt.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    @Transactional
    public Student createStudent(Student student) {
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("学号已存在: " + student.getStudentId());
        }

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("邮箱已存在: " + student.getEmail());
        }

        // 验证邮箱格式
        if (!isValidEmail(student.getEmail())) {
            throw new RuntimeException("邮箱格式不正确: " + student.getEmail());
        }

        return studentRepository.save(student);
    }

    @Transactional
    public Optional<Student> updateStudent(Long id, Student studentDetails) {
        return studentRepository.findById(id).map(existingStudent -> {
            // 检查学号是否重复（排除自己）
            if (!existingStudent.getStudentId().equals(studentDetails.getStudentId()) &&
                    studentRepository.existsByStudentId(studentDetails.getStudentId())) {
                throw new RuntimeException("学号已存在: " + studentDetails.getStudentId());
            }

            // 检查邮箱是否重复（排除自己）
            if (!existingStudent.getEmail().equals(studentDetails.getEmail()) &&
                    studentRepository.existsByEmail(studentDetails.getEmail())) {
                throw new RuntimeException("邮箱已存在: " + studentDetails.getEmail());
            }

            // 验证邮箱格式
            if (!isValidEmail(studentDetails.getEmail())) {
                throw new RuntimeException("邮箱格式不正确: " + studentDetails.getEmail());
            }

            existingStudent.setStudentId(studentDetails.getStudentId());
            existingStudent.setName(studentDetails.getName());
            existingStudent.setMajor(studentDetails.getMajor());
            existingStudent.setGrade(studentDetails.getGrade());
            existingStudent.setEmail(studentDetails.getEmail());

            return studentRepository.save(existingStudent);
        });
    }

    @Transactional
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    public List<Student> getStudentsByGrade(Integer grade) {
        return studentRepository.findByGrade(grade);
    }

    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}