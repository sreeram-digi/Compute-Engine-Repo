package com.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.ReplaceOptions;
import com.portal.bean.Criteria;
import com.portal.bean.CriteriaGroup;
import com.portal.exception.CriteriaNotFoundException;
import com.portal.repository.CriteriaGroupRepository;
import com.portal.repository.FeedBackRepository;
import com.portal.service.FeedBackService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FeedBackServiceImpl implements FeedBackService {

	private FeedBackRepository feedBackRepository;

	private CriteriaGroupRepository criteriaGroupRepository;

	private MongoTemplate mongoTemplate;

	public FeedBackServiceImpl(FeedBackRepository feedBackRepository, CriteriaGroupRepository criteriaGroupRepository,
			MongoTemplate mongoTemplate) {
		this.feedBackRepository = feedBackRepository;
		this.criteriaGroupRepository = criteriaGroupRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Criteria createCriteria(Criteria criteria) {
		return feedBackRepository.save(criteria);
	}

	@Override
	public void deleteCriteriaById(String id) {
		feedBackRepository.deleteById(id);
	}

	@Override
	public Criteria updateCriteriaById(Criteria criteria) throws CriteriaNotFoundException {
		feedBackRepository.findById(criteria.getId()).orElseThrow(
				() -> new CriteriaNotFoundException("Criteria with Id " + criteria.getId() + " not found"));
		this.feedBackRepository.save(criteria);
		return criteria;
	}

	@Override
	public Criteria getCriteriaById(String id) throws CriteriaNotFoundException {
		log.debug("getCriteriaById(): getting criteria with id: " + id);
		return feedBackRepository.findById(id)
				.orElseThrow(() -> new CriteriaNotFoundException("Criteria with Id " + id + " not found"));
	}

	@Override
	public List<Criteria> getAllCriteria() {
		return feedBackRepository.findAll();
	}

	@Override
	public void createCriteriaGroup(CriteriaGroup criteriaGroup) {
		List<Document> criterias = new ArrayList<>();
		criteriaGroup.getCriteriaIds().forEach(id -> {
			Document d = new Document("$ref", "criteria").append("$id", id);
			criterias.add(d);
		});
		Document finalDoc = new Document("_id", criteriaGroup.getId()).append("criterias", criterias);
		mongoTemplate.getCollection("criteriaGroup").insertOne(finalDoc);
		// return criteriaGroupRepository.save(criteriaGroup);
	}

	@Override
	public void deleteCriteriaGroupById(String id) {
		criteriaGroupRepository.deleteById(id);
	}

	@Override
	public void updateCriteriaGroupById(CriteriaGroup criteriaGroup) throws CriteriaNotFoundException {
		criteriaGroupRepository.findById(criteriaGroup.getId()).orElseThrow(
				() -> new CriteriaNotFoundException("CriteriaGroup with Id " + criteriaGroup.getId() + " not found"));
		List<Document> criterias = new ArrayList<>();
		criteriaGroup.getCriteriaIds().forEach(id -> {
			Document d = new Document("$ref", "criteria").append("$id", id);
			criterias.add(d);
		});
		Document finalDoc = new Document("_id", criteriaGroup.getId()).append("criterias", criterias);
		Document filter = new Document("_id", criteriaGroup.getId());
		ReplaceOptions replaceOpc = new ReplaceOptions().upsert(false);
		mongoTemplate.getCollection("criteriaGroup").replaceOne(filter, finalDoc, replaceOpc);
	}

	@Override
	public CriteriaGroup getCriteriaGroupById(String id) throws CriteriaNotFoundException {
		return criteriaGroupRepository.findById(id)
				.orElseThrow(() -> new CriteriaNotFoundException("CriteriaGroup with Id " + id + " not found"));
	}

	@Override
	public List<CriteriaGroup> getAllCriteriaGroups() {
		return criteriaGroupRepository.findAll();
	}

}
