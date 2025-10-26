校园选课系统 - 单体版

项目简介

这是一个基于 Spring Boot 的单体架构校园选课与教学资源管理平台。项目实现了课程管理、学生管理和选课管理等核心功能，为后续微服务架构重构打下基础。

技术栈

• 框架: Spring Boot 3.5.6

• 语言: Java 17

• 构建工具: Maven

• 数据存储: 内存存储（ConcurrentHashMap）

• API风格: RESTful

功能特性

核心功能模块

1. 课程管理
   • 课程信息的增删改查

   • 课程容量管理

   • 教师信息管理

2. 学生管理
   • 学生信息的增删改查

   • 学号唯一性验证

   • 邮箱格式验证

3. 选课管理
   • 学生选课/退课

   • 课程容量限制

   • 重复选课检查

   • 选课记录查询

业务规则

• ✅ 课程容量限制检查

• ✅ 重复选课检查

• ✅ 课程/学生存在性验证

• ✅ 级联更新选课人数

项目结构


src/main/java/com/zjsu/ljt/course/
├── CourseApplication.java          # 启动类
├── model/                          # 实体类
│   ├── Course.java
│   ├── Instructor.java
│   ├── ScheduleSlot.java
│   ├── Student.java
│   └── Enrollment.java
├── repository/                     # 数据访问层
│   ├── CourseRepository.java
│   ├── StudentRepository.java
│   └── EnrollmentRepository.java
├── service/                        # 业务逻辑层
│   ├── CourseService.java
│   ├── StudentService.java
│   └── EnrollmentService.java
└── controller/                     # 控制器层
    ├── CourseController.java
    ├── StudentController.java
    └── EnrollmentController.java


快速开始

环境要求

• JDK 17+

• Maven 3.6+

• IntelliJ IDEA 

运行步骤

1. 克隆项目
   git clone <项目地址>
   cd course
   

2. 编译项目
   mvn clean compile
   

3. 运行应用
   mvn spring-boot:run
   
   或直接在 IDE 中运行 CourseApplication.java

4. 访问应用
   • 应用启动后访问: http://localhost:8080

   • 默认端口: 8080

API 接口文档

课程管理接口

方法 端点 描述 状态码

GET /api/courses 查询所有课程 200

GET /api/courses/{id} 根据ID查询课程 200/404

POST /api/courses 创建课程 201

PUT /api/courses/{id} 更新课程信息 200

DELETE /api/courses/{id} 删除课程 200

创建课程示例:
POST /api/courses
{
  "code": "CS101",
  "title": "计算机科学导论",
  "instructor": {
    "id": "T001",
    "name": "张教授",
    "email": "zhang@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "MONDAY",
    "startTime": "08:00",
    "endTime": "10:00",
    "expectedAttendance": 50
  },
  "capacity": 60
}


学生管理接口

方法 端点 描述 状态码

GET /api/students 查询所有学生 200

GET /api/students/{id} 根据ID查询学生 200/404

POST /api/students 创建学生 201

PUT /api/students/{id} 更新学生信息 200

DELETE /api/students/{id} 删除学生 200

创建学生示例:
POST /api/students
{
  "studentId": "S2024001",
  "name": "张三",
  "major": "计算机科学与技术",
  "grade": 2024,
  "email": "zhangsan@example.edu.cn"
}


选课管理接口

方法 端点 描述 状态码

GET /api/enrollments 查询所有选课记录 200

POST /api/enrollments 学生选课 201

DELETE /api/enrollments/{id} 学生退课 200

GET /api/enrollments/course/{courseId} 按课程查询选课 200

GET /api/enrollments/student/{studentId} 按学生查询选课 200

选课示例:
POST /api/enrollments
{
  "courseId": "course-001",
  "studentId": "S001"
}


测试指南

测试场景

1. 课程管理流程 - 完整的CRUD操作测试
2. 选课业务流程 - 容量限制和重复选课测试
3. 学生管理流程 - 学生信息管理和删除限制测试
4. 错误处理 - 各种异常情况测试

测试工具

• Postman: API接口测试

• curl: 命令行测试

• 浏览器: 简单GET请求测试

测试命令示例

# 查询所有课程
curl -X GET http://localhost:8080/api/courses

# 创建课程
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"code":"CS101","title":"计算机科学导论","capacity":60}'

# 学生选课
curl -X POST http://localhost:8080/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{"courseId":"course-001","studentId":"S001"}'


响应格式

成功响应

{
  "code": 200,
  "message": "Success",
  "data": { ... }
}


错误响应

{
  "code": 404,
  "message": "课程不存在",
  "data": null
}


配置说明

应用配置

• 端口: 8080

• 数据存储: 内存存储，应用重启后数据重置

Maven依赖

主要依赖项：
• spring-boot-starter-web - Web功能支持

• spring-boot-starter-validation - 数据验证

• spring-boot-starter-test - 测试支持

开发说明

数据模型

• Course: 课程信息，包含教师和排课信息

• Student: 学生信息，包含学号和专业信息

• Enrollment: 选课记录，关联课程和学生

业务规则实现

1. 容量检查: 在选课时验证课程容量
2. 重复选课: 检查学生是否已选同一课程
3. 存在性验证: 操作前验证课程/学生是否存在
4. 删除保护: 有选课记录的学生不能被删除



版本信息

• 版本: 0.0.1-SNAPSHOT

• Spring Boot: 3.5.6

• Java: 17
