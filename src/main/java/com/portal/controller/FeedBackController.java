package com.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bean.Criteria;
import com.portal.bean.CriteriaGroup;
import com.portal.exception.CriteriaNotFoundException;
import com.portal.service.FeedBackService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(maxAge = 3600)
@Slf4j
@RestController
public class FeedBackController {
	private FeedBackService feedBackService;

	public FeedBackController(FeedBackService feedBackService) {
		this.feedBackService = feedBackService;
	}

	@Operation(summary = " This method is used to create criteria")
	@PostMapping(value = "/criteria")
	public Criteria createCriteria(@RequestBody Criteria criteria) {
		log.debug("createCriteria() :creating the criteria:" + criteria.getId());
		return feedBackService.createCriteria(criteria);
	}

	@Operation(summary = "This method is used to delete criteria with id")
	@DeleteMapping(value = "/criteria/{id}")
	public void deleteCriteriaById(@PathVariable(value = "id") String id) {
		feedBackService.deleteCriteriaById(id);
	}

	@Operation(summary = "This method is used to update criteria with id")
	@PutMapping(value = "/criteria")
	public Criteria updateCriteriaById(@RequestBody Criteria criteria) throws CriteriaNotFoundException {
		return feedBackService.updateCriteriaById(criteria);
	}

	@Operation(summary = "This method is used to get criteria using id")
	@GetMapping("/criteria/{id}")
	public Criteria getCriteriaById(@PathVariable String id) throws CriteriaNotFoundException {
		return feedBackService.getCriteriaById(id);
	}

	@Operation(summary = "This method id used to ger all the available criteria's")
	@GetMapping(value = "/criteria")
	public List<Criteria> getAllCriteria() {
		log.debug("getAllCriteria() :getting all criteria:");
		return this.feedBackService.getAllCriteria();
	}

	@Operation(summary = " This method is used to create criteria group")
	@PostMapping(value = "/criteriaGroup")
	public void createCriteriaGroup(@RequestBody CriteriaGroup criteriaGroup) {
		log.debug("createCriteriaGroup() :creating the criteriaGroup:" + criteriaGroup.getId());
		feedBackService.createCriteriaGroup(criteriaGroup);
	}

	@Operation(summary = "This method is used to delete criteria group with id")
	@DeleteMapping(value = "/criteriaGroup/{id}")
	public void deleteCriteriaGroupById(@PathVariable(value = "id") String id) {
		feedBackService.deleteCriteriaGroupById(id);
	}

	@Operation(summary = "This method is used to update criteria group")
	@PutMapping(value = "/criteriaGroup")
	public void updateCriteriaGroupById(@RequestBody CriteriaGroup criteriaGroup) throws CriteriaNotFoundException {
		feedBackService.updateCriteriaGroupById(criteriaGroup);
	}

	@Operation(summary = "This method is used to get criteria group using id")
	@GetMapping("/criteriaGroup/{id}")
	public CriteriaGroup getCriteriaGroupById(@PathVariable String id) throws CriteriaNotFoundException {
		return feedBackService.getCriteriaGroupById(id);
	}

	@Operation(summary = "This method id used to ger all the available criteria groups")
	@GetMapping(value = "/criteriaGroup")
	public List<CriteriaGroup> getAllCriteriaGroups() {
		log.debug("getAllCriteriaGroups() :getting all criteriaGroups:");
		return this.feedBackService.getAllCriteriaGroups();
	}

}
