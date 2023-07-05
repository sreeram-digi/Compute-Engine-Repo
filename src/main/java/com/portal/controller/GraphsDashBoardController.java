package com.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.annotation.Get;
import com.portal.bean.Candidate;
import com.portal.bean.Interviewer;
import com.portal.response.CandidateResponce;
import com.portal.service.GraphsDashBoardService;
import com.portal.utils.JobSkillSetLocationTitleGraphs;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GraphsDashBoardController {

	GraphsDashBoardService graphsDashBoardService;

	@Autowired
	JobSkillSetLocationTitleGraphs graphs;
 	
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
	
	@Operation(summary = "Filtering data for jobskills,jobtitle,joblocation the graphs in DashBoard Page")
	@GetMapping("/filteringJobLocationJobSKillsJobTitleByDate/{dateFromDropDown}")
	public Map<String,Map<Map<String,Map<String,Integer>>,List<?>>> 
	getAllInformationForJobLocationAndJobSkillSetAndJobTitleFromCandidateAndCandidateFeedback(@PathVariable("dateFromDropDown")String dateFromDropDown) throws Exception{
		
		return graphsDashBoardService.getAllInformationForJobLocationAndJobSkillSetAndJobTitleFromCandidateAndCandidateFeedback(dateFromDropDown);
	}

	@Operation(summary = "This method is used to get number of candidates with rating")
	@GetMapping(value = "/candidateListWithRating/{rating}")
	public Map<String,Integer> getCandidateByRatings(@PathVariable("rating") String rating){
		return graphsDashBoardService.getCandidateByRatings(rating);

	}

	@Operation(summary="This method is used to get All candidates based on the rating")
	@GetMapping("/getAllCandidatesListByRating/{rating}")
	public List<CandidateResponce> getAllCandidatesByRatings(@PathVariable("rating") String rating){
		return graphsDashBoardService.getAllCandidatesByRatings(rating);
	}

	@Operation( summary = "This methods will return a map : Key = X-axis fields & Value = Y-axis plotting data")
	@GetMapping( value = "/getAllCandidateFeedBackCriteria/{inputDropdownCriteria}")
	public Map<String, Integer> getCandidateBySelectedWorkflowStatus(@PathVariable("inputDropdownCriteria") String inputDropdownCriteria) {
		return graphsDashBoardService.getCandidateBySelectedWorkflowStatus(inputDropdownCriteria);
	}

	@Operation( summary = "This method return the object of specified graph")
	@GetMapping( value = "getAllCandidateInformationWithStatus/{inputStatusCriteria}" )
	public List<CandidateResponce> listOfCandidatesForSpecificSelectedWorkFLowStatus(@PathVariable("inputStatusCriteria") String inputStatusCriteria) {
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
	@GetMapping("/getListForJobTitleAgainstStatus/{inputJobTiltles}/{status}")
	public List<CandidateResponce> getListOfCandidatesForJobTitle(@PathVariable("inputJobTiltles") String inputJobtitle,
			@PathVariable("status") String status) throws Exception {
		return graphsDashBoardService.getListOfCandidatesForJobTitle(inputJobtitle,status);
	}

	@Operation(summary = " This method is used to provide data for the dropdown location")
	@GetMapping("/getAllLocationFromJobForDropdown")
	public List<String> getAllJobPostedLocation() {
		return graphsDashBoardService.getAllJobPostedLocation();
	}

	@Operation(summary = " This method is used to get count for location")
	@GetMapping("/getCandidatesCountByJobLocation/{locationCandidateCount}/{action}")
	public Map<String, Map<String, Integer>> getCandidatesCountByJobLocation(@PathVariable("action") String action,@PathVariable("locationCandidateCount") String locationCandidateCount) {
		return graphsDashBoardService.getCandidatesCountByJobLocation(action,locationCandidateCount);
	}


	@Operation(summary = " This method is used to get list of candidates for location")
	@GetMapping("/getAllCandidateLocationByList/{locationCandidateCount}/{action}")
	public List<CandidateResponce> getAllCandidateFeedbackIntoList(@PathVariable("action") String action,@PathVariable("locationCandidateCount") String locationCandidateCount) {
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
    @GetMapping("/getCandidatesListAccordingToSkills/{action}/{skills}")
    public List<CandidateResponce> getCandidatesListAccordingToStatus(@PathVariable("action") String action, @PathVariable("skills") String skills){
        return graphsDashBoardService.getCandidatesListAccordingToStatus(action, skills);
    }

	
	@Operation(summary = " This method is used to get list of interviewers by position")
    @GetMapping("/getListOfData/{position}")
	public List<Interviewer> getListOfData(@PathVariable("position") String position){
		return graphsDashBoardService.getListOfData(position);
	}
	
	@GetMapping("/dateFilterForJobGraphs/{dropDownDate}")
	public Map<String,Map<String,Map<String,Integer>>> graphWithJobTitle(@PathVariable("dropDownDate") String dropDownDate)throws Exception{
		return graphs.getAllInformationForJobLocationAndJobSkillSetAndJobTitleFromCandidateAndCandidateFeedback(dropDownDate);
	}
	
	
	@GetMapping("/dateFilterForWorkflowRatingsAndHr/{dropDownDate}")
	public Map<String,Map<String,Integer>> getCandidateRatingWorkflowSelectorsHrsCount(@PathVariable("dropDownDate")String dateFromDropDown){
		return graphs.getCandidateRatingWorkflowSelectorsHrsCount(dateFromDropDown);
	}
	
//	@GetMapping("/dateFilterForJobToGetList/")
//	public  Map<String,Map<String,List<CandidateResponce>>> getListOfCandidatesForJobTitle() throws Exception {
//		return graphs.getListOfCandidatesForJobTitle();
//	}
}
