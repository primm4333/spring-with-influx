package com.example.student_management.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.example.student_management.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private InfluxDBClient influxDBClient;

    private final String bucket = "studentDB";
    private final String org = "Ashen";

    @Override
    public void saveStudent(Student student) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        writeApi.writeMeasurement(bucket, org, WritePrecision.NS, student);
    }

    @Override
    public List<Student> getAllStudents() {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String fluxQuery = "from(bucket: \"studentDB\") |> range(start: -7d) |> limit(n: 100)"; // Limit data fetching

        List<FluxTable> tables = queryApi.query(fluxQuery, org);
        List<Student> students = new ArrayList<>();

        tables.forEach(table -> table.getRecords().forEach(record -> {
            Student student = new Student();
            Object idValue = record.getValueByKey("sID");
            Object nameValue = record.getValueByKey("name");
            Object ageValue = record.getValueByKey("age");

            if (idValue != null) student.setsID(idValue.toString());
            if (nameValue != null) student.setName(nameValue.toString());
            if (ageValue != null) student.setAge(Integer.parseInt(ageValue.toString()));

            students.add(student);
        }));

        return students;
    }

    @Override
    public Student getStudentById(String sID) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String fluxQuery = String.format(
                "from(bucket: \"studentDB\") |> range(start: -7d) |> filter(fn: (r) => r[\"sID\"] == \"%s\") |> limit(n: 1)", sID
        );

        List<FluxTable> tables = queryApi.query(fluxQuery, org);
        if (tables.isEmpty()) {
            return null;
        }

        Student student = new Student();
        tables.forEach(table -> table.getRecords().forEach(record -> {
            Object idValue = record.getValueByKey("sID");
            Object nameValue = record.getValueByKey("name");
            Object ageValue = record.getValueByKey("age");

            if (idValue != null) student.setsID(idValue.toString());
            if (nameValue != null) student.setName(nameValue.toString());
            if (ageValue != null) student.setAge(Integer.parseInt(ageValue.toString()));
        }));

        return student;
    }

    @Override
    public void deleteStudent(String sID) {
        // InfluxDB 2.x does not support DELETE by query directly.
        System.out.println("Deletion in InfluxDB 2.x requires retention policy or bucket reorganization.");
    }
}
