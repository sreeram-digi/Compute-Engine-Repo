package com.portal.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.Interviewer;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;


@Component
public class GettingListOfObjectsForGraphs {
	
	
	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	CandidateFeedbackRepository candidateFeedbackRepository;

	@Autowired
	InterviewerRepository interviewerRepository;
	
	
	/**
	 * @author Naga Sreeram
	 * {@summary : When Admin's clicks on a specific bar , he can view all the data of that specific bar}
	 */
	public List<Candidate> listOfCandidatesForSpecificSelectedWorkFLowStatus(String inputStatusCriteria) {

		List<Candidate> filteredCandidateDateFromInputStatuesCriteria = new ArrayList<>();
		List<CandidateFeedback> gettingCandidateIDusingCandidateStatusCriteria= new ArrayList<>();
		for(String statusOfCandidate:inputStatusCriteria.split(",")) {
		
			gettingCandidateIDusingCandidateStatusCriteria.addAll(candidateFeedbackRepository.findBystatus(statusOfCandidate));
		
		}

		List<String> filteringCanidateID = gettingCandidateIDusingCandidateStatusCriteria.stream().map(CandidateFeedback::getId).toList();

		for(int i=0; i<filteringCanidateID.size(); i++) {
			Candidate candidateObject = candidateRepository.findById(filteringCanidateID.get(i)).get();
			filteredCandidateDateFromInputStatuesCriteria.add(candidateObject);
		}
		
		System.out.println(filteredCandidateDateFromInputStatuesCriteria);
		return filteredCandidateDateFromInputStatuesCriteria;
	}
	
	
	/**
	 * @author Preeti Rani Pal
	 * @return List<Candidate>
	 * 
	 * {@summary : The method is to get list of candidates rating  }
	 */
	
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
	
	
	/**
	 * @author Revathi Muddani
	 * @return List<Interviewer> 
	 * 
	 * {@summary: This method is used to return the List of interviewer  }
	 */

	public	List<Interviewer> getListOfData(String position){
		String[] placeHondersForDashBoardsGraphsSplitInArray = position.split(",");
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
