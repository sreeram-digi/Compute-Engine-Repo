package com.portal.service;

import com.auth0.jwk.JwkException;
import com.portal.bean.Candidate;
import com.portal.bean.Interviewer;
import com.portal.bean.MembersForMeeting;
import com.portal.bean.UserResponse;
import com.portal.bean.WorkFlowBean;
import com.portal.exception.UserNotFoundException;

import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public interface AdminService {

	UserResponse login(String userName, String password) throws UserNotFoundException;
	
	void callWorkFlow(WorkFlowBean workFlowBean, boolean initial,String cancel) throws Exception;

	UserResponse validateAndLogin(String token) throws MalformedURLException, JwkException, UserNotFoundException;

	void deleteEvent(String eventId, String jwtToken);

	void sendNotification(String subject, String emailBody, List<MembersForMeeting> members);

	String createEvent(String subject, String emailBody, String startDateTime, String endDateTime,
			List<MembersForMeeting> members, String jwtToken);

	List<MembersForMeeting> createMeetingInviteesList(Interviewer interviewer, Candidate candiate, List<String> additionalInterviewers, List<String> ccEmailList);
	
	JSONObject decodeUserToken(String token);
}
