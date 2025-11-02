package com.zjsu.ljt.course.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "欢迎使用校园选课系统！API文档：\n" +
                "课程管理: GET/POST/PUT/DELETE /api/courses\n" +
                "学生管理: GET/POST/PUT/DELETE /api/students\n" +
                "选课管理: GET/POST/DELETE /api/enrollments\n" +
                "健康检查: GET /health/db, /health/readiness, /health/liveness\n" +
                "当前时间: " + java.time.LocalDateTime.now();
    }

    @GetMapping("/course-api")
    public String courseApiHome() {
        return "校园选课系统API服务运行中！\n" +
                "请使用以下端点：\n" +
                "- 课程管理: /api/courses\n" +
                "- 学生管理: /api/students\n" +
                "- 选课管理: /api/enrollments\n" +
                "- 健康检查: /health/db\n" +
                "服务器时间: " + java.time.LocalDateTime.now();
    }
}