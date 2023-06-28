package com.portal.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.WorkFlowConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.bean.Interviewer;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.repository.JobRepository;

@Component
public class DashBoardFilteringsByRangeOfDates {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	CandidateFeedbackRepository candidateFeedbackRepository;

	@Autowired
	InterviewerRepository interviewerRepository;

	@Autowired
	JobRepository jobRepository;

	public Map<String,Map<Map<String,Integer>,List<?>>> 
	getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(String dateFromDropDown){

		Map<String,Map<Map<String,Integer>,List<?>>> finalMap = new HashMap<>();

		
		long date = 0;

		switch(dateFromDropDown) {
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

		/* Candidate WorkFlow */

		Map<Map<String,Integer>,List<?>> addXYaxisMapAndHouringListObjectWorkflow = new HashMap<>();
		
		addXYaxisMapAndHouringListObjectWorkflow.put(getCandidateWorkflowByDateRange(WorkFlowConstants.workFlowConstants, date),
				listOfCandidatesForSpecificSelectedWorkFLowStatusByDateRange(WorkFlowConstants.workFlowConstants));

		finalMap.put("WorkFlow",addXYaxisMapAndHouringListObjectWorkflow);

		/* Candidate Rating */
		
		Map<Map<String,Integer>,List<?>> addXYaxisMapAndHouringListObjectCandidateRating = new HashMap<>();

		addXYaxisMapAndHouringListObjectCandidateRating.put(getCandidatesByRatingFilteredByDateRange(WorkFlowConstants.inputCriteria,
				date), listOfCandidatesForSpecificRatingFilteredByDateRange(WorkFlowConstants.inputCriteria));
		
		finalMap.put("Ratings",addXYaxisMapAndHouringListObjectCandidateRating);

		/* Interviewer Information */

		Map<Map<String,Integer>,List<?>> addXYaxisMapAndHouringListObjectInterviewer = new HashMap<>();

		addXYaxisMapAndHouringListObjectInterviewer.put(getHrAndSelectorByDateRange(WorkFlowConstants.inputSelection,date), 
				getListOfHrAndSelectorData(WorkFlowConstants.inputSelection));
		
		finalMap.put("Count OF HR's and Selectors", addXYaxisMapAndHouringListObjectInterviewer);
		/* Job Information */
		

		return finalMap;

	}

	

	public Map<String,Integer> getCandidateWorkflowByDateRange(String inputDropdownCriteria , long inputDateRange){
		
		List<CandidateFeedback> filteredCandidateDataUponGivenRangeDates = new ArrayList<>();
		Map<String,Integer> storageForXandYaxisPlottingValues = new HashMap<>();
		String[] placeHoldersForDashBoardsGraphsSplitInArray = inputDropdownCriteria.split(",");

		List<CandidateFeedback> consistsOfAllCandidateFeedbacks = candidateFeedbackRepository.findAll();

		for(CandidateFeedback candidateFeedback:consistsOfAllCandidateFeedbacks) {

			List<CandidateHistory> consistsOfAllCandidateHistoryInformation = candidateFeedback.getCandidateHistory();

			if(consistsOfAllCandidateHistoryInformation.get(consistsOfAllCandidateHistoryInformation.size()-1)
					.getLastModifiedDate().toLocalDate().isAfter(LocalDate.now().minusWeeks(1)) && 
					consistsOfAllCandidateHistoryInformation.get(consistsOfAllCandidateHistoryInformation.size()-1)
					.getLastModifiedDate().toLocalDate().isBefore(LocalDate.now())) {
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
				storageForXandYaxisPlottingValues.put(placeHoldersForDashBoardsGraphsSplitInArray[i], count);
			}

		}


		return storageForXandYaxisPlottingValues;

	}

	public List<Candidate> listOfCandidatesForSpecificSelectedWorkFLowStatusByDateRange(String inputStatusCriteria){

		List<Candidate> filteredCandidateDateFromInputStatuesCriteria = new ArrayList<>();

		String[] placeHondersForDashBoardsGraphsSplitInArray = inputStatusCriteria.split(",");

		for(int i=0; i<placeHondersForDashBoardsGraphsSplitInArray.length; i++) {
			List<CandidateFeedback> gettingCandidateIDusingCandidateStatusCriteria =
					candidateFeedbackRepository.findBystatus(placeHondersForDashBoardsGraphsSplitInArray[i]);

			List<String> filteringCanidateID = gettingCandidateIDusingCandidateStatusCriteria.stream().map(CandidateFeedback::getId).toList();

			for(int j=0; j<filteringCanidateID.size(); j++) {
				Candidate candidateObject = candidateRepository.findById(filteringCanidateID.get(j)).get();
				filteredCandidateDateFromInputStatuesCriteria.add(candidateObject);
			}

		}

		return filteredCandidateDateFromInputStatuesCriteria;

	}

	public Map<String,Integer> getCandidatesByRatingFilteredByDateRange(String inputDropdownCriteria , long inputDateRange){
		
		List<CandidateFeedback> candidateFeedbacksList=candidateFeedbackRepository.findAll();

		List<CandidateFeedback> listOfFilteredCandidate= new ArrayList<>();    
		LocalDate startingDate =LocalDate.now().minusWeeks(inputDateRange);
		LocalDate endDate = LocalDate.now();

		for(CandidateFeedback candidateFeedback:candidateFeedbacksList) {
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

		List<Map<String,Object>> listOffeedbackRating    =listOfFilteredCandidate.stream()
				.map(CandidateFeedback::getFeedBack)
				.filter(p->p !=null && !p.values().contains(null) )
				.collect(Collectors.toList());

		for(Map<String,Object> candidateFeedbackObject : listOffeedbackRating) {
			List<Double> parsingDoubleObjectList= new ArrayList<>();
			String feedback =candidateFeedbackObject.values().toString();    
			for(String indivisualFeeback:feedback.split(", ")) {
				indivisualFeeback=indivisualFeeback.replaceAll("[\\[\\],\\s]", "");
				parsingDoubleObjectList.add(Double.parseDouble(indivisualFeeback));
			}
			OptionalDouble calculatedAverage=parsingDoubleObjectList.stream().mapToDouble(values->values).average();
			listOfAverage.add((int)Math.round(calculatedAverage.getAsDouble()));
		}

		for(String indivisualRating:inputDropdownCriteria.split(",")) {
			if(listOfAverage.contains(Integer.parseInt(indivisualRating))) {
				List<Integer> noumberOfCandidates=    listOfAverage.stream()
						.filter(p->p.equals(Integer.parseInt(indivisualRating))).toList();    
				numberOfCandidatesWithRatings.put(indivisualRating,noumberOfCandidates.size());
			}
		}
		return numberOfCandidatesWithRatings;

	}

	public List<Candidate> listOfCandidatesForSpecificRatingFilteredByDateRange(String inputStatusCriteria){

		List<String> groupOfRatingsInput= Arrays.asList(inputStatusCriteria.split(","));
		List<CandidateFeedback> candidateFeedbacksList= candidateFeedbackRepository.findAll();
		List<Candidate> returningCandidateList = new ArrayList<>();

		for(CandidateFeedback candidateFeedback:candidateFeedbacksList) {
			if(candidateFeedback.getFeedBack()!=null) {
				List<Double> parsingDoubleObjectList= new ArrayList<>();
				Map<String, Object> candidateFeeds =candidateFeedback.getFeedBack();
				for(String candidateFeedbackObject : candidateFeeds.values().toString().split(", ")) {
					candidateFeedbackObject=candidateFeedbackObject.replaceAll("[\\[\\],\\s]", "");
					parsingDoubleObjectList.add(Double.parseDouble(candidateFeedbackObject));
				}

				OptionalDouble calculatedAverage=parsingDoubleObjectList.stream().mapToDouble(values->values).average();
				for(String indivisualRating:groupOfRatingsInput) {
					if(Integer.parseInt(indivisualRating)==((int)Math.round(calculatedAverage.getAsDouble()))){
						Candidate candidateObject= candidateRepository.findById(candidateFeedback.getId()).get();
						returningCandidateList.add(candidateObject);
					}
				}
			}
		}

		return returningCandidateList;

	}
	
	public Map<String, Integer> getHrAndSelectorByDateRange(String inputDropdownCriteria , long inputDateRange) {

		List<Interviewer> interviewersList = interviewerRepository.findAll();
		LocalDate startingDate =LocalDate.now().minusWeeks(inputDateRange);
		LocalDate endDate = LocalDate.now();

		String[] placeHondersForDashBoardsGraphsSplitInArray = inputDropdownCriteria.split(",");
		Map<String, Integer> positionsMap = new HashMap();

		for(String str : placeHondersForDashBoardsGraphsSplitInArray ) {

			if(str.equalsIgnoreCase("HR")) {

				List<Interviewer> countHR = interviewersList.stream().filter(x->x.isHr())
						.filter(interviewer->interviewer.getDateOfAssignedPosition().toLocalDate().isAfter(startingDate)
								&& interviewer.getDateOfAssignedPosition().toLocalDate().isBefore(endDate)).collect(Collectors.toList());
				positionsMap.put(str, countHR.size());
			}
			if(str.equalsIgnoreCase("SELECTOR")) {

				List<Interviewer> countSelector = interviewersList.stream().filter(x->x.isSelector())
						.filter(interviewer->interviewer.getDateOfAssignedPosition().toLocalDate().isAfter(startingDate)
								&& interviewer.getDateOfAssignedPosition().toLocalDate().isBefore(endDate)).collect(Collectors.toList());
				positionsMap.put(str, countSelector.size());
			}    
		}
		return positionsMap;
	}
	
	private List<Interviewer> getListOfHrAndSelectorData(String inputselection) {
		
		String[] placeHondersForDashBoardsGraphsSplitInArray = inputselection.split(",");
		List<Interviewer> interviewersList = interviewerRepository.findAll();
		
		List<Interviewer> newInterviewers = new ArrayList<>();

		for(String str : placeHondersForDashBoardsGraphsSplitInArray ) {
			if(str.equalsIgnoreCase("HR")) {
				List<Interviewer> listHr = interviewersList.stream().filter(x->x.isHr()).collect(Collectors.toList());
				newInterviewers.addAll(listHr);
			}
			if(str.equalsIgnoreCase("SELECTOR")) {
				List<Interviewer> listSelector = interviewersList.stream().filter(x->x.isSelector()).collect(Collectors.toList());
				newInterviewers.addAll(listSelector);
			}    
		}
		return newInterviewers;
	}

}
