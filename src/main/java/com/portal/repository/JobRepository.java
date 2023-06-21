package com.portal.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portal.bean.Job;
@Repository
public interface JobRepository extends MongoRepository<Job, String> {


}
