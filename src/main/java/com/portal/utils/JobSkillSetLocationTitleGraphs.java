package com.portal.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.WorkFlowConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.bean.Interviewer;
import com.portal.bean.Job;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.repository.JobRepository;
import com.portal.response.CandidateResponce;

@Component
public class JobSkillSetLocationTitleGraphs {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	CandidateFeedbackRepository candidateFeedbackRepository;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	InterviewerRepository interviewerRepository;

	public Map<String,Map<String,Map<String,Integer>>>
	getAllInformationForJobLocationAndJobSkillSetAndJobTitleFromCandidateAndCandidateFeedback(String dateFromDropDown) throws Exception{


		Map<String,Map<String,Map<String,Integer>>> finalMap=new HashMap<>();

		long date = 0;

		switch(dateFromDropDown) {
		case("Daily"):
			date = 0;
		break;
		case("Weekly"):
			date = 1;
		break;
		case("Monthly"):
			date = 4;
		break;
		case("Quarterly"):
			date = 13;
		break;
		case("Half-Yearly"):
			date = 26;
		break;
		case("Yearly"):
			date = 52;
		break;
		default:
			date = 52;
		}


		/*Joblocation*/

		finalMap.put("jobLocation",getAllJobLocationFromDateRange(date));


		/*jobtitle*/

		finalMap.put("jobTitle", getJobByTitles(date));

		/*Job skill set*/

		finalMap.put("jobSkills",getCandidatesCountAccordingToStatus(WorkFlowConstants.appliedSelectedRejectedValues, date) );

		return finalMap;

	}

	public Map<String,Map<String,Integer>> getJobByTitles(long inputdate) throws Exception{

		List<String> jobTitleList=jobRepository.findAll().stream().map(p->p.getJobTitle()).distinct().toList();

		Map<String,Map<String,Integer>> statusOfcandidateMap=new HashMap<>();

		LocalDate startingDate =LocalDate.now().minusWeeks(inputdate);
		LocalDate endDate = LocalDate.now();	

		for(String inputJobTitleObject:jobTitleList) {
			Map<String,Integer> xAndYasisValues=new HashMap<>();
			if(jobTitleList.contains(inputJobTitleObject)){

				List<String> statusList=Arrays.asList(WorkFlowConstants.appliedSelectedRejectedValues.split(","));

				for(String singleStatusObject:statusList) {
					List<String> multipleStatusList=new ArrayList<>();
					List<Candidate> tempStatusList= new ArrayList<>();
					switch(singleStatusObject) {

					case "Applied":
						multipleStatusList.add(WorkFlowConstants.appliedWorkflowConstants);
						break;

					case "Selected":
						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.selectedWorkflowConstants.split(",")));
						break;

					case "Rejected":
						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.rejectedWorkflowConstants.split(",")));
						break;	
					}

