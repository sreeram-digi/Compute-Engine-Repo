package com.portal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.portal.bean.CriteriaGroup;

public interface CriteriaGroupRepository extends MongoRepository<CriteriaGroup, String> {

}
