package com.zjsu.ljt.course.service;

import com.zjsu.ljt.course.model.Course;
import com.zjsu.ljt.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> updateCourse(String id, Course courseDetails) {
        return courseRepository.findById(id).map(existingCourse -> {
            existingCourse.setCode(courseDetails.getCode());
            existingCourse.setTitle(courseDetails.getTitle());
            existingCourse.setInstructor(courseDetails.getInstructor());
            existingCourse.setSchedule(courseDetails.getSchedule());
            existingCourse.setCapacity(courseDetails.getCapacity());
            return courseRepository.save(existingCourse);
        });
    }

    public boolean deleteCourse(String id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsById(String id) {
        return courseRepository.existsById(id);
    }
}