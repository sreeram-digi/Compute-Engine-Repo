package com.portal.service;

import java.util.List;
import java.util.Map;
import com.portal.bean.Candidate;

public interface GraphsDashBoardService {

	public Map<String,Map<Map<String,Integer>,List<?>>> 
	getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(String dateFromDropDown);
	
	Map<String,Integer> getCandidateByRatings(String rating);

    List<Candidate> getAllCandidatesByRatings(String rating);
    
	Map<String,Integer> getCandidateBySelectedWorkflowStatus(String inputDropdownCriteria);
	
	List<Candidate> listOfCandidatesForSpecificSelectedWorkFLowStatus(String inputStatusCriteria);
	
	Map<String, Integer> getCount(String position);
	
}
