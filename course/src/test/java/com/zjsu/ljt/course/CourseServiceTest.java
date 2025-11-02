package com.zjsu.ljt.course;

import com.zjsu.ljt.course.model.Course;
import com.zjsu.ljt.course.model.Instructor;
import com.zjsu.ljt.course.model.ScheduleSlot;
import com.zjsu.ljt.course.repository.CourseRepository;
import com.zjsu.ljt.course.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setCode("CS101");
        testCourse.setTitle("计算机基础");
        testCourse.setInstructor(new Instructor("T001", "张教授", "zhang@example.com"));
        testCourse.setSchedule(new ScheduleSlot("周一", "08:00", "10:00", 50));
        testCourse.setCapacity(60);
        testCourse.setEnrolled(0);
    }

    @Test
    void testGetAllCourses() {
        // 准备数据
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findAll()).thenReturn(courses);

        // 执行测试
        List<Course> result = courseService.getAllCourses();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CS101", result.get(0).getCode());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCourseById() {
        // 准备数据
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // 执行测试
        Optional<Course> result = courseService.getCourseById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals("计算机基础", result.get().getTitle());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCourse() {
        // 准备数据
        when(courseRepository.existsByCode("CS101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // 执行测试
        Course result = courseService.createCourse(testCourse);

        // 验证结果
        assertNotNull(result);
        assertEquals("CS101", result.getCode());
        verify(courseRepository, times(1)).existsByCode("CS101");
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void testCreateCourseWithDuplicateCode() {
        // 准备数据
        when(courseRepository.existsByCode("CS101")).thenReturn(true);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.createCourse(testCourse);
        });

        assertEquals("课程代码已存在: CS101", exception.getMessage());
        verify(courseRepository, times(1)).existsByCode("CS101");
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testUpdateCourse() {
        // 准备数据
        Course updatedCourse = new Course();
        updatedCourse.setCode("CS101");
        updatedCourse.setTitle("计算机基础（更新版）");
        updatedCourse.setCapacity(80);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        //when(courseRepository.existsByCode("CS101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        // 执行测试
        Optional<Course> result = courseService.updateCourse(1L, updatedCourse);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals("计算机基础（更新版）", result.get().getTitle());
        assertEquals(80, result.get().getCapacity());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testDeleteCourse() {
        // 准备数据
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);

        // 执行测试
        boolean result = courseService.deleteCourse(1L);

        // 验证结果
        assertTrue(result);
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNonExistentCourse() {
        // 准备数据
        when(courseRepository.existsById(999L)).thenReturn(false);

        // 执行测试
        boolean result = courseService.deleteCourse(999L);

        // 验证结果
        assertFalse(result);
        verify(courseRepository, times(1)).existsById(999L);
        verify(courseRepository, never()).deleteById(anyLong());
    }
}