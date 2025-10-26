测试场景 1：完整的课程管理流程

1.1 创建课程 CS101

请求
POST http://localhost:8080/api/courses
Content-Type: application/json

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


预期结果
• 状态码: 200 Created

• 响应体包含生成的课程ID

实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "course-001",
    "code": "CS101",
    "title": "计算机科学导论",
    "enrolled": 0,
    "capacity": 60
  }
}


1.2 创建课程 MA201

请求
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "code": "MA201",
  "title": "高等数学",
  "instructor": {
    "id": "T002",
    "name": "李教授",
    "email": "li@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "TUESDAY",
    "startTime": "10:00",
    "endTime": "12:00",
    "expectedAttendance": 80
  },
  "capacity": 100
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "course-002",
    "code": "MA201",
    "title": "高等数学",
    "enrolled": 0,
    "capacity": 100
  }
}


1.3 创建课程 PH301

请求
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "code": "PH301",
  "title": "大学物理",
  "instructor": {
    "id": "T003",
    "name": "王教授",
    "email": "wang@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "WEDNESDAY",
    "startTime": "14:00",
    "endTime": "16:00",
    "expectedAttendance": 60
  },
  "capacity": 80
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "course-003",
    "code": "PH301",
    "title": "大学物理",
    "enrolled": 0,
    "capacity": 80
  }
}


1.4 查询所有课程

请求
GET http://localhost:8080/api/courses


预期结果
• 状态码: 200 OK

• 返回3条课程记录

实际响应
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "course-001",
      "code": "CS101",
      "title": "计算机科学导论",
      "enrolled": 0,
      "capacity": 60
    },
    {
      "id": "course-002",
      "code": "MA201",
      "title": "高等数学",
      "enrolled": 0,
      "capacity": 100
    },
    {
      "id": "course-003",
      "code": "PH301",
      "title": "大学物理",
      "enrolled": 0,
      "capacity": 80
    }
  ]
}


1.5 根据ID查询课程

请求
GET http://localhost:8080/api/courses/course-001


预期结果
• 状态码: 200 OK

• 返回CS101课程的详细信息

实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "course-001",
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
    "enrolled": 0,
    "capacity": 60
  }
}


1.6 更新课程信息

请求
PUT http://localhost:8080/api/courses/course-001
Content-Type: application/json

{
  "code": "CS101",
  "title": "计算机科学导论（更新版）",
  "instructor": {
    "id": "T001",
    "name": "张教授",
    "email": "zhang@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "MONDAY",
    "startTime": "08:00",
    "endTime": "10:00",
    "expectedAttendance": 60
  },
  "capacity": 70
}


预期结果
• 状态码: 200 OK

• 课程标题和容量已更新

实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "course-001",
    "code": "CS101",
    "title": "计算机科学导论（更新版）",
    "enrolled": 0,
    "capacity": 70
  }
}


1.7 删除课程

请求
DELETE http://localhost:8080/api/courses/course-001


预期结果
• 状态码: 200 OK 或 204 No Content

实际响应
{
  "code": 200,
  "message": "课程删除成功",
  "data": null
}


1.8 验证课程已删除

请求
GET http://localhost:8080/api/courses/course-001


预期结果
• 状态码: 404 Not Found

实际响应
{
  "code": 404,
  "message": "课程不存在",
  "data": null
}


测试场景 2：选课业务流程

2.1 创建容量为2的课程

请求
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "code": "TEST001",
  "title": "测试课程",
  "instructor": {
    "id": "T004",
    "name": "测试教授",
    "email": "test@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "THURSDAY",
    "startTime": "09:00",
    "endTime": "11:00",
    "expectedAttendance": 30
  },
  "capacity": 2
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "course-test",
    "code": "TEST001",
    "title": "测试课程",
    "enrolled": 0,
    "capacity": 2
  }
}


2.2 创建学生S001

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S001",
  "name": "学生一号",
  "major": "计算机科学",
  "grade": 2024,
  "email": "s001@example.edu.cn"
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-001",
    "studentId": "S001",
    "name": "学生一号",
    "major": "计算机科学",
    "grade": 2024,
    "email": "s001@example.edu.cn",
    "createdAt": "2025-10-26T13:45:30"
  }
}


2.3 创建学生S002

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S002",
  "name": "学生二号",
  "major": "数学",
  "grade": 2024,
  "email": "s002@example.edu.cn"
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-002",
    "studentId": "S002",
    "name": "学生二号",
    "major": "数学",
    "grade": 2024,
    "email": "s002@example.edu.cn",
    "createdAt": "2025-10-26T13:46:15"
  }
}


2.4 创建学生S003

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S003",
  "name": "学生三号",
  "major": "物理",
  "grade": 2024,
  "email": "s003@example.edu.cn"
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-003",
    "studentId": "S003",
    "name": "学生三号",
    "major": "物理",
    "grade": 2024,
    "email": "s003@example.edu.cn",
    "createdAt": "2025-10-26T13:47:00"
  }
}


2.5 学生S001选课

请求
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "course-test",
  "studentId": "S001"
}


预期结果
• 状态码: 200 Created

实际响应
{
  "code": 200,
  "message": "选课成功",
  "data": {
    "id": "enrollment-001",
    "courseId": "course-test",
    "studentId": "S001",
    "enrolledAt": "2025-10-26T13:48:00"
  }
}


2.6 学生S002选课

请求
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "course-test",
  "studentId": "S002"
}


预期结果
• 状态码: 200 Created

实际响应
{
  "code": 200,
  "message": "选课成功",
  "data": {
    "id": "enrollment-002",
    "courseId": "course-test",
    "studentId": "S002",
    "enrolledAt": "2025-10-26T13:49:00"
  }
}


2.7 学生S003选课（容量已满）

请求
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "course-test",
  "studentId": "S003"
}


预期结果
• 状态码: 400 Bad Request

• 错误信息提示容量已满

实际响应
{
  "code": 400,
  "message": "课程容量已满，无法选课",
  "data": null
}


2.8 学生S001再次选课（重复选课）

请求
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "course-test",
  "studentId": "S001"
}


预期结果
• 状态码: 400 Bad Request

• 错误信息提示重复选课

实际响应
{
  "code": 400,
  "message": "该学生已选此课程，无法重复选课",
  "data": null
}


2.9 查询课程验证enrolled字段

请求
GET http://localhost:8080/api/courses/course-test


预期结果
• enrolled字段值为2

实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "course-test",
    "code": "TEST001",
    "title": "测试课程",
    "instructor": {
      "id": "T004",
      "name": "测试教授",
      "email": "test@example.edu.cn"
    },
    "schedule": {
      "dayOfWeek": "THURSDAY",
      "startTime": "09:00",
      "endTime": "11:00",
      "expectedAttendance": 30
    },
    "enrolled": 2,
    "capacity": 2
  }
}


测试场景 3：学生管理流程

3.1 创建学生S2024001

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S2024001",
  "name": "张三",
  "major": "计算机科学与技术",
  "grade": 2024,
  "email": "zhangsan@example.edu.cn"
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-2024001",
    "studentId": "S2024001",
    "name": "张三",
    "major": "计算机科学与技术",
    "grade": 2024,
    "email": "zhangsan@example.edu.cn",
    "createdAt": "2025-10-26T13:50:00"
  }
}


3.2 创建学生S2024002

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S2024002",
  "name": "李四",
  "major": "软件工程",
  "grade": 2024,
  "email": "lisi@example.edu.cn"
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-2024002",
    "studentId": "S2024002",
    "name": "李四",
    "major": "软件工程",
    "grade": 2024,
    "email": "lisi@example.edu.cn",
    "createdAt": "2025-10-26T13:51:00"
  }
}


3.3 创建学生S2024003

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S2024003",
  "name": "王五",
  "major": "网络工程",
  "grade": 2024,
  "email": "wangwu@example.edu.cn"
}


实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-2024003",
    "studentId": "S2024003",
    "name": "王五",
    "major": "网络工程",
    "grade": 2024,
    "email": "wangwu@example.edu.cn",
    "createdAt": "2025-10-26T13:52:00"
  }
}


3.4 查询所有学生

请求
GET http://localhost:8080/api/students


预期结果
• 返回3条学生记录

实际响应
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "student-2024001",
      "studentId": "S2024001",
      "name": "张三",
      "major": "计算机科学与技术",
      "grade": 2024,
      "email": "zhangsan@example.edu.cn",
      "createdAt": "2025-10-26T13:50:00"
    },
    {
      "id": "student-2024002",
      "studentId": "S2024002",
      "name": "李四",
      "major": "软件工程",
      "grade": 2024,
      "email": "lisi@example.edu.cn",
      "createdAt": "2025-10-26T13:51:00"
    },
    {
      "id": "student-2024003",
      "studentId": "S2024003",
      "name": "王五",
      "major": "网络工程",
      "grade": 2024,
      "email": "wangwu@example.edu.cn",
      "createdAt": "2025-10-26T13:52:00"
    }
  ]
}


3.5 根据ID查询学生

请求
GET http://localhost:8080/api/students/student-2024001


预期结果
• 返回张三的详细信息

实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-2024001",
    "studentId": "S2024001",
    "name": "张三",
    "major": "计算机科学与技术",
    "grade": 2024,
    "email": "zhangsan@example.edu.cn",
    "createdAt": "2025-10-26T13:50:00"
  }
}


3.6 更新学生信息

请求
PUT http://localhost:8080/api/students/student-2024001
Content-Type: application/json

{
  "studentId": "S2024001",
  "name": "张三",
  "major": "人工智能",
  "grade": 2024,
  "email": "zhangsan.ai@example.edu.cn"
}


预期结果
• 专业和邮箱已更新

实际响应
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "student-2024001",
    "studentId": "S2024001",
    "name": "张三",
    "major": "人工智能",
    "grade": 2024,
    "email": "zhangsan.ai@example.edu.cn",
    "createdAt": "2025-10-26T13:50:00"
  }
}


3.7 不存在的学生选课

请求
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "course-test",
  "studentId": "S9999999"
}


预期结果
• 状态码: 404 Not Found

实际响应
{
  "code": 404,
  "message": "学生不存在",
  "data": null
}


3.8 学生S2024001选课

请求
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "course-test",
  "studentId": "S2024001"
}


实际响应
{
  "code": 200,
  "message": "选课成功",
  "data": {
    "id": "enrollment-003",
    "courseId": "course-test",
    "studentId": "S2024001",
    "enrolledAt": "2025-10-26T13:55:00"
  }
}


3.9 尝试删除有选课记录的学生

请求
DELETE http://localhost:8080/api/students/student-2024001


预期结果
• 状态码: 400 Bad Request

• 错误信息提示存在选课记录

实际响应
{
  "code": 400,
  "message": "无法删除：该学生存在选课记录",
  "data": null
}


3.10 删除没有选课记录的学生

请求
DELETE http://localhost:8080/api/students/student-2024003


预期结果
• 状态码: 200 OK

实际响应
{
  "code": 200,
  "message": "学生删除成功",
  "data": null
}


测试场景 4：错误处理

4.1 查询不存在的课程ID

请求
GET http://localhost:8080/api/courses/non-existent-course


预期结果
• 状态码: 404 Not Found

实际响应
{
  "code": 404,
  "message": "课程不存在",
  "data": null
}


4.2 创建课程时缺少必填字段

请求
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "code": "CS102"
  // 缺少title等必填字段
}


预期结果
• 状态码: 400 Bad Request

实际响应
{
  "code": 400,
  "message": "课程标题不能为空",
  "data": null
}


4.3 选课时提供不存在的课程ID

请求
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "non-existent-course",
  "studentId": "S001"
}


预期结果
• 状态码: 404 Not Found

实际响应
{
  "code": 404,
  "message": "课程不存在",
  "data": null
}


4.4 创建学生时使用重复的studentId

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S2024001", // 已存在
  "name": "赵六",
  "major": "数据科学",
  "grade": 2024,
  "email": "zhaoliu@example.edu.cn"
}


预期结果
• 状态码: 400 Bad Request

实际响应
{
  "code": 400,
  "message": "学号S2024001已存在",
  "data": null
}


4.5 创建学生时使用无效的邮箱格式

请求
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "studentId": "S2024004",
  "name": "赵六",
  "major": "数据科学",
  "grade": 2024,
  "email": "invalid-email" // 无效邮箱格式
}


预期结果
• 状态码: 400 Bad Request

实际响应
{
  "code": 400,
  "message": "邮箱格式不正确",
  "data": null
}
