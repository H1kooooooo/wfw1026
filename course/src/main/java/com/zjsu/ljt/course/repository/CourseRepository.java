package com.zjsu.ljt.course.repository;

import com.zjsu.ljt.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

    List<Course> findByInstructorNameContaining(String instructorName);

    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword%")
    List<Course> findByTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT c FROM Course c WHERE c.enrolled < c.capacity")
    List<Course> findCoursesWithAvailableSeats();

    boolean existsByCode(String code);
}