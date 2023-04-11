package com.knf.dev.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.knf.dev.demo.model.Student;

public interface StudentRepository extends MongoRepository<Student,String>{

}
