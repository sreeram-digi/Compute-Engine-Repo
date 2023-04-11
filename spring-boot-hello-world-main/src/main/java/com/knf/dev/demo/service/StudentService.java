package com.knf.dev.demo.service;

import java.util.List;
import java.util.Optional;
import com.knf.dev.demo.model.Student;

public interface StudentService {

	
	Optional<Student> getStudentById(String id);
	List<Student> getAllStudentList();
	Student saveStudent(Student student);
	
}
