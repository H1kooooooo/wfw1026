package com.zjsu.ljt.course.repository;

import com.zjsu.ljt.course.model.Student;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StudentRepository {
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, Student> studentsByStudentId = new ConcurrentHashMap<>();

    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    public Optional<Student> findByStudentId(String studentId) {
        return students.values().stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
    }

    public Student save(Student student) {
        students.put(student.getId(), student);
        studentsByStudentId.put(student.getStudentId(), student);
        return student;
    }

    public void deleteById(String id) {
        Student student = students.get(id);
        if (student != null) {
            studentsByStudentId.remove(student.getStudentId());
            students.remove(id);
        }
    }

    public boolean existsById(String id) {
        return students.containsKey(id);
    }

    public boolean existsByStudentId(String studentId) {
        return studentsByStudentId.containsKey(studentId);
    }
}