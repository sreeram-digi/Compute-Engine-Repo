package com.portal.action.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.ApplicationConfigurations;
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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PendingShortListAction extends BaseAction {

	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	private InterviewerRepository interviewerRepository;

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private ApplicationConfigurations applicationConfigurations;

	@Autowired
	private EmailUtil emailUtil;
	
	
	@Override
	public void action(Map<String, Object> object, JSONObject workFlowNode, String action) throws Exception {
			CandidateFeedback candidateFeedback = getCandidateFeedback(object, workFlowNode, action, candidateFeedbackRepository);
			
			String candidateId = (String) object.get(ActionConstants.CANDIDATE_ID);
			String selectorId = (String) object.get(ActionConstants.SELECTOR_ID);
			Interviewer interviewer = interviewerRepository.findById(selectorId).orElseThrow(()->new Exception("Interviewr not found"));
			if(interviewer.isExternalUser()) {
				throw new Exception("External User is not allowed to move the stage");
			}
			List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
			canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,object));
			
			candidateFeedback.setId(candidateId);
			candidateFeedback.setStatus(action);
			candidateFeedback.setInterviewerName(interviewer.getInterviewerName());
			candidateFeedback.setCurrentInterviewId(selectorId);
			candidateFeedbackRepository.save(candidateFeedback);

			//Sending notification to Selector to short candidate
			Candidate candiate = candidateRepository.findById(candidateId).get();
			
			List<MembersForMeeting> emailNotificationList =new ArrayList<>();
			MembersForMeeting candidateMember = new MembersForMeeting();
			candidateMember.setEmail(interviewer.getInterviewerEmail());
			emailNotificationList.add(candidateMember);
			
			String subject = emailUtil.constructEmailSubject(ApplicationConstants.SHORTLIST_CANDIDATE, candiate.getFirstName()+" "+candiate.getLastName());
			
			String emailbody = emailUtil.constructEmailBody(ApplicationConstants.SHORTLIST_CANDIDATE, 
											interviewer.getInterviewerName(),
											candiate.getFirstName()+" "+candiate.getLastName(), 
											applicationConfigurations.getPortalURL()+"/viewcandidate/"+candidateId);
			
			adminService.sendNotification(subject,emailbody,emailNotificationList);
			
			super.action(object, workFlowNode, action);
	}

}
