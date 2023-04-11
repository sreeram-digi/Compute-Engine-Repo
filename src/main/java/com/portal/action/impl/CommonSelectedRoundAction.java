package com.portal.action.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.ApplicationConfigurations;
import com.portal.ApplicationConstants;
import com.portal.action.ActionConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.bean.MembersForMeeting;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.service.AdminService;
import com.portal.service.impl.EmailUtil;
import com.portal.utils.PortalUtils;

public class CommonSelectedRoundAction extends BaseAction {

	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;

	private InterviewerRepository interviewerRepo;

	@Autowired
	private ApplicationConfigurations applicationConfigurations;

	@Autowired
	private AdminService adminService;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private EmailUtil emailUtil;

	@Override
	public void action(Map<String, Object> input, JSONObject workFlowNode, String action) throws Exception {

		CandidateFeedback candidateFeedback = getCandidateFeedback(input,workFlowNode, action, candidateFeedbackRepository);
		String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));

		candidateFeedback.setId(candidateId);
		candidateFeedback.setFeedBack((Map) input.get(ActionConstants.FEEDBACK));
		candidateFeedback.setOverAllFeedBack((String) input.get(ActionConstants.OVERALL_FEED_BACK));
		candidateFeedback.setNextInterviewDate(null);
		candidateFeedback.setNextInterviewTime(null);
		candidateFeedback.setTimeTakenForInterview(null);
		candidateFeedback.setCurrentInterviewId(null);
		candidateFeedback.setInterviewerName(null);
		candidateFeedback.setStatus(action);
		candidateFeedback.setCandidateHistory(canhistorylist);
		candidateFeedbackRepository.save(candidateFeedback);

		// Sending notification to admin to schedule
		Candidate candiate = candidateRepository.findById(candidateId).get();
		List<MembersForMeeting> emailNotificationList = new ArrayList<>();
		MembersForMeeting candidateMember = new MembersForMeeting();
		candidateMember.setEmail(applicationConfigurations.getAdminNotificationEmail());
		emailNotificationList.add(candidateMember);

		String subject = emailUtil.constructEmailSubject(ApplicationConstants.ASSIGN_INTERVIEWER,
				candiate.getFirstName() + " " + candiate.getLastName());

		String emailbody = emailUtil.constructEmailBody(ApplicationConstants.ASSIGN_INTERVIEWER,
				candiate.getFirstName() + " " + candiate.getLastName(),
				applicationConfigurations.getPortalURL() + "/viewcandidate/" + candidateId);

		adminService.sendNotification(subject, emailbody, emailNotificationList);
		
		super.action(input, workFlowNode, action);

	}

}
