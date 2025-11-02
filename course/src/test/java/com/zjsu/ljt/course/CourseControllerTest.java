package com.zjsu.ljt.course;


import com.zjsu.ljt.course.controller.CourseController;
import com.zjsu.ljt.course.model.Course;
import com.zjsu.ljt.course.model.Instructor;
import com.zjsu.ljt.course.model.ScheduleSlot;
import com.zjsu.ljt.course.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    void testGetAllCourses() throws Exception {
        // 准备数据
        Course course = new Course();
        course.setId(1L);
        course.setCode("CS101");
        course.setTitle("计算机基础");

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course));

        // 执行测试并验证
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("选课成功"))
                .andExpect(jsonPath("$.data[0].code").value("CS101"));

        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void testGetCourseById() throws Exception {
        // 准备数据
        Course course = new Course();
        course.setId(1L);
        course.setCode("CS101");
        course.setTitle("计算机基础");

        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));

        // 执行测试并验证
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.code").value("CS101"));

        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    void testGetCourseByIdNotFound() throws Exception {
        // 准备数据
        when(courseService.getCourseById(999L)).thenReturn(Optional.empty());

        // 执行测试并验证
        mockMvc.perform(get("/api/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("课程不存在"));

        verify(courseService, times(1)).getCourseById(999L);
    }

    @Test
    void testCreateCourse() throws Exception {
        // 准备数据
        Course course = new Course();
        course.setId(1L);
        course.setCode("NEW001");
        course.setTitle("新课程");

        when(courseService.createCourse(any(Course.class))).thenReturn(course);

        // 执行测试并验证
        String courseJson = """
        {
            "code": "NEW001",
            "title": "新课程",
            "instructor": {
                "id": "T001",
                "name": "测试教授",
                "email": "test@example.com"
            },
            "schedule": {
                "dayOfWeek": "周一",
                "startTime": "08:00",
                "endTime": "10:00",
                "expectedAttendance": 30
            },
            "capacity": 40
        }
        """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.code").value("NEW001"));

        verify(courseService, times(1)).createCourse(any(Course.class));
    }
}