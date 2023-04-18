package com.portal.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.UsernamePasswordCredential;
import com.azure.identity.UsernamePasswordCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.Attendee;
import com.microsoft.graph.models.AttendeeType;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Location;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.OnlineMeetingProviderType;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.models.UserSendMailParameterSet;
import com.microsoft.graph.requests.GraphServiceClient;
import com.portal.ApplicationConfigurations;
import com.portal.ApplicationConstants;
import com.portal.action.Action;
import com.portal.action.ActionConstants;
import com.portal.bean.Candidate;
import com.portal.bean.Interviewer;
import com.portal.bean.MembersForMeeting;
import com.portal.bean.UserResponse;
import com.portal.bean.WorkFlowBean;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.InterviewerRepository;
import com.portal.service.AdminService;
import com.portal.utils.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	private InterviewerRepository interviewerRepo;

	@Autowired
	@Qualifier("workflow")
	private JSONObject workflow;

	
	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;
	
	@Autowired
	private ApplicationConfigurations applicationConfigurations;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public AdminServiceImpl(InterviewerRepository interviewerRepo) {
		this.interviewerRepo = interviewerRepo;
	}

	public UserResponse login(String userName, String password) throws UserNotFoundException {
		UserResponse userresponse = new UserResponse();
		Interviewer interviewer = interviewerRepo.findByInterviewerEmailAndInterviewerPassword(userName, password)
									.orElseThrow(()->new UserNotFoundException("Username and password does not match"));
		System.out.println("interviewer ::: "+interviewer.getId());
		System.out.println("interviewer ::: "+interviewer.getInterviewerEmail());
		System.out.println("interviewer ::: "+interviewer.getInterviewerPassword());
		System.out.println("interviewer ::: "+interviewer.getInterviewerName());
		userresponse.setId(interviewer.getId());
		userresponse.setUserName(interviewer.getInterviewerName());
		userresponse.setStatus(Boolean.TRUE);
		userresponse.setAdmin(interviewer.isAdmin());
		userresponse.setSelector(interviewer.isSelector());
		userresponse.setHr(interviewer.isHr());
		userresponse.setExternalUser(interviewer.isExternalUser());
		List<String> accessList = new ArrayList<>();
		if(interviewer.isAdmin()){
			accessList.add("Admin");
		}
		if(interviewer.isSelector()){
			accessList.add("Selector");
		}
		if(interviewer.isHr()){
			accessList.add("Hr");
		}
		if(interviewer.isExternalUser()){
			accessList.clear();
			accessList.add("externalUser");
		}
		userresponse.setToken(jwtTokenUtil.generateToken(userName, interviewer.getId(),accessList,((interviewer.isExternalUser())?"externalUser":"internalUser"),""));
		return userresponse;

	}

	

	@Override
	public void callWorkFlow(WorkFlowBean workFlowBean,boolean initial, String cancel) throws Exception {
		
		JSONObject obj = null;
		if(initial) {
			workFlowBean.setAction(workflow.getString("initalTrigger"));
			obj = (JSONObject) workflow.get(workflow.getString("initalTrigger"));
		}else {
			obj = (JSONObject) workflow.get(workFlowBean.getAction());
		}
		String action = obj.getString("Action");
		if(cancel != null && cancel.equalsIgnoreCase("cancel")) {
			obj = (JSONObject) workflow.get("revertPreviousStage");
			action = obj.getString("Action");
		}
		if(cancel != null && cancel.equalsIgnoreCase("onHold")) {
			obj = (JSONObject) workflow.get("onHoldProfile");
			action = obj.getString("Action");
		}
		if(cancel != null && cancel.equalsIgnoreCase("dropped")) {
			obj = (JSONObject) workflow.get("droppedProfile");
			action = obj.getString("Action");
		}
		if(cancel != null && cancel.equalsIgnoreCase("reInitiate")) {
			obj = (JSONObject) workflow.get("reInitiateProfile");
			action = obj.getString("Action");
		}
		if(cancel != null && cancel.equalsIgnoreCase("skipAction")) {
			obj = (JSONObject) workflow.get("skipAction");
			action = obj.getString("Action");
		}
		
		obj = workflow;
		Class c = Class.forName(action);
		Action act = (Action) c.newInstance();
		autowireCapableBeanFactory.autowireBean(act);
		act.action(workFlowBean.getValueMap(), obj, workFlowBean.getAction());
		
	}

	@Override
	public UserResponse validateAndLogin(String token) throws MalformedURLException, JwkException, UserNotFoundException {
		List<Interviewer> interviewers = null;
		Interviewer interviewer = null;
		UserResponse userresponse = null;
		try {
			DecodedJWT jwt = JWT.decode(token);
			JwkProvider provider = new UrlJwkProvider(new URL("https://login.microsoftonline.com/common/discovery/keys"));
			Jwk jwk = provider.get(jwt.getKeyId());
			Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
			algorithm.verify(jwt);
		//	interviewers = interviewerRepo.findByInterviewerEmail(String.valueOf(jwt.getClaim("email")).toString());
			interviewers = interviewerRepo.findByInterviewerEmail(String.valueOf(jwt.getClaim("preferred_username")).replace('\"',' ').trim().toLowerCase());
			
		//	interviewers = interviewerRepo.findByInterviewerEmail(token);
			if(interviewers != null && interviewers.size() ==1) {
				interviewer = interviewers.get(0);
				if(interviewer.getInterviewerName()==null) {
					interviewer.setInterviewerName(String.valueOf(jwt.getClaim("name")).replace('\"',' ').trim());
					//interviewer.setInterviewerName(token);
					interviewerRepo.save(interviewer);
				}
				userresponse = new UserResponse();
				userresponse.setId(interviewer.getId());
				userresponse.setUserName(interviewer.getInterviewerName());
				userresponse.setStatus(Boolean.TRUE);
				userresponse.setAdmin(interviewer.isAdmin());
				userresponse.setSelector(interviewer.isSelector());
				userresponse.setHr(interviewer.isHr());
				userresponse.setExternalUser(interviewer.isExternalUser());
				List<String> accessList = new ArrayList<>();
				if(interviewer.isAdmin()){
					accessList.add("Admin");
				}
				if(interviewer.isSelector()){
					accessList.add("Selector");
				}
				if(interviewer.isHr()){
					accessList.add("Hr");
				}
				if(interviewer.isExternalUser()){
					accessList.clear();
					accessList.add("externalUser");
				}
				userresponse.setToken(jwtTokenUtil.generateToken(interviewer.getInterviewerName(), interviewer.getId(),accessList,"internalUser",String.valueOf(jwt.getClaim("oid")).replace('\"',' ').trim().toLowerCase()));
				//userresponse.setToken("123456");
			}
		}catch (SignatureVerificationException e){
			log.error(e.getMessage());
			throw new UserNotFoundException("Interviewer not found token validation failed");
		}
		if(userresponse != null) {
			return userresponse;
		}else{
			throw new UserNotFoundException("Interviewer not found");
		}
	}

	@Override
	public String createEvent(String subject, String emailBody, String startDateTime, String endDateTime, List<MembersForMeeting> members, String jwtToken){

		GraphServiceClient graphClient = null;
		
		if(StringUtils.isEmpty(jwtToken))
			graphClient=getGraphClient();
		else
			graphClient = getGraphClientWithSecret();
		
		Event event = new Event();
		event.subject =subject;
		ItemBody body = new ItemBody();
		body.contentType = BodyType.HTML;
		body.content = emailBody;
		event.body = body;
		
		DateTimeTimeZone start = new DateTimeTimeZone();
		start.dateTime = startDateTime; //"2022-06-01T13:00:00";
		start.timeZone = "Asia/Kolkata";
		event.start = start;
		DateTimeTimeZone end = new DateTimeTimeZone();
		end.dateTime = endDateTime; //"2022-06-01T14:00:00";
		end.timeZone = "Asia/Kolkata";
		event.end = end;
		Location location = new Location();
		location.displayName = "At Teams";
		event.location = location;
		LinkedList<Attendee> attendeesList = new LinkedList<Attendee>();

		for (MembersForMeeting member: members) {
			Attendee attendees = new Attendee();
			EmailAddress emailAddress = new EmailAddress();
			emailAddress.address = member.getEmail();
			emailAddress.name = member.getName();
			attendees.emailAddress = emailAddress;
			if(member.getType() != null && member.getType().toUpperCase().equals(AttendeeType.OPTIONAL.toString())){
				attendees.type = AttendeeType.OPTIONAL;
			}else {
				attendees.type = AttendeeType.REQUIRED;
			}
			attendeesList.add(attendees);
		}


		event.attendees = attendeesList;
		event.allowNewTimeProposals = true;
		event.isOnlineMeeting = true;
		event.onlineMeetingProvider = OnlineMeetingProviderType.TEAMS_FOR_BUSINESS;

		if(StringUtils.isEmpty(jwtToken))
		{
			Event eventCreated = graphClient.me().events()
					.buildRequest()
					.post(event);
			return eventCreated.id;
		}
		
		else
		{
			DecodedJWT jwt = JWT.decode(jwtToken);
			Event eventCreated = graphClient.users(String.valueOf(jwt.getClaim(ApplicationConstants.MICROSOFT_ID)).replace('\"',' ').trim().toLowerCase()).events()
					.buildRequest()
					.post(event);
			return eventCreated.id;
		}
		
	}
	
	@Override
	public void sendNotification(String subject, String emailBody,List<MembersForMeeting> members){

		GraphServiceClient graphClient = getGraphClient();

		Message message = new Message();
		message.subject =subject;
		
		ItemBody body = new ItemBody();
		body.contentType = BodyType.HTML;
		body.content = emailBody;
		message.body = body;
		
		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
		LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
		
		for (MembersForMeeting member: members) {
			
			if(member.getType() == null || !ActionConstants.OPTIONAL.equalsIgnoreCase(member.getType()))
			{
				Recipient toRecipients = new Recipient();
				EmailAddress emailAddress = new EmailAddress();
				emailAddress.address = member.getEmail();
				toRecipients.emailAddress = emailAddress;
				toRecipientsList.add(toRecipients);
			}
			else
			{
				Recipient ccRecipient = new Recipient();
				EmailAddress emailAddress = new EmailAddress();
				emailAddress.address = member.getEmail();
				ccRecipient.emailAddress = emailAddress;
				ccRecipientsList.add(ccRecipient);
			}
			
		}

		message.toRecipients = toRecipientsList;
		message.ccRecipients = ccRecipientsList;
		
		boolean saveToSentItems = applicationConfigurations.isSaveToSentItems();
		
		try {
			graphClient.me()
			.sendMail(UserSendMailParameterSet
				.newBuilder()
				.withMessage(message)
				.withSaveToSentItems(saveToSentItems)
				.build())
			.buildRequest()
			.post();
			
			log.info("Email Notification sent successfully ");
		} catch (Exception e)
		{
			log.error("There was error sending email notification, supressing error proceeding",e);
		}

	}
	
	public void deleteEvent(String eventId, String jwtToken) {
		GraphServiceClient graphClient = null;
		
		try {
			if(StringUtils.isEmpty(jwtToken))
				graphClient=getGraphClient();
			else
				graphClient = getGraphClientWithSecret();
			
			if(StringUtils.isEmpty(jwtToken))
			{
				Event eventCreated = graphClient.me().events(eventId).buildRequest().delete();
			}
			
			else
			{
				DecodedJWT jwt = JWT.decode(jwtToken);
				Event eventCreated = graphClient.users(String.valueOf(jwt.getClaim(ApplicationConstants.MICROSOFT_ID)).replace('\"',' ').trim().toLowerCase())
						.events(eventId).buildRequest().delete();
						
			}
		} catch(Exception e)
		{
			log.error("There was error when trying to delete event in Microsoft, error is supressed and processing",e);
		}
	}
	
	private GraphServiceClient getGraphClient() {
		final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
				.clientId("473c4975-2d19-4854-859d-3be1fd26274c")
				.username(applicationConfigurations.getFromemail())
				.password(applicationConfigurations.getEmailPassword())
				.build();
		List<String> scopes = new ArrayList<>();
		scopes.add(".default");

		final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, usernamePasswordCredential);

		return GraphServiceClient.builder().authenticationProvider( tokenCredentialAuthProvider ).buildClient();
	}
	
	private GraphServiceClient getGraphClientWithSecret() {
		ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder().clientId("473c4975-2d19-4854-859d-3be1fd26274c")
															.clientSecret("mEt8Q~VrsXUGemJM8Bc-P6bPOtVpJLutVH5c7aC3")
															.tenantId("457941d4-6381-44c9-ab2f-9f70e80bfec5")
															.build();
		
		List<String> scopes = new ArrayList<>();
		scopes.add(".default");

		final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);

		return GraphServiceClient.builder().authenticationProvider( tokenCredentialAuthProvider ).buildClient();
	}

	/**
	 * Adding admin and uploader email to CC List
	 */
	public List<MembersForMeeting> createMeetingInviteesList(Interviewer interviewer, Candidate candiate,List<String> additionalInterviewers, List<String> ccEmailList) {
		List<MembersForMeeting> inviteesList =new ArrayList<>();
		
		//Add candidate
		MembersForMeeting candidateMember = new MembersForMeeting();
		candidateMember.setEmail(candiate.getEmail());
		candidateMember.setName(candiate.getFirstName());
		inviteesList.add(candidateMember);
		
		//Add interviewer
		MembersForMeeting interviewerMember = new MembersForMeeting();
		interviewerMember.setEmail(interviewer.getInterviewerEmail());
		interviewerMember.setName(interviewer.getInterviewerName());
		inviteesList.add(interviewerMember);
		
		
		//Add UploadedBy Person
		Optional<String> uploadedPersonEmail = Optional.ofNullable(candiate.getUploadedByEmail());
		if(uploadedPersonEmail.isPresent())
		{
			MembersForMeeting uploader = new MembersForMeeting();
			uploader.setEmail(uploadedPersonEmail.get());
			uploader.setName(candiate.getUploadedByName());
			uploader.setType(ActionConstants.OPTIONAL);
			inviteesList.add(uploader);
		}
		
		Optional.ofNullable(additionalInterviewers).orElse(Collections.emptyList()).stream().forEach(email -> {
			if(EmailUtil.isValidEmail(email)) {
				MembersForMeeting additionalInvitee = new MembersForMeeting(); 
				additionalInvitee.setEmail(email);
				inviteesList.add(additionalInvitee);
				} 
			});
		
		Optional.ofNullable(ccEmailList).orElse(Collections.emptyList()).stream().forEach(email -> {
			if(EmailUtil.isValidEmail(email)) {
				MembersForMeeting additionalInvitee = new MembersForMeeting(); 
				additionalInvitee.setEmail(email);
				additionalInvitee.setType(ActionConstants.OPTIONAL);
				inviteesList.add(additionalInvitee);
				} 
			});
		
		return inviteesList;
	}

	@Override
	public JSONObject decodeUserToken(String token) {
		return JwtTokenUtil.decodeUserToken(token);
	}
}