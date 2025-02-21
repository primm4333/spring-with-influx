package com.example.student_management.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxTable;
import com.influxdb.client.QueryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.student_management.model.Student;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private InfluxDBClient influxDBClient;

    private final String bucket = "studentDB";
    private final String org = "Ashen"; // Replace with your actual InfluxDB org

    @Override
    public void saveStudent(Student student) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        writeApi.writeMeasurement(bucket, org, com.influxdb.client.domain.WritePrecision.NS, student);
    }

    @Override
    public List<Student> getAllStudents() {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String fluxQuery = "from(bucket: \"studentDB\") |> range(start: -30d)";

        List<FluxTable> tables = queryApi.query(fluxQuery, org);
        List<Student> students = new ArrayList<>();

        tables.forEach(table -> table.getRecords().forEach(record -> {
            Student student = new Student();
            student.setsID(record.getValueByKey("sID").toString());
            student.setName(record.getValueByKey("name").toString());
            student.setAge(Integer.parseInt(record.getValueByKey("age").toString()));
            students.add(student);
        }));

        return students;
    }

    @Override
    public Student getStudentById(String sID) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String fluxQuery = String.format("from(bucket: \"studentDB\") |> range(start: -30d) |> filter(fn: (r) => r.sID == \"%s\")", sID);

        List<FluxTable> tables = queryApi.query(fluxQuery, org);
        if (tables.isEmpty()) {
            return null;
        }

        Student student = new Student();
        tables.forEach(table -> table.getRecords().forEach(record -> {
            student.setsID(record.getValueByKey("sID").toString());
            student.setName(record.getValueByKey("name").toString());
            student.setAge(Integer.parseInt(record.getValueByKey("age").toString()));
        }));

        return student;
    }

    @Override
    public void deleteStudent(String sID) {
        // InfluxDB 2.x does not support DELETE by query directly.
        // You need to configure retention policies to remove old data automatically.
        System.out.println("Deletion in InfluxDB 2.x requires retention policy or bucket reorganization.");
    }
}
