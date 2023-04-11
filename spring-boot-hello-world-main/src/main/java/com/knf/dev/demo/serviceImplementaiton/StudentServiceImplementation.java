package com.knf.dev.demo.serviceImplementaiton;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.knf.dev.demo.model.Student;
import com.knf.dev.demo.repository.StudentRepository;
import com.knf.dev.demo.service.StudentService;

@Service
public class StudentServiceImplementation implements StudentService{
	
	@Autowired
	StudentRepository studentRepository;

	@Override
	public Optional<Student> getStudentById(String id) {
		return studentRepository.findById(id);
	}

	@Override
	public List<Student> getAllStudentList() {
		return studentRepository.findAll();
	}

	@Override
	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}

}
