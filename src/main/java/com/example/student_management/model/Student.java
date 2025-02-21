package com.example.student_management.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "students") // Ensure this matches your InfluxDB measurement name
public class Student {

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Column(timestamp = true)
    private Instant time = Instant.now(); // Required for InfluxDB time-series storage

    @Column(tag = true) // Tags are indexed and used for filtering
    private String sID = UUID.randomUUID().toString(); // Auto-generate student ID

    @Column
    private String name;

    @Column
    private int age;
}
