package com.zjsu.ljt.course.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "instructor_id", length = 50)),
            @AttributeOverride(name = "name", column = @Column(name = "instructor_name", length = 100)),
            @AttributeOverride(name = "email", column = @Column(name = "instructor_email", length = 100))
    })
    private Instructor instructor;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dayOfWeek", column = @Column(name = "day_of_week", length = 20)),
            @AttributeOverride(name = "startTime", column = @Column(name = "start_time", length = 20)),
            @AttributeOverride(name = "endTime", column = @Column(name = "end_time", length = 20)),
            @AttributeOverride(name = "expectedAttendance", column = @Column(name = "expected_attendance"))
    })
    private ScheduleSlot schedule;

    @Column(nullable = false)
    private int capacity;

    private int enrolled = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Course() {}

    public Course(String code, String title, Instructor instructor,
                  ScheduleSlot schedule, int capacity) {
        this.code = code;
        this.title = title;
        this.instructor = instructor;
        this.schedule = schedule;
        this.capacity = capacity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public ScheduleSlot getSchedule() { return schedule; }
    public void setSchedule(ScheduleSlot schedule) { this.schedule = schedule; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getEnrolled() { return enrolled; }
    public void setEnrolled(int enrolled) { this.enrolled = enrolled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 业务方法
    public boolean hasAvailableSeats() {
        return enrolled < capacity;
    }

    public void enrollStudent() {
        if (hasAvailableSeats()) {
            enrolled++;
        } else {
            throw new RuntimeException("课程容量已满");
        }
    }

    public void withdrawStudent() {
        if (enrolled > 0) {
            enrolled--;
        }
    }
}