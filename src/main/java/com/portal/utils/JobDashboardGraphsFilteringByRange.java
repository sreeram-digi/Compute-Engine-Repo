package com.portal.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.JobRepository;

@Component
public class JobDashboardGraphsFilteringByRange {

	
	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	JobRepository jobRepository;
	
	public Map<String,Map<Map<String,Map<String,Integer>>,List<?>>> 
		getAllInformationForJobLocationAndJobSkillSetAndJobTitleFromCandidateAndCandidateFeedback(String dateFromDropDown){
		
		Map<String,Map<Map<String,Map<String,Integer>>,List<?>>> finalMap = new HashMap<>();
		
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
		
		/* Job location */
		
		Map<Map<String,Map<String,Integer>>,List<?>> addXYAxisMapAndHoveringListObjectJobLocation =new HashMap<>();
		
		/* Job Title */
		
		Map<Map<String,Map<String,Integer>>,List<?>> addXYAxisMapAndHoveringListObjectJobTitle =new HashMap<>();
		
		/* Job Title */
		
		Map<Map<String,Map<String,Integer>>,List<?>> addXYAxisMapAndHoveringListObjectJobSkills =new HashMap<>();
		
		
		
		
		return finalMap;
		
	}
	
	
	
}
