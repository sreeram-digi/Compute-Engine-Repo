package com.portal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.portal.bean.Job;

public interface JobRepository extends MongoRepository<Job, String> {

}
