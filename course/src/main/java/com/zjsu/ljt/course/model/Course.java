package com.zjsu.ljt.course.model;

public class Course {
    private String id;
    private String code;
    private String title;
    private Instructor instructor;
    private ScheduleSlot schedule;
    private int capacity;
    private int enrolled;

    public Course() {
        this.enrolled = 0;
    }

    public Course(String id, String code, String title, Instructor instructor,
                  ScheduleSlot schedule, int capacity) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.instructor = instructor;
        this.schedule = schedule;
        this.capacity = capacity;
        this.enrolled = 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    // 业务方法
    public boolean hasAvailableSeats() {
        return enrolled < capacity;
    }

    public void enrollStudent() {
        if (hasAvailableSeats()) {
            enrolled++;
        }
    }

    public void withdrawStudent() {
        if (enrolled > 0) {
            enrolled--;
        }
    }
}