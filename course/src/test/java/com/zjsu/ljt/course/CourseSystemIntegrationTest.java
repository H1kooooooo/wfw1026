package com.zjsu.ljt.course;

import com.zjsu.ljt.course.model.Course;
import com.zjsu.ljt.course.model.Instructor;
import com.zjsu.ljt.course.model.ScheduleSlot;
import com.zjsu.ljt.course.model.Student;
import com.zjsu.ljt.course.repository.CourseRepository;
import com.zjsu.ljt.course.repository.StudentRepository;
import com.zjsu.ljt.course.service.CourseService;
import com.zjsu.ljt.course.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev") // 使用开发环境配置
class CourseSystemIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/course-api";
    }

    @Test
    void contextLoads() {
        // 测试应用上下文加载
        assertNotNull(restTemplate);
        assertNotNull(courseRepository);
        assertNotNull(studentRepository);
    }

    @Test
    void testDatabaseConnection() {
        // 测试数据库连接
        assertTrue(courseRepository.count() >= 0);
        assertTrue(studentRepository.count() >= 0);
        System.out.println("数据库连接测试通过，课程数量: " + courseRepository.count());
    }

    @Test
    void testHealthCheck() {
        // 测试健康检查接口
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/health/db", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        System.out.println("健康检查测试通过: " + response.getBody());
    }

    @Test
    void testCourseCRUD() {
        // 创建测试课程
        Course testCourse = new Course();
        testCourse.setCode("TEST001");
        testCourse.setTitle("集成测试课程");
        testCourse.setInstructor(new Instructor("T999", "测试教授", "test@example.com"));
        testCourse.setSchedule(new ScheduleSlot("周一", "10:00", "12:00", 30));
        testCourse.setCapacity(40);

        // 测试创建课程
        Course savedCourse = courseService.createCourse(testCourse);
   //     assertNotNull(savedCourse.getId());
    //    assertEquals("TEST001", savedCourse.getCode());
    //    System.out.println("课程创建测试通过，ID: " + savedCourse.getId());

        // 测试查询课程
        Course foundCourse = courseService.getCourseById(savedCourse.getId()).orElse(null);
        assertNotNull(foundCourse);
        assertEquals("集成测试课程", foundCourse.getTitle());

        // 测试更新课程
        foundCourse.setTitle("更新后的测试课程");
        Course updatedCourse = courseService.updateCourse(foundCourse.getId(), foundCourse).orElse(null);
        assertNotNull(updatedCourse);
        assertEquals("更新后的测试课程", updatedCourse.getTitle());

        // 测试删除课程
        boolean deleted = courseService.deleteCourse(updatedCourse.getId());
        assertTrue(deleted);

        // 验证课程已删除
        assertFalse(courseService.getCourseById(updatedCourse.getId()).isPresent());
        System.out.println("课程CRUD测试全部通过");
    }

    @Test
    void testStudentCRUD() {
        // 创建测试学生
        Student testStudent = new Student();
        testStudent.setStudentId("2024999999");
        testStudent.setName("集成测试学生");
        testStudent.setMajor("计算机科学");
        testStudent.setGrade(2024);
        testStudent.setEmail("integration_test@example.com");

        // 测试创建学生
        Student savedStudent = studentService.createStudent(testStudent);
        assertNotNull(savedStudent.getId());
        assertEquals("2024999999", savedStudent.getStudentId());
        System.out.println("学生创建测试通过，ID: " + savedStudent.getId());

        // 测试查询学生
        Student foundStudent = studentService.getStudentById(savedStudent.getId()).orElse(null);
        assertNotNull(foundStudent);
        assertEquals("集成测试学生", foundStudent.getName());

        // 测试更新学生
        foundStudent.setName("更新后的测试学生");
        Student updatedStudent = studentService.updateStudent(foundStudent.getId(), foundStudent).orElse(null);
        assertNotNull(updatedStudent);
        assertEquals("更新后的测试学生", updatedStudent.getName());

        // 测试删除学生
        boolean deleted = studentService.deleteStudent(updatedStudent.getId());
        assertTrue(deleted);

        // 验证学生已删除
        assertFalse(studentService.getStudentById(updatedStudent.getId()).isPresent());
        System.out.println("学生CRUD测试全部通过");
    }

    @Test
    void testCourseCodeUniqueConstraint() {
        // 测试课程代码唯一约束
        Course course1 = new Course();
        course1.setCode("UNIQUE001");
        course1.setTitle("唯一约束测试课程1");
        course1.setInstructor(new Instructor("T001", "教授1", "prof1@example.com"));
        course1.setSchedule(new ScheduleSlot("周二", "08:00", "10:00", 20));
        course1.setCapacity(30);

        Course savedCourse1 = courseService.createCourse(course1);
        assertNotNull(savedCourse1);

        // 尝试创建相同课程代码的课程
        Course course2 = new Course();
        course2.setCode("UNIQUE001"); // 相同的课程代码
        course2.setTitle("唯一约束测试课程2");
        course2.setInstructor(new Instructor("T002", "教授2", "prof2@example.com"));
        course2.setSchedule(new ScheduleSlot("周三", "10:00", "12:00", 25));
        course2.setCapacity(35);

        try {
            courseService.createCourse(course2);
            fail("应该抛出重复课程代码的异常");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("课程代码已存在"));
            System.out.println("课程代码唯一约束测试通过: " + e.getMessage());
        }

        // 清理测试数据
        courseService.deleteCourse(savedCourse1.getId());
    }

    @Test
    void testStudentIdUniqueConstraint() {
        // 测试学号唯一约束
        Student student1 = new Student();
        student1.setStudentId("UNIQUE2024");
        student1.setName("唯一约束测试学生1");
        student1.setMajor("软件工程");
        student1.setGrade(2024);
        student1.setEmail("unique1@example.com");

        Student savedStudent1 = studentService.createStudent(student1);
        assertNotNull(savedStudent1);

        // 尝试创建相同学号的学生
        Student student2 = new Student();
        student2.setStudentId("UNIQUE2024"); // 相同的学号
        student2.setName("唯一约束测试学生2");
        student2.setMajor("数据科学");
        student2.setGrade(2024);
        student2.setEmail("unique2@example.com");

        try {
            studentService.createStudent(student2);
            fail("应该抛出重复学号的异常");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("学号已存在"));
            System.out.println("学号唯一约束测试通过: " + e.getMessage());
        }

        // 清理测试数据
        studentService.deleteStudent(savedStudent1.getId());
    }
}