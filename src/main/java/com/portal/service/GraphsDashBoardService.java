package com.portal.service;

import java.util.List;
import java.util.Map;
import com.portal.bean.Candidate;
import com.portal.bean.Interviewer;

public interface GraphsDashBoardService {

	public Map<String,Map<Map<String,Integer>,List<?>>> 
	getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(String dateFromDropDown);

	Map<String,Integer> getCandidateByRatings(String rating);

	List<Candidate> getAllCandidatesByRatings(String rating);

	Map<String,Integer> getCandidateBySelectedWorkflowStatus(String inputDropdownCriteria);

	List<Candidate> listOfCandidatesForSpecificSelectedWorkFLowStatus(String inputStatusCriteria);

	Map<String, Integer> getHrAndSelectorCount(String position);

	Map<String,Map<String,Integer>> getDataForJobTitleAgainstStatus(String inputJobTiltles, String status) throws Exception;

	List<Candidate> getListOfCandidatesForJobTitle(String inputJobtitle);

	List<String> getAllJobTitlesForDropDown();

	List<Candidate> getAllCandidateFeedbackIntoList(String action, String locationCandidateCount);

	Map<String, Map<String, Long>> getCandidatesCountByJobLocation(String action, String locationCandidateCount);

	List<String> getAllJobPostedLocation();

	List<String> getAllSkillsFromJob();

	Map<String, Map<String, Integer>> getCandidatesCountAccordingToStatus(String action, String skills);

	List<Candidate> getCandidatesListAccordingToStatus(String action, String skills
	
	Map<String, Integer> getCount(String position);

	List<Interviewer> getListOfData(String position);
	
}

