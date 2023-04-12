package com.portal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.portal.bean.Criteria;

public interface FeedBackRepository extends MongoRepository<Criteria, String>{
	

}