					for(String sublistOfstatus:multipleStatusList) {
						List<CandidateFeedback> candidateFeedback=candidateFeedbackRepository.findBystatus(sublistOfstatus);
						List<String> candidateIds =candidateFeedback.stream().map(CandidateFeedback::getId).toList();
						for(String candidateId:candidateIds) {
							Candidate candidate	=candidateRepository.findById(candidateId).get();
							if(candidate.getLastModifiedDate().toLocalDate().isAfter(startingDate)
									&& candidate.getLastModifiedDate().toLocalDate().isBefore(endDate)) {
								if(candidate.getJobTitle().equalsIgnoreCase(inputJobTitleObject)) {
									tempStatusList.add(candidate);
								}
							}

							if(candidate.getUploadedDate().toLocalDate().isAfter(startingDate) && 
									candidate.getUploadedDate().toLocalDate().isBefore(endDate)) {
								if(sublistOfstatus.equals("Applied")) {
									tempStatusList=candidateRepository.findAll().stream().filter(p->p.getJobTitle().equalsIgnoreCase(inputJobTitleObject)).toList();

								}
							}
						}
						xAndYasisValues.put(singleStatusObject, tempStatusList.size());
					}
				}
				statusOfcandidateMap.put(inputJobTitleObject, xAndYasisValues);
			}
			else {
				throw new Exception("Job Title not found");
			}
		}


		return statusOfcandidateMap;
	}

	/**
	 * @author Naga Sreeram
	 * {@summary : Filtering by job location and returning the Count of candiadtes with respective to status }
	 */

	public Map<String,Map<String,Integer>> getAllJobLocationFromDateRange(long inputDate){

		Map<String, Map<String, Integer>> finalMap = new HashMap<>();
		LocalDate startingDate =LocalDate.now().minusWeeks(inputDate);
		LocalDate endDate = LocalDate.now();

		String[] splitIntoSelectSpecificAction = WorkFlowConstants.appliedSelectedRejectedValues.split(",");

		List<String> splitIntoLocation =jobRepository.findAll().stream().map(p->p.getLocation()).toList();

		for(int i=0; i<splitIntoLocation.size(); i++) {
			Map<String,Integer> storageForXandYaxisPlottingValues = new HashMap<>();
			for(int j=0; j<splitIntoSelectSpecificAction.length; j++) {
				List<Candidate> tempStatusList= new ArrayList<>();

				String selectSpecificAction = null;

				switch(splitIntoSelectSpecificAction[j]) {
				case("Applied"):
					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
				break;
				case("Selected"):
					selectSpecificAction = WorkFlowConstants.selectedWorkflowConstants;
				break;
				case("Rejected"):
					selectSpecificAction = WorkFlowConstants.rejectedWorkflowConstants;
				break;
				default:
					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
					break;
				}
				String[] filteredActionInputFromUser = selectSpecificAction.split(",");

				for(int k=0; k<filteredActionInputFromUser.length; k++) {

					List<CandidateFeedback> feedbackList = candidateFeedbackRepository.findBystatus(filteredActionInputFromUser[k]);
					for(CandidateFeedback candidateFeedback:feedbackList) {
						Candidate candidate = candidateRepository.findById(candidateFeedback.getId()).get();
						Job job = jobRepository.findByLocation(splitIntoLocation.get(i));

						if(candidate.getLastModifiedDate().toLocalDate().isAfter(startingDate) && 
								candidate.getLastModifiedDate().toLocalDate().isBefore(endDate)) {
							if(job.getId().contains(candidate.getJobId())) { 

								tempStatusList.add(candidate);
							}
						}

						if(candidate.getUploadedDate().toLocalDate().isAfter(startingDate) && 
								candidate.getUploadedDate().toLocalDate().isBefore(endDate)) {
							if(filteredActionInputFromUser[k].equalsIgnoreCase("Applied")) {
								tempStatusList  =  candidateRepository.findAll().stream()
										.filter(p->p.getJobId().equals(job.getId())).toList();
							}
						}
					}
				}
				storageForXandYaxisPlottingValues.put(splitIntoSelectSpecificAction[j], tempStatusList.size());
			}
			finalMap.put(splitIntoLocation.get(i), storageForXandYaxisPlottingValues);
		}

		return finalMap;
	}


	/**
	* @author Revathi Muddani
	* @return Map<String, Map<String, Integer>>>
	* 
	* {@summary: This method is used to return the Count of candidates according to status and skill set }
	*/

	public Map<String, Map<String, Integer>> getCandidatesCountAccordingToStatus(String actions,long inputDateRange) {

		String[] actionArray = actions.split(",");

		List<Job> jobList = jobRepository.findAll();

		Map<String,Map<String,Integer>> allMap = new HashMap<>();

		for(Job jobs : jobList) {

			List<Candidate> candidateList = candidateRepository.findByJobId(jobRepository.findBySkillSet(jobs.getSkillSet()).getId());
			Map<String,Integer> countMap = new HashMap<>();

			for(String action : actionArray) {

				switch (action) {
				case "Applied": countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.appliedWorkflowConstants.split(","),inputDateRange));
				break;

				case "Rejected" : countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.rejectedWorkflowConstants.split(","),inputDateRange));
				break;

				case "Selected" : countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.selectedWorkflowConstants.split(","),inputDateRange));
				break;
				}
			}
			allMap.put(jobs.getSkillSet(), countMap);
		}
		return allMap;
	}

	public Integer getCandidateCount(List<Candidate> candidateList, String[] actions,long inputDateRange) {

		LocalDate startingDate =LocalDate.now().minusWeeks(inputDateRange);
		LocalDate endDate = LocalDate.now();


		List<Candidate> candidatesAction = new ArrayList<>();

		for(String action : actions) {

			if(action.equalsIgnoreCase("Applied")) {
				candidatesAction.addAll(candidateList.stream().filter(candidate->candidate.getUploadedDate().toLocalDate().isAfter(startingDate)
						&& candidate.getUploadedDate().toLocalDate().isBefore(endDate)).toList());
				break;
			}

			candidatesAction.addAll(candidateList.stream().filter(candidate->candidate.getCandidateFeedback().getStatus().equalsIgnoreCase(action))
					.filter(candidate->candidate.getUploadedDate().toLocalDate().isAfter(startingDate)
							&& candidate.getUploadedDate().toLocalDate().isBefore(endDate)).toList());
		}
		return candidatesAction.size();
	}


	
	public Map<String,Map<String,Integer>> getCandidateRatingWorkflowSelectorsHrsCount(String dateFromDropDown){
	
		Map<String,Map<String,Integer>> finalMap=new HashMap<>();
		
		long date = 0;

		switch(dateFromDropDown) {
		case("Daily"):
			date = 0;
		break;
		case("Weekly"):
			date = 1;
		break;
		case("Monthly"):
			date = 4;
		break;
		case("Quarterly"):
			date = 13;
		break;
		case("Half-Yearly"):
			date = 26;
		break;
		case("Yearly"):
			date = 52;
		break;
		default:
			date = 52;
		}
		
		finalMap.put("Workflow", getCandidateWorkflowByDateRange(WorkFlowConstants.workFlowConstants, date));
		
		finalMap.put("Ratings",getCandidatesByRatingFilteredByDateRange(WorkFlowConstants.inputCriteria,date ));
		
		finalMap.put("CountOfHRSelectors", getHrAndSelectorByDateRange(WorkFlowConstants.inputSelection,date));
		
		
		return finalMap;
	}
	
	
	/**
	 * @author Naga Sreeram
	 * {@summary : When Admin's clicks on a specific bar , he can view all the data of that specific bar}
	 */

	public Map<String,Integer> getCandidateWorkflowByDateRange(String inputDropdownCriteria , long inputDateRange){
		
		LocalDate startingDate =LocalDate.now().minusWeeks(inputDateRange);
		LocalDate endDate = LocalDate.now();
		
		List<CandidateFeedback> filteredCandidateDataUponGivenRangeDates = new ArrayList<>();
		
		String[] placeHoldersForDashBoardsGraphsSplitInArray = inputDropdownCriteria.split(",");

		List<CandidateFeedback> consistsOfAllCandidateFeedbacks = candidateFeedbackRepository.findAll();
		Map<String,Integer> storageForXandYaxisPlottingValues = new HashMap<>();
		for(CandidateFeedback candidateFeedback:consistsOfAllCandidateFeedbacks) {
			
			List<CandidateHistory> consistsOfAllCandidateHistoryInformation = candidateFeedback.getCandidateHistory();

			if(consistsOfAllCandidateHistoryInformation.get(consistsOfAllCandidateHistoryInformation.size()-1)
					.getLastModifiedDate().toLocalDate().isAfter(startingDate) && 
					consistsOfAllCandidateHistoryInformation.get(consistsOfAllCandidateHistoryInformation.size()-1)
					.getLastModifiedDate().toLocalDate().isBefore(endDate)) {
				filteredCandidateDataUponGivenRangeDates.add(candidateFeedback);
			}

		}

		List<String> getStatusFromfilteredCandidateDataUponGivenRangeDates = filteredCandidateDataUponGivenRangeDates
				.stream().map(CandidateFeedback::getStatus).toList();

		for(int i=0; i<placeHoldersForDashBoardsGraphsSplitInArray.length; i++) {
			int count = 0;
			for(int j=0; j<getStatusFromfilteredCandidateDataUponGivenRangeDates.size(); j++) {

				if(placeHoldersForDashBoardsGraphsSplitInArray[i].equals(
						(getStatusFromfilteredCandidateDataUponGivenRangeDates.get(j)))) {
					count++;

				}
				System.out.println(storageForXandYaxisPlottingValues);
				storageForXandYaxisPlottingValues.put(placeHoldersForDashBoardsGraphsSplitInArray[i], count);
			}

		}
		return storageForXandYaxisPlottingValues;
	}

	/**
	 * @author Preeti Rani Pal
	 * @return Map<String,Integer>
	 * 
	 * {@summary : The method is to get list of candidates rating  }
	 */

	public Map<String,Integer> getCandidatesByRatingFilteredByDateRange(String inputDropdownCriteria , long inputDateRange){
		
		List<CandidateFeedback> listOfFilteredCandidate= new ArrayList<>();    
		LocalDate startingDate =LocalDate.now().minusWeeks(inputDateRange);
		LocalDate endDate = LocalDate.now();

		for(CandidateFeedback candidateFeedback : candidateFeedbackRepository.findAll()) {
			List<CandidateHistory> indivisualcandidateHistorielist = candidateFeedback.getCandidateHistory();
			if(indivisualcandidateHistorielist.get(indivisualcandidateHistorielist.size()-1)
					.getLastModifiedDate().toLocalDate().isAfter(startingDate)
					&& indivisualcandidateHistorielist.get(indivisualcandidateHistorielist.size()-1)
					.getLastModifiedDate().toLocalDate().isBefore(endDate)) {
				listOfFilteredCandidate.add(candidateFeedback);
			}
		}
		
		List<Integer> listOfAverage= new ArrayList<>();
		Map<String,Integer> numberOfCandidatesWithRatings= new HashMap<>();

		List<Map<String,Object>> listOffeedbackRating = listOfFilteredCandidate.stream()
				.map(CandidateFeedback::getFeedBack)
				.filter(p->p !=null && !p.values().contains(null) )
				.collect(Collectors.toList());

		for(Map<String,Object> candidateFeedbackObject : listOffeedbackRating) {
			List<Double> parsingDoubleObjectList= new ArrayList<>();
			String feedback =candidateFeedbackObject.values().toString();   
			
			for(String indivisualFeeback : feedback.split(", ")) {
				indivisualFeeback=indivisualFeeback.replaceAll("[\\[\\],\\s]", "");
				parsingDoubleObjectList.add(Double.parseDouble(indivisualFeeback));
			}
			listOfAverage.add((int)Math.round(parsingDoubleObjectList.stream().mapToDouble(values->values).average().getAsDouble()));
		}

		for(String indivisualRating : inputDropdownCriteria.split(",")) {
			if(listOfAverage.contains(Integer.parseInt(indivisualRating))) {
				List<Integer> noumberOfCandidates=    listOfAverage.stream()
						.filter(p->p.equals(Integer.parseInt(indivisualRating))).toList();    
				numberOfCandidatesWithRatings.put(indivisualRating,noumberOfCandidates.size());
			}
		}
		return numberOfCandidatesWithRatings;
	}

	
	/**
	 * @author Revathi Muddani
	 * @return Map<String, Integer>
	 * 
	 * {@summary: This method is used to get count of HR's and Selectors  }
	 */

	public Map<String, Integer> getHrAndSelectorByDateRange(String inputDropdownCriteria , long inputDateRange) {

		LocalDate startingDate =LocalDate.now().minusWeeks(inputDateRange);
		LocalDate endDate = LocalDate.now();

		Map<String, Integer> positionsMap = new HashMap();

		for(String str : inputDropdownCriteria.split(",") ) {

			if(str.equalsIgnoreCase("HR")) {

				List<Interviewer> countHR = interviewerRepository.findAll().stream().filter(x->x.isHr())
						.filter(interviewer->interviewer.getDateOfAssignedPosition().toLocalDate().isAfter(startingDate)
								&& interviewer.getDateOfAssignedPosition().toLocalDate().isBefore(endDate)).collect(Collectors.toList());
				positionsMap.put(str, countHR.size());
			}
			if(str.equalsIgnoreCase("SELECTOR")) {

				List<Interviewer> countSelector = interviewerRepository.findAll().stream().filter(x->x.isSelector())
						.filter(interviewer->interviewer.getDateOfAssignedPosition().toLocalDate().isAfter(startingDate)
								&& interviewer.getDateOfAssignedPosition().toLocalDate().isBefore(endDate)).collect(Collectors.toList());
				positionsMap.put(str, countSelector.size());
			}    
		}
		return positionsMap;
	}
	


}
