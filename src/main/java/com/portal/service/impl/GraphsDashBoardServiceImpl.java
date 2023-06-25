package com.portal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.Interviewer;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.repository.JobRepository;
import com.portal.service.GraphsDashBoardService;
import com.portal.utils.DashBoardFilteringsByRangeOfDates;

@Service
public class GraphsDashBoardServiceImpl implements GraphsDashBoardService{

	@Autowired
	DashBoardFilteringsByRangeOfDates boardFilteringsByRangeOfDates;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	CandidateFeedbackRepository candidateFeedbackRepository;

	@Autowired
	InterviewerRepository interviewerRepository;

	@Autowired
	JobRepository jobRepository;

	@Override
	public Map<String, Map<Map<String, Integer>, List<?>>> 
	getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(String dateFromDropDown) {
		return boardFilteringsByRangeOfDates.getAllInformationForCandidateRatingsCandidateWorkFlowInterviewerInformationAndJobInformation(dateFromDropDown);
	}

	/**
	 * @author Naga Sreeram
	 * {@summary : When Admin's gives Criteria from dropdown this method provides Values of X and Y axis}
	 */
	@Override
	public Map<String, Integer> getCandidateBySelectedWorkflowStatus(String inputDropdownCriteria) {

		Map<String,Integer> storageForXandYaxisPlottingValues = new HashMap<>();

		String[] placeHondersForDashBoardsGraphsSplitInArray = inputDropdownCriteria.split(",");

		for(int i=0; i<placeHondersForDashBoardsGraphsSplitInArray.length; i++) {
			List<CandidateFeedback> list = candidateFeedbackRepository.findBystatus(placeHondersForDashBoardsGraphsSplitInArray[i]);
			storageForXandYaxisPlottingValues.put(placeHondersForDashBoardsGraphsSplitInArray[i],list.size());
		}

		return storageForXandYaxisPlottingValues;

	}

	/**
	 * @author Naga Sreeram
	 * {@summary : When Admin's clicks on a specific bar , he can view all the data of that specific bar}
	 */
	@Override
	public List<Candidate> listOfCandidatesForSpecificSelectedWorkFLowStatus(String inputStatusCriteria) {

		List<Candidate> filteredCandidateDateFromInputStatuesCriteria = new ArrayList<>();

		List<CandidateFeedback> gettingCandidateIDusingCandidateStatusCriteria =
				candidateFeedbackRepository.findBystatus(inputStatusCriteria);

		List<String> filteringCanidateID = gettingCandidateIDusingCandidateStatusCriteria.stream().map(CandidateFeedback::getId).toList();

		for(int i=0; i<filteringCanidateID.size(); i++) {
			Candidate candidateObject = candidateRepository.findById(filteringCanidateID.get(i)).get();
			filteredCandidateDateFromInputStatuesCriteria.add(candidateObject);
		}

		return filteredCandidateDateFromInputStatuesCriteria;
	}

	/**
	 * @author Preeti Rani Pal
	 * @return Map<String,Integer>
	 * 
	 * {@summary : The method is filtering data by candidates rating  }
	 */
	@Override
	public Map<String,Integer> getCandidateByRatings(String rating) {

		List<String> groupOfRatingsInput= Arrays.asList(rating.split(","));
		List<CandidateFeedback> candidateFeedbacksList=candidateFeedbackRepository.findAll();
		List<Integer> listOfAverage= new ArrayList<>();
		Map<String,Integer> numberOfCandidatesWithRatings= new HashMap<>();

		List<Map<String,Object>> listOffeedbackRating    =candidateFeedbacksList.stream()
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

		for(String indivisualRating:groupOfRatingsInput) {
			if(listOfAverage.contains(Integer.parseInt(indivisualRating))) {
				List<Integer> noumberOfCandidates=    listOfAverage.stream()
						.filter(p->p.equals(Integer.parseInt(indivisualRating))).toList();    
				numberOfCandidatesWithRatings.put(indivisualRating,noumberOfCandidates.size());
			}
		}

		return numberOfCandidatesWithRatings;

	}

	@Override
	public List<Candidate> getAllCandidatesByRatings(String rating) {
		List<String> groupOfRatingsInput= Arrays.asList(rating.split(","));
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

	@Override
	public Map<String, Integer> getCount(String position) {

		String[] placeHondersForDashBoardsGraphsSplitInArray = position.split(",");
		Map<String, Integer> positionsMap = new HashMap();
		List<Interviewer> interviewersList = interviewerRepository.findAll();

		for(String str : placeHondersForDashBoardsGraphsSplitInArray ) {
			if(str.equalsIgnoreCase("HR")) {
				List<Interviewer> count = interviewersList.stream().filter(x->x.isHr()).collect(Collectors.toList());
				positionsMap.put(str, count.size());
			}
			if(str.equalsIgnoreCase("SELECTOR")) {
				List<Interviewer> count = interviewersList.stream().filter(x->x.isSelector()).collect(Collectors.toList());
				positionsMap.put(str, count.size());
			}    
		}
		return positionsMap;
	}

}
