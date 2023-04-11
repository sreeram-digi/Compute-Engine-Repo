package com.knf.dev.demo.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.knf.dev.demo.model.Student;
import com.knf.dev.demo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class HelloController {
	
	@Autowired
	StudentService studentService;

	@Operation(summary = "This should return you Hello World! string")
    @GetMapping("/hello")
    public String getMessage() {
        return "Hello World!";
    }
    
	@Operation(summary = "The should return you Hi All string")
    @GetMapping("/getInfo")
    public String getInfo() {
    	return "Hi All";
    }
    
	@Operation(summary = "This should return Naga Sreeram string")
    @GetMapping("/callName")
    public String callName() {
    	return "Naga Sreeram";
    }
    
    @Operation(summary = "This is used for saving specific student into MongoDB")
    @PostMapping("/saveUser")
    public Student saveStudent(@RequestBody Student student) {
    	return studentService.saveStudent(student);
    }
    
    @Operation(summary = "This should return the information of specific student with provided ID")
    @GetMapping("/getStudentById/{id}")
	public Optional<Student> getStudentById(@PathVariable String id) {
    	return studentService.getStudentById(id);
    }
    
    @Operation(summary = "This should return all the information about the Students")
    @GetMapping("/getAllStudentInfo")
	public List<Student> getAllStudentList() {
		return studentService.getAllStudentList();
	}
    
}
