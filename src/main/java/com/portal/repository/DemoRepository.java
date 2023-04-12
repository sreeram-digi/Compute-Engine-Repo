package com.portal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portal.bean.DemoPojo;

@Repository
public interface DemoRepository extends MongoRepository<DemoPojo, String>{

}
