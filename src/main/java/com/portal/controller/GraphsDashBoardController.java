package com.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bean.Candidate;
import com.portal.service.GraphsDashBoardService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GraphsDashBoardController {

	GraphsDashBoardService graphsDashBoardService;

	public GraphsDashBoardController(GraphsDashBoardService graphsDashBoardService) {
		super();
		this.graphsDashBoardService = graphsDashBoardService;
	}

	@Operation(summary = "Filtering data for all the graphs in DashBoard Page")
	@GetMapping(value = "/filterDashBoardByDateRanges/{dateFromDropDown}")
	public Map<String, Map<Map<String, Integer>, List<?>>> 
	getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(@PathVariable("dateFromDropDown") String dateFromDropDown){
		log.info("HIT :: Filtering data for all the graphs in DashBoard Page");
		return graphsDashBoardService.getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(dateFromDropDown);
	}

	@Operation(summary = "This method is used to get number of candidates with rating")
	@GetMapping(value = "/candidateListWithRating/{rating}")
	public Map<String,Integer> getCandidateByRatings(@PathVariable("rating") String rating){
		return graphsDashBoardService.getCandidateByRatings(rating);

	}

	@Operation(summary="This method is used to get All candidates based on the rating")
	@GetMapping("/getAllCandidatesListByRating/{rating}")
	public List<Candidate> getAllCandidatesByRatings(@PathVariable("rating") String rating){
		return graphsDashBoardService.getAllCandidatesByRatings(rating);
	}

	@Operation( summary = "This methods will return a map : Key = X-axis fields & Value = Y-axis plotting data")
	@GetMapping( value = "/getAllCandidateFeedBackCriteria/{inputDropdownCriteria}")
	public Map<String, Integer> getCandidateBySelectedWorkflowStatus(@PathVariable("inputDropdownCriteria") String inputDropdownCriteria) {
		return graphsDashBoardService.getCandidateBySelectedWorkflowStatus(inputDropdownCriteria);
	}

	@Operation( summary = "This method return the object of specified graph")
	@GetMapping( value = "getAllCandidateInformationWithStatus/{inputStatusCriteria}" )
	public List<Candidate> listOfCandidatesForSpecificSelectedWorkFLowStatus(@PathVariable("inputStatusCriteria") String inputStatusCriteria) {
		return graphsDashBoardService.listOfCandidatesForSpecificSelectedWorkFLowStatus(inputStatusCriteria);
	}

	@Operation(summary = " This method is used to get count of HR's and Selectors")
	@GetMapping("/getInterviewerByPosition/{position}")
	public Map<String, Integer> getHrAndSelectorCount(@PathVariable("position") String position){
		return graphsDashBoardService.getHrAndSelectorCount(position);
	}

	@Operation(summary = " This method is used to provide data for the dropdown for jobtitle")
	@GetMapping("/getAllJobtitleForDropdown")
	public List<String> getAllJobTitlesForDropDown(){
		return graphsDashBoardService.getAllJobTitlesForDropDown();
	}
	
	@Operation(summary = " This method is used to get information about job title")
	@GetMapping("/getDataForJobTitleAgainstStatus/{inputJobTiltles}/{status}")
	public Map<String,Map<String,Integer>> getDataForJobTitleAgainstStatus(@PathVariable("inputJobTiltles") String inputJobTiltles, 
			@PathVariable("status") String status) throws Exception{

		return graphsDashBoardService.getDataForJobTitleAgainstStatus(inputJobTiltles, status);
	}

	@Operation(summary = " This method is used to get list of candidate for job title")
	@GetMapping("/getListForJobTitleAgainstStatus/{inputJobTiltle}")
	public List<Candidate> getListOfCandidatesForJobTitle(@PathVariable("inputJobTiltle") String inputJobtitle) {
		return graphsDashBoardService.getListOfCandidatesForJobTitle(inputJobtitle);
	}

	@Operation(summary = " This method is used to provide data for the dropdown location")
	@GetMapping("/getAllLocationFromJobForDropdown")
	public List<String> getAllJobPostedLocation() {
		return graphsDashBoardService.getAllJobPostedLocation();
	}

	@Operation(summary = " This method is used to get count for location")
	@GetMapping("/getCandidatesCountByJobLocation/{locationCandidateCount}/{action}")
	public Map<String,Map<String, Long>> getCandidatesCountByJobLocation(@PathVariable("action") String action,@PathVariable("locationCandidateCount") String locationCandidateCount) {
		return graphsDashBoardService.getCandidatesCountByJobLocation(action,locationCandidateCount);
	}


	@Operation(summary = " This method is used to get list of candidates for location")
	@GetMapping("/getAllCandidateLocationByList/{locationCandidateCount}/{action}")
	public List<Candidate> getAllCandidateFeedbackIntoList(@PathVariable("action") String action,@PathVariable("locationCandidateCount") String locationCandidateCount) {
		return graphsDashBoardService.getAllCandidateFeedbackIntoList(action, locationCandidateCount);
	}
	
	@Operation(summary = "This method is used to get all skills from job pojo")
    @GetMapping("/getAllSkillsFromJob")
    public List<String> getAllSkillsFromJob(){
        return graphsDashBoardService.getAllSkillsFromJob();
    }

    @Operation(summary = "This method is used to return the count of candidates according to status and skill set")
    @GetMapping("/getCandidatesCountAccordingToStatus/{action}/{skills}")
    public Map<String, Map<String, Integer>> getCandidatesCountAccordingToStatus(@PathVariable("action") String action, @PathVariable("skills") String skills){
        return graphsDashBoardService.getCandidatesCountAccordingToStatus(action, skills);
    }

    @Operation(summary = "This method is used to return the List of candidates according to status and skill set")
    @GetMapping("/getCandidatesListAccordingToStatus/{action}/{skills}")
    public List<Candidate> getCandidatesListAccordingToStatus(@PathVariable("action") String action, @PathVariable("skills") String skills){
        return graphsDashBoardService.getCandidatesListAccordingToStatus(action, skills);
    }
}
