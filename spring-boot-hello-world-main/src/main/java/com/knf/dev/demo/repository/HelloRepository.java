package com.knf.dev.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.knf.dev.demo.entity.Hello;

@EnableMongoRepositories
public interface HelloRepository extends MongoRepository<Hello, String>{

	Hello findByName(String name);

}
