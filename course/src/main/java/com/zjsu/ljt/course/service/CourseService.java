package com.zjsu.ljt.course.service;

import com.zjsu.ljt.course.model.Course;
import com.zjsu.ljt.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByCode(String code) {
        return courseRepository.findByCode(code);
    }

    @Transactional
    public Course createCourse(Course course) {
        if (courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("课程代码已存在: " + course.getCode());
        }
        return courseRepository.save(course);
    }

    @Transactional
    public Optional<Course> updateCourse(Long id, Course courseDetails) {
        return courseRepository.findById(id).map(existingCourse -> {
            // 检查课程代码是否重复（排除自己）
            if (!existingCourse.getCode().equals(courseDetails.getCode()) &&
                    courseRepository.existsByCode(courseDetails.getCode())) {
                throw new RuntimeException("课程代码已存在: " + courseDetails.getCode());
            }

            existingCourse.setCode(courseDetails.getCode());
            existingCourse.setTitle(courseDetails.getTitle());
            existingCourse.setInstructor(courseDetails.getInstructor());
            existingCourse.setSchedule(courseDetails.getSchedule());
            existingCourse.setCapacity(courseDetails.getCapacity());
            return courseRepository.save(existingCourse);
        });
    }

    @Transactional
    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Course> findCoursesWithAvailableSeats() {
        return courseRepository.findCoursesWithAvailableSeats();
    }

    public List<Course> findByTitleContaining(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }

    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }
}