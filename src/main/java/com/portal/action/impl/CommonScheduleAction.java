package com.portal.action.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.ApplicationConstants;
import com.portal.action.ActionConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.bean.Interviewer;
import com.portal.bean.MembersForMeeting;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.service.AdminService;
import com.portal.service.impl.EmailUtil;
import com.portal.utils.PortalUtils;

public class CommonScheduleAction extends BaseAction {

	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private InterviewerRepository interviewerRepository;
	
	@Autowired
	private AdminService adminService;

	@Autowired
	private EmailUtil emailUtil;

	@Override
	public void action(Map<String, Object> input, JSONObject workFlow, String action) throws Exception {

		CandidateFeedback candidateFeedback = getCandidateFeedback(input,workFlow, action, candidateFeedbackRepository);
		String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
		String interViewerId = (String) input.get(ActionConstants.INTERVIEWER_ID);
		
		List<String> additionalInterviewers = (List) input.get(ActionConstants.ADDITIONAL_INTERVIEWERS);
		List<String> ccEmailsList = (List) input.get(ActionConstants.CC_EMAILS_LIST);
		
		Interviewer interviewer = interviewerRepository.findById(interViewerId).orElseThrow(()->new Exception("Interviewr not found"));
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));

		Candidate candiate = candidateRepository.findById(candidateId).get();
		LocalDate localDate = LocalDate.parse((String) input.get(ActionConstants.INTERVIEW_DATE));
		LocalTime localTime = LocalTime.parse((String) input.get(ActionConstants.INTERVIEW_TIME));
		Integer timeTakenForInterview = (Integer)input.get(ActionConstants.TIME_TAKEN_FOR_THE_INTERVIEW);
		
		List<MembersForMeeting> inviteesList = adminService.createMeetingInviteesList(interviewer, candiate,additionalInterviewers,ccEmailsList);
		
		JSONObject workFlowNode = workFlow.getJSONObject(action);
		
		if(candidateFeedback.getEventId() != null && workFlowNode.has("deleteEvent") && workFlowNode.getBoolean("deleteEvent")) {
			adminService.deleteEvent(candidateFeedback.getEventId(), (String)input.get(ApplicationConstants.JWT_TOKEN));
		}
		
		String subject = emailUtil.constructEmailSubject(ApplicationConstants.ATTEND_INTERVIEW, candiate.getJobTitle(), workFlowNode.getString("emailMessage"),candiate.getFirstName()+" "+candiate.getLastName());
		
		String emailBody = emailUtil.constructEmailBody(ApplicationConstants.ATTEND_INTERVIEW, 
				candiate.getFirstName()+" "+candiate.getLastName(), interviewer.getInterviewerName());
		
		String eventId = adminService.createEvent(subject, emailBody,
				PortalUtils.getFormatedDate(localDate, localTime), PortalUtils.getFormatedDate(localDate, localTime.plusMinutes(timeTakenForInterview)), 
				inviteesList, (String)input.get(ApplicationConstants.JWT_TOKEN));
		
		
		candidateFeedback.setId(candidateId);
		candidateFeedback.setCurrentInterviewId(interViewerId);
		candidateFeedback.setInterviewerName(interviewer.getInterviewerName());
		candidateFeedback.setNextInterviewDate((String) input.get(ActionConstants.INTERVIEW_DATE));
		candidateFeedback.setNextInterviewTime((String) input.get(ActionConstants.INTERVIEW_TIME));
		candidateFeedback.setTimeTakenForInterview((Integer)input.get(ActionConstants.TIME_TAKEN_FOR_THE_INTERVIEW));
		candidateFeedback.setCriteriaGroup((String) input.get(ActionConstants.CRITERIA_GROUP));
		candidateFeedback.setStatus(action);
		candidateFeedback.setFeedBack(null);
		candidateFeedback.setOverAllFeedBack(null);
		candidateFeedback.setCandidateHistory(canhistorylist);
		candidateFeedback.setEventId(eventId);
		
		candidateFeedbackRepository.save(candidateFeedback);
		
		super.action(input, workFlowNode, action);

	}

}
