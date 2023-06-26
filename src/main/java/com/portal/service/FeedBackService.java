package com.portal.service;

import java.util.List;

import com.portal.bean.Criteria;
import com.portal.bean.CriteriaGroup;
import com.portal.exception.CriteriaNotFoundException;

public interface FeedBackService {

	Criteria createCriteria(Criteria criteria);

	void deleteCriteriaById(String id);

	Criteria updateCriteriaById(Criteria criteria) throws CriteriaNotFoundException;

	Criteria getCriteriaById(String id) throws CriteriaNotFoundException;

	List<Criteria> getAllCriteria();

	void createCriteriaGroup(CriteriaGroup criteriaGroup);

	void deleteCriteriaGroupById(String id);

	void updateCriteriaGroupById(CriteriaGroup criteriaGroup) throws CriteriaNotFoundException;

	CriteriaGroup getCriteriaGroupById(String id) throws CriteriaNotFoundException;

	List<CriteriaGroup> getAllCriteriaGroups();

}
