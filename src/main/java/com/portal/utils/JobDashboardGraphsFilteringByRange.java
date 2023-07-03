package com.portal.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.WorkFlowConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.Job;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.JobRepository;
import com.portal.response.CandidateResponce;

@Component
public class JobDashboardGraphsFilteringByRange {


	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	CandidateFeedbackRepository candidateFeedbackRepository;

	@Autowired
	JobRepository jobRepository;

	public Map<String,Map<Map<String,Map<String,Integer>>,List<?>>> 
	getAllInformationForJobLocationAndJobSkillSetAndJobTitleFromCandidateAndCandidateFeedback(String dateFromDropDown) throws Exception{

		Map<String,Map<Map<String,Map<String,Integer>>,List<?>>> finalMap = new HashMap<>();

		long date = 0;

		switch(dateFromDropDown) {
		case("Daily"):
			date = 0;
		break;
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
		addXYAxisMapAndHoveringListObjectJobLocation.put(getAllJobLocationFromDateRange(date),
				getAllCandidateFeedbackIntoList(WorkFlowConstants.appliedSelectedRejectedValues) );

		/* Job Title */

		Map<Map<String,Map<String,Integer>>,List<?>> addXYAxisMapAndHoveringListObjectJobTitle =new HashMap<>();
		addXYAxisMapAndHoveringListObjectJobTitle.put(getAllJobTitleFromDateRange(date),  
				getListOfCandidatesForJobTitle(WorkFlowConstants.appliedSelectedRejectedValues));

		/* Job Title */

		Map<Map<String,Map<String,Integer>>,List<?>> addXYAxisMapAndHoveringListObjectJobSkills =new HashMap<>();

		addXYAxisMapAndHoveringListObjectJobSkills.put(getCandidatesCountAccordingToStatus(WorkFlowConstants.appliedSelectedRejectedValues,date), 
				getCandidatesListAccordingToStatus(WorkFlowConstants.appliedSelectedRejectedValues));

		finalMap.put("JobSkills", addXYAxisMapAndHoveringListObjectJobSkills);
		
		finalMap.put("JobTitle", addXYAxisMapAndHoveringListObjectJobTitle);
		
		finalMap.put("JobLocation", addXYAxisMapAndHoveringListObjectJobLocation);
		
		return finalMap;

	}
	
	
	
	/**
	 * @author Naga Sreeram
	 * {@summary : Filtering by job location and returning the Count of candiadtes with respective to status }
	 */

	public Map<String,Map<String,Integer>> getAllJobLocationFromDateRange(long inputDate){

		Map<String, Map<String, Integer>> finalMap = new HashMap<>();
		LocalDate startingDate =LocalDate.now().minusWeeks(inputDate);
		LocalDate endDate = LocalDate.now();

		String[] splitIntoSelectSpecificAction = WorkFlowConstants.appliedSelectedRejectedValues.split(",");

		List<String> splitIntoLocation =jobRepository.findAll().stream().map(p->p.getLocation()).toList();

		for(int i=0; i<splitIntoLocation.size(); i++) {
			Map<String,Integer> storageForXandYaxisPlottingValues = new HashMap<>();
			for(int j=0; j<splitIntoSelectSpecificAction.length; j++) {
				List<Candidate> tempStatusList= new ArrayList<>();

				String selectSpecificAction = null;

				switch(splitIntoSelectSpecificAction[j]) {
				case("Applied"):
					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
				break;
				case("Selected"):
					selectSpecificAction = WorkFlowConstants.selectedWorkflowConstants;
				break;
				case("Rejected"):
					selectSpecificAction = WorkFlowConstants.rejectedWorkflowConstants;
				break;
				default:
					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
					break;
				}
				String[] filteredActionInputFromUser = selectSpecificAction.split(",");

				for(int k=0; k<filteredActionInputFromUser.length; k++) {

					List<CandidateFeedback> feedbackList = candidateFeedbackRepository.findBystatus(filteredActionInputFromUser[k]);
					for(CandidateFeedback candidateFeedback:feedbackList) {
						Candidate candidate = candidateRepository.findById(candidateFeedback.getId()).get();
						Job job = jobRepository.findByLocation(splitIntoLocation.get(i));

						if(candidate.getLastModifiedDate().toLocalDate().isAfter(startingDate) && 
								candidate.getLastModifiedDate().toLocalDate().isBefore(endDate)) {
							if(job.getId().contains(candidate.getJobId())) { 

								tempStatusList.add(candidate);
							}
						}

						if(candidate.getUploadedDate().toLocalDate().isAfter(startingDate) && 
								candidate.getUploadedDate().toLocalDate().isBefore(endDate)) {
							if(filteredActionInputFromUser[k].equalsIgnoreCase("Applied")) {
								tempStatusList  =  candidateRepository.findAll().stream()
										.filter(p->p.getJobId().equals(job.getId())).toList();
							}
						}
					}
				}
				storageForXandYaxisPlottingValues.put(splitIntoSelectSpecificAction[j], tempStatusList.size());
			}
			finalMap.put(splitIntoLocation.get(i), storageForXandYaxisPlottingValues);
		}

		System.out.println(finalMap);
		return finalMap;
	}


	/**
	 * @author Naga Sreeram
	 * {@summary : Filtering by job location and returning the list of candiadtes with respective to status }
	 */

	public List<CandidateResponce> getAllCandidateFeedbackIntoList(String action) {
		
		List<Job> splitIntoLocation = jobRepository.findAll();
		
		List<CandidateResponce> listHoldingIndividualInformation = new ArrayList<>();

		String[] splitIntoSelectSpecificAction = action.split(",");

		for(Job job : splitIntoLocation) {

			for(int j=0; j<splitIntoSelectSpecificAction.length; j++) {

				String selectSpecificAction = null;
				switch(splitIntoSelectSpecificAction[j]) {
				case("Applied"):
					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
				break;
				case("Selected"):
					selectSpecificAction = WorkFlowConstants.selectedWorkflowConstants;
				break;
				case("Rejected"):
					selectSpecificAction = WorkFlowConstants.rejectedWorkflowConstants;
				break;
				default:
					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
					break;
				}
				String[] filteredActionInputFromUser = selectSpecificAction.split(",");

				for(int k=0; k<filteredActionInputFromUser.length; k++) {

					List<CandidateFeedback> feedbackList = candidateFeedbackRepository.findBystatus(filteredActionInputFromUser[k]);
					for(CandidateFeedback candidateFeedback:feedbackList) {
						Candidate candidate = candidateRepository.findById(candidateFeedback.getId()).get();
						if(job.getId().contains(candidate.getJobId())) { 
							CandidateResponce candidateResponce = new  CandidateResponce();
							BeanUtils.copyProperties(candidate, candidateResponce);
							listHoldingIndividualInformation.add(candidateResponce);
						}

						if(filteredActionInputFromUser[k].equalsIgnoreCase("Applied")) {
							candidateRepository.findAll().stream()
							.filter(p->p.getJobId().equals(job.getId())).toList()
							.forEach(candidates->{
								CandidateResponce candidateResponce = new  CandidateResponce();
								BeanUtils.copyProperties(candidates, candidateResponce);
								listHoldingIndividualInformation.add(candidateResponce);
							});
						}
					}
				}
			}
		}
		return listHoldingIndividualInformation;
		
		
//		List<Candidate> listHoldingIndividualInformation = new ArrayList<>();
//
//		String[] splitIntoSelectSpecificAction = action.split(",");
//		
//		for(int i=0; i<splitIntoLocation.size(); i++) {
//
//			for(int j=0; j<splitIntoSelectSpecificAction.length; j++) {
//				String selectSpecificAction = null;
//				switch(splitIntoSelectSpecificAction[j]) {
//				case("Applied"):
//					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
//				break;
//				case("Selected"):
//					selectSpecificAction = WorkFlowConstants.selectedWorkflowConstants;
//				break;
//				case("Rejected"):
//					selectSpecificAction = WorkFlowConstants.rejectedWorkflowConstants;
//				break;
//				default:
//					selectSpecificAction = WorkFlowConstants.appliedWorkflowConstants;
//					break;
//				}
//				String[] filteredActionInputFromUser = selectSpecificAction.split(",");
//
//				for(int k=0; k<filteredActionInputFromUser.length; k++) {
//
//					List<CandidateFeedback> feedbackList = candidateFeedbackRepository.findBystatus(filteredActionInputFromUser[k]);
//					for(CandidateFeedback candidateFeedback:feedbackList) {
//						Candidate candidate = candidateRepository.findById(candidateFeedback.getId()).get();
//						Job job = jobRepository.findByLocation(splitIntoLocation.get(i).getLocation());
//						if(job.getId().contains(candidate.getJobId())) { 
//							listHoldingIndividualInformation.add(candidate);
//						}
//					}
//				}
//			}
//		}
//		return listHoldingIndividualInformation;
	}


	/**
	 * @author Preeti Rani Pal
	 * @return List<Candidate>
	 * 
	 * {@summary: this method is to get Count of candidates for job titles against status }
	 * @throws Exception 
	 */

	public Map<String,Map<String,Integer>> getAllJobTitleFromDateRange(long inputDate) throws Exception{

		List<String> jobTitleList=jobRepository.findAll().stream().map(p->p.getJobTitle()).distinct().toList();

		Map<String,Map<String,Integer>> returningMap=new HashMap<>();

		LocalDate startingDate =LocalDate.now().minusWeeks(inputDate);
		LocalDate endDate = LocalDate.now();	

		for(String inputJobTitleObject:jobTitleList) {
			Map<String,Integer> xAndYasisValues=new HashMap<>();
			if(jobTitleList.contains(inputJobTitleObject)){

				List<String> statusList=Arrays.asList(WorkFlowConstants.appliedSelectedRejectedValues.split(","));

				for(String singleStatusObject:statusList) {
					List<String> multipleStatusList=new ArrayList<>();
					List<Candidate> tempStatusList= new ArrayList<>();
					switch(singleStatusObject) {

					case "Applied":
						multipleStatusList.add(WorkFlowConstants.appliedWorkflowConstants);
						break;

					case "Selected":
						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.selectedWorkflowConstants.split(",")));
						break;

					case "Rejected":
						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.rejectedWorkflowConstants.split(",")));
						break;	
					}

					for(String sublistOfstatus:multipleStatusList) {
						List<CandidateFeedback> candidateFeedback=candidateFeedbackRepository.findBystatus(sublistOfstatus);
						List<String> candidateIds =candidateFeedback.stream().map(CandidateFeedback::getId).toList();
						for(String candidateId:candidateIds) {
							Candidate candidate	=candidateRepository.findById(candidateId).get();
							if(candidate.getLastModifiedDate().toLocalDate().isAfter(startingDate)
									&& candidate.getLastModifiedDate().toLocalDate().isBefore(endDate)) {
								if(candidate.getJobTitle().equalsIgnoreCase(inputJobTitleObject)) {
									tempStatusList.add(candidate);
								}
							}

							if(candidate.getUploadedDate().toLocalDate().isAfter(startingDate) && 
									candidate.getUploadedDate().toLocalDate().isBefore(endDate)) {
								if(sublistOfstatus.equals("Applied")) {
									tempStatusList=candidateRepository.findAll().stream().filter(p->p.getJobTitle().equalsIgnoreCase(inputJobTitleObject)).toList();

								}
							}
						}
						xAndYasisValues.put(singleStatusObject, tempStatusList.size());
					}
				}
				returningMap.put(inputJobTitleObject, xAndYasisValues);
			}
			else {
				throw new Exception("Job Title not found");
			}
		}
		return returningMap;

	}

	/**
	 * @author Preeti Rani Pal
	 * @return List<Candidate>
	 * 
	 * {@summary: this method is to get list of candidates for job titles against status }
	 * @throws Exception 
	 */

	public List<CandidateResponce> getListOfCandidatesForJobTitle(String status) throws Exception {
		
		List<String> jobTitleList=jobRepository.findAll().stream().map(p->p.getJobTitle()).toList();

		List<CandidateResponce> returningCandidateList=new ArrayList<>();

		for(String inputJobTitleObject : jobTitleList) {
			if(jobTitleList.contains(inputJobTitleObject)){

				List<String> statusList=Arrays.asList(status.split(","));
				for(String singleStatusObject:statusList) {
					List<String> multipleStatusList=new ArrayList<>();
					List<Candidate> tempStatusList= new ArrayList<>();
					switch(singleStatusObject) {

					case "Applied":
						multipleStatusList.add(WorkFlowConstants.appliedWorkflowConstants);
						break;

					case "Selected":
						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.selectedWorkflowConstants.split(",")));
						break;

					case "Rejected":
						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.rejectedWorkflowConstants.split(",")));
						break;	
					}

					for(String sublistOfstatus:multipleStatusList) {
						List<CandidateFeedback> candidateFeedback=candidateFeedbackRepository.findBystatus(sublistOfstatus);
						List<String> candidateIds =candidateFeedback.stream().map(CandidateFeedback::getId).toList();
						for(String candidateId:candidateIds) {
							Candidate candidate	=candidateRepository.findById(candidateId).get();
							if(candidate.getJobTitle().equalsIgnoreCase(inputJobTitleObject)) {
								tempStatusList.add(candidate);
								CandidateResponce candidateResponce = new  CandidateResponce();
								BeanUtils.copyProperties(candidate, candidateResponce);
								returningCandidateList.add(candidateResponce);
							}
						}
						
						if(sublistOfstatus.equals("Applied")) {
							tempStatusList=candidateRepository.findAll().stream().filter(p->p.getJobTitle().equalsIgnoreCase(inputJobTitleObject)).toList();
							for(Candidate candidate : tempStatusList) {
								CandidateResponce candidateResponce = new  CandidateResponce();
								BeanUtils.copyProperties(candidate, candidateResponce);
								returningCandidateList.add(candidateResponce);
							}
						}
					}
				}
			}
			else {
				throw new Exception("Job Title not found");
			}
			
	}
		return returningCandidateList;
		

