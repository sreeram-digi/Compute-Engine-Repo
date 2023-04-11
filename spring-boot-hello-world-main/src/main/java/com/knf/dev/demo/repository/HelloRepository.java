package com.knf.dev.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.knf.dev.demo.entity.Hello;

public interface HelloRepository extends MongoRepository<Hello, String>{

	Hello findByName(String name);

}
