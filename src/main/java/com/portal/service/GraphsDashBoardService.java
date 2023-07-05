package com.portal.service;

import java.util.List;
import java.util.Map;

import com.portal.bean.Candidate;
import com.portal.bean.Interviewer;
import com.portal.response.CandidateResponce;

public interface GraphsDashBoardService {

	Map<String,Map<Map<String,Integer>,List<?>>> 
	getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(String dateFromDropDown);

	Map<String,Map<Map<String,Map<String,Integer>>,List<?>>> 
	getAllInformationForJobLocationAndJobSkillSetAndJobTitleFromCandidateAndCandidateFeedback(String dateFromDropDown)throws Exception;
	
	Map<String,Integer> getCandidateByRatings(String rating);

	List<CandidateResponce> getAllCandidatesByRatings(String rating);

	Map<String,Integer> getCandidateBySelectedWorkflowStatus(String inputDropdownCriteria);

	List<CandidateResponce> listOfCandidatesForSpecificSelectedWorkFLowStatus(String inputStatusCriteria);

	Map<String, Integer> getHrAndSelectorCount(String position);

	Map<String,Map<String,Integer>> getDataForJobTitleAgainstStatus(String inputJobTiltles, String status) throws Exception;

	List<CandidateResponce> getListOfCandidatesForJobTitle(String inputJobtitle,String status) throws Exception;

	List<String> getAllJobTitlesForDropDown();

	List<CandidateResponce> getAllCandidateFeedbackIntoList(String action, String locationCandidateCount);

	Map<String, Map<String, Integer>> getCandidatesCountByJobLocation(String action, String locationCandidateCount);

	List<String> getAllJobPostedLocation();

	List<String> getAllSkillsFromJob();

	Map<String, Map<String, Integer>> getCandidatesCountAccordingToStatus(String action, String skills);

	List<CandidateResponce> getCandidatesListAccordingToStatus(String action, String skills);
	
	List<Interviewer> getListOfData(String position);
	
	
	Map<String,Map<String,Map<String,Integer>>> graphWithJobTitle(String dropDownDate)throws Exception;
}