//		for(String inputJobTitleObject:jobTitleList) {
//			if(jobTitleList.contains(inputJobTitleObject)){
//
//				List<String> statusList=Arrays.asList(status.split(","));
//				for(String singleStatusObject:statusList) {
//					List<String> multipleStatusList=new ArrayList<>();
//					List<Candidate> tempStatusList= new ArrayList<>();
//					switch(singleStatusObject) {
//
//					case "Applied":
//						multipleStatusList.add(WorkFlowConstants.appliedWorkflowConstants);
//						break;
//
//					case "Selected":
//						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.selectedWorkflowConstants.split(",")));
//						break;
//
//					case "Rejected":
//						multipleStatusList.addAll(Arrays.asList(WorkFlowConstants.rejectedWorkflowConstants.split(",")));
//						break;	
//					}
//
//					for(String sublistOfstatus:multipleStatusList) {
//						List<CandidateFeedback> candidateFeedback=candidateFeedbackRepository.findBystatus(sublistOfstatus);
//						List<String> candidateIds =candidateFeedback.stream().map(CandidateFeedback::getId).toList();
//						for(String candidateId:candidateIds) {
//							Candidate candidate	=candidateRepository.findById(candidateId).get();
//							if(candidate.getJobTitle().equalsIgnoreCase(inputJobTitleObject)) {
//								tempStatusList.add(candidate);
//								returningCandidateList.add(candidate);
//							}
//						}
//
//						if(sublistOfstatus.equals("Applied")) {
//							tempStatusList=candidateRepository.findAll().stream().filter(p->p.getJobTitle().equalsIgnoreCase(inputJobTitleObject)).toList();
//							returningCandidateList.addAll(tempStatusList);
//						}
//					}
//				}
//			}
//			else {
//				throw new Exception("Job Title not found");
//			}
//
//		}
//		return returningCandidateList;

	}

	
	/**
	 * @author Revathi Muddani
	 * @return List<Candidate>
	 * 
	 * {@summary: This method is used to return the Count of candidates according to status and skill set }
	 */
	
	public Map<String, Map<String, Integer>> getCandidatesCountAccordingToStatus(String actions,long inputDateRange) {

		String[] actionArray = actions.split(",");

		List<Job> jobList = jobRepository.findAll();

		Map<String,Map<String,Integer>> allMap = new HashMap<>();

		for(Job jobs : jobList) {

			List<Candidate> candidateList = candidateRepository.findByJobId(jobRepository.findBySkillSet(jobs.getSkillSet()).getId());
			Map<String,Integer> countMap = new HashMap<>();

			for(String action : actionArray) {

				switch (action) {
				case "Applied": countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.appliedWorkflowConstants.split(","),inputDateRange));
				break;

				case "Rejected" : countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.rejectedWorkflowConstants.split(","),inputDateRange));
				break;

				case "Selected" : countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.selectedWorkflowConstants.split(","),inputDateRange));
				break;
				}
			}
			allMap.put(jobs.getSkillSet(), countMap);
		}
		return allMap;
	}

	public Integer getCandidateCount(List<Candidate> candidateList, String[] actions,long inputDateRange) {

		LocalDate startingDate =LocalDate.now().minusWeeks(inputDateRange);
		LocalDate endDate = LocalDate.now();


		List<Candidate> candidatesAction = new ArrayList<>();

		for(String action : actions) {

			if(action.equalsIgnoreCase("Applied")) {
				candidatesAction.addAll(candidateList.stream().filter(candidate->candidate.getUploadedDate().toLocalDate().isAfter(startingDate)
						&& candidate.getUploadedDate().toLocalDate().isBefore(endDate)).toList());
				break;
			}

			candidatesAction.addAll(candidateList.stream().filter(candidate->candidate.getCandidateFeedback().getStatus().equalsIgnoreCase(action))
					.filter(candidate->candidate.getUploadedDate().toLocalDate().isAfter(startingDate)
							&& candidate.getUploadedDate().toLocalDate().isBefore(endDate)).toList());
		}
		return candidatesAction.size();
	}


	/**
	 * @author Revathi Muddani
	 * @return List<Candidate>
	 * 
	 * {@summary: This method is used to return the List of candidates according to status and skill set }
	 */

	public List<CandidateResponce> getCandidatesListAccordingToStatus(String actions) {

		String[] actionArray = actions.split(",");

		List<CandidateResponce> listOfCandidates = new ArrayList<>();

		List<Job> jobList = jobRepository.findAll();

		for(Job jobs : jobList) {

			List<Candidate> candidateList = candidateRepository.findByJobId(jobRepository.findBySkillSet(jobs.getSkillSet()).getId());

			for(String action : actionArray) {

				switch (action) {
				case "Applied": listOfCandidates.addAll(getCandidateList(candidateList, WorkFlowConstants.appliedWorkflowConstants.split(",")));
				break;

				case "Rejected" : listOfCandidates.addAll(getCandidateList(candidateList, WorkFlowConstants.rejectedWorkflowConstants.split(",")));
				break;

				case "Selected" : listOfCandidates.addAll(getCandidateList(candidateList, WorkFlowConstants.selectedWorkflowConstants.split(",")));
				break;
				}
			}
		}
		return listOfCandidates;
	}

	public List<CandidateResponce> getCandidateList(List<Candidate> candidateList, String[] actions) {

		List<CandidateResponce> candidatesResponceAction = new ArrayList<>();

		for(String action : actions) {

			if(action.equalsIgnoreCase("Applied")) {
				for(Candidate candidate : candidateList) {
					CandidateResponce candidateResponce = new  CandidateResponce();
					BeanUtils.copyProperties(candidate, candidateResponce);
					candidatesResponceAction.add(candidateResponce);
				}
			}

			candidateList.stream()
			.filter(candidate->candidate.getCandidateFeedback().getStatus().equalsIgnoreCase(action))
			.forEach(candidates->{
				CandidateResponce candidateResponce = new  CandidateResponce();
				BeanUtils.copyProperties(candidates, candidateResponce);
				candidatesResponceAction.add(candidateResponce);
			});
		}
		return candidatesResponceAction;
	}



}
