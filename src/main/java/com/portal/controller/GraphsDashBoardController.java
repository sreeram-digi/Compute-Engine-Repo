package com.portal.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bean.Candidate;
import com.portal.service.GraphsDashBoardService;
import com.portal.service.impl.GraphsDashBoardServiceImpl;
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
	
	@Operation(summary = " This method is used to get interviewer by position")
    @GetMapping("/getInterviewerByPosition/{position}")
    public Map<String, Integer> getCount(@PathVariable("position") String position){
        return graphsDashBoardService.getCount(position);
    }
	
}
