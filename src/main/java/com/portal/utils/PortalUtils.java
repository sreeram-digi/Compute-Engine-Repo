package com.portal.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;

public class PortalUtils {
	
	public static List jsonArrayToList(JSONArray array) {
		List list = new ArrayList<>();
		array.forEach(value->{
			list.add(value);
		});
		return list;  
	}
	
	public static CandidateHistory getCanHistory(CandidateFeedback candidateFeedback, Map input) {
		CandidateHistory canHistory = new CandidateHistory();

		canHistory.setInterviwerId(candidateFeedback.getCurrentInterviewId());
		canHistory.setFeedBack(candidateFeedback.getFeedBack());
		canHistory.setOverAllFeedBack(candidateFeedback.getOverAllFeedBack());
		canHistory.setInterviewTime(candidateFeedback.getNextInterviewTime());
		canHistory.setInterviewDate(candidateFeedback.getNextInterviewDate());
		canHistory.setRoundStatus(candidateFeedback.getStatus());
		canHistory.setInterviewerName(candidateFeedback.getInterviewerName());
		canHistory.setLastModifiedDate(LocalDateTime.now());
		
		return canHistory;
	}
	
	public static String getFormatedDate(LocalDate date, LocalTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
		return formatter.format(date)+"T"+timeFormatter.format(time);
	}


}
