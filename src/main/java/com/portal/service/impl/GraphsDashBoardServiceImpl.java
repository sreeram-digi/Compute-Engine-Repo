package com.portal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.WorkFlowConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.Interviewer;
import com.portal.bean.Job;
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
	 * @author Naga Sreeram
	 * {@summary : Filtering by job location and returning the count with respective to status }
	 */

	@Override
	public Map<String,Map<String, Long>> getCandidatesCountByJobLocation(String action, String locationCandidateCount) {

		Map<String,Map<String, Long>> finalMap = new HashMap<>();

		String[] splitIntoSelectSpecificAction = action.split(",");
		String[] splitIntoLocation = locationCandidateCount.split(",");

		for(int i=0; i<splitIntoLocation.length; i++) {
			Map<String,Long> storageForXandYaxisPlottingValues = new HashMap<>();
			for(int j=0; j<splitIntoSelectSpecificAction.length; j++) {
				String selectSpecificAction = null;
				long count = 0;
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
						Job job = jobRepository.findByLocation(splitIntoLocation[i]);
						if(job.getId().contains(candidate.getJobId())) { 
							count++;
						}
						if(filteredActionInputFromUser[k].equalsIgnoreCase("Applied")) {
							count =  candidateRepository.findAll().stream()
									.filter(p->p.getJobId().equals(job.getId())).count();
						}
					}
				}
				storageForXandYaxisPlottingValues.put(splitIntoSelectSpecificAction[j], count);
			}
			finalMap.put(splitIntoLocation[i], storageForXandYaxisPlottingValues);
		}
		return finalMap;
	}


	/**
	 * @author Naga Sreeram
	 * {@summary : Filtering by job location and returning the list of candiadtes with respective to status }
	 */

	@Override
	public List<Candidate> getAllCandidateFeedbackIntoList(String action, String locationCandidateCount) {

		List<Candidate> listHoldingIndividualInformation = new ArrayList<>();

		String[] splitIntoSelectSpecificAction = action.split(",");
		String[] splitIntoLocation = locationCandidateCount.split(",");

		for(int i=0; i<splitIntoLocation.length; i++) {

			for(int j=0; j<splitIntoSelectSpecificAction.length; j++) {
				String selectSpecificAction = null;
				int count = 0;
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
						Job job = jobRepository.findByLocation(splitIntoLocation[i]);
						if(job.getId().contains(candidate.getJobId())) { 
							listHoldingIndividualInformation.add(candidate);
						}
					}
				}
			}
		}
		return listHoldingIndividualInformation;
	}


	/**
	 * @author Naga Sreeram
	 * {@summary : This methods gets data by job location and returning the list of job location }
	 */

	@Override
	public List<String> getAllJobPostedLocation() {

		return jobRepository.findAll().stream()
				.map(Job::getLocation).distinct().toList();
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

	/**
	 * @author Preeti Rani Pal
	 * @return List<Candidate>
	 * 
	 * {@summary : The method is to get list of candidates rating  }
	 */

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


	/**
	 * @author Preeti Rani Pal
	 * @return List<String>
	 * 
	 * {@summary: this method is to get all job titles for dropdown  }
	 */

	@Override
	public List<String> getAllJobTitlesForDropDown(){
		return candidateRepository.findAll().stream().map(p->p.getJobTitle()).distinct().toList();
	}


	/**
	 * @author Preeti Rani Pal
	 * @return Map<String,Map<String,Integer>>
	 * 
	 * {@summary: this method is to get all job titles against status }
	 */
	
	@Override
	public Map<String,Map<String,Integer>> getDataForJobTitleAgainstStatus(String inputJobTiltles, String status) throws Exception{

		Map<String,Map<String,Integer>> finalMap=new HashMap<>();

		List<String> jobTitleList=jobRepository.findAll().stream().map(p->p.getJobTitle()).distinct().toList();

		for(String inputJobTitleObject:Arrays.asList(inputJobTiltles.split(","))) {
			Map<String,Integer> xAndYasisValues=new HashMap<>();
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
							}
						}
						if(sublistOfstatus.equals("Applied")) {
							tempStatusList=candidateRepository.findAll().stream().filter(p->p.getJobTitle().equalsIgnoreCase(inputJobTitleObject)).toList();

						}
						xAndYasisValues.put(singleStatusObject, tempStatusList.size());
					}
				}
				finalMap.put(inputJobTitleObject, xAndYasisValues);
			}
			else {
				throw new Exception("Job Title not found");
			}
		}
		return finalMap;
	}


	/**
	 * @author Preeti Rani Pal
	 * @return List<Candidate>
	 * 
	 * {@summary: this method is to get list of candidates for job titles against status }
	 */

	@Override
	public List<Candidate> getListOfCandidatesForJobTitle(String inputJobtitle) {

		List<String> jobTitleList=jobRepository.findAll().stream().map(p->p.getJobTitle()).distinct().toList();
		List<Candidate> returningCandidateList=new ArrayList<>();
		for(String inputJobTitleObject:Arrays.asList(inputJobtitle.split(","))) {
			if(jobTitleList.contains(inputJobTitleObject)){

				List<String> statusList=Arrays.asList(inputJobTitleObject.split(","));
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
								returningCandidateList.add(candidate);
							}
						}
						if(sublistOfstatus.equals("Applied")) {
							tempStatusList=candidateRepository.findAll().stream().filter(p->p.getJobTitle().equalsIgnoreCase(inputJobTitleObject)).toList();
							returningCandidateList.addAll(tempStatusList);
						}
					}
				}
			}
		}
		return returningCandidateList;
	}

	
	/**
	 * @author Revathi Muddani
	 * @return Map<String, Integer>
	 * 
	 * {@summary: This method is used to get count of HR's and Selectors  }
	 */

	@Override
	public Map<String, Integer> getHrAndSelectorCount(String position) {

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
	
	/**
	 * @author Revathi Muddani
	 * @return List<String>
	 * 
	 * {@summary: This method is to get all data for job skills  }
	 */
	
	@Override
	public List<String> getAllSkillsFromJob() {
		List<Job> getAllInformationFromJob = jobRepository.findAll();
		List<String> getAllJobBySkill = getAllInformationFromJob.stream().map(Job::getSkillSet).toList();
		return getAllJobBySkill;
	}

	
	/**
	 * @author Revathi Muddani
	 * @return Map<String, Integer>
	 * 
	 * {@summary: This method is used to return the count of candidates according to status and skill set }
	 */

	@Override
	public Map<String, Map<String, Integer>> getCandidatesCountAccordingToStatus(String actions, String skills) {

		String[] actionArray = actions.split(",");
		String[] skillArray = skills.split(",");

		
		Map<String,Map<String,Integer>> allMap = new HashMap<>();

		for(String skill : skillArray) {

			Job job = jobRepository.findBySkillSet(skill);
			List<Candidate> candidateList = candidateRepository.findByJobId(job.getId());
			Map<String,Integer> countMap = new HashMap<>();
			
			for(String action : actionArray) {

				switch (action) {
				case "Applied": countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.appliedWorkflowConstants.split(",")));
				break;

				case "Rejected" : countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.rejectedWorkflowConstants.split(",")));
				break;

				case "Selected" : countMap.put(action, getCandidateCount(candidateList, WorkFlowConstants.selectedWorkflowConstants.split(",")));
				break;
				}
			}
			allMap.put(skill, countMap);
		}
		return allMap;
	}

	public Integer getCandidateCount(List<Candidate> candidateList, String[] actions) {

		List<Candidate> candidatesAction = new ArrayList<>();

		for(String action : actions) {

			if(action.equalsIgnoreCase("Applied")) {
				candidatesAction.addAll(candidateList);
				break;
			}

			for(Candidate candidate : candidateList) {
				if(candidate.getCandidateFeedback().getStatus().equalsIgnoreCase(action)) {
					candidatesAction.add(candidate);
				}
			}
		}
		return candidatesAction.size();
	}


	/**
	 * @author Revathi Muddani
	 * @return List<Candidate>
	 * 
	 * {@summary: This method is used to return the List of candidates according to status and skill set }
	 */

	@Override
	public List<Candidate> getCandidatesListAccordingToStatus(String actions, String skills) {

		String[] actionArray = actions.split(",");
		String[] skillArray = skills.split(",");

		List<Candidate> listOfCandidates = new ArrayList<>();

		for(String skill : skillArray) {

			Job job = jobRepository.findBySkillSet(skill);
			List<Candidate> candidateList = candidateRepository.findByJobId(job.getId());

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

	public List<Candidate> getCandidateList(List<Candidate> candidateList, String[] actions) {

		List<Candidate> candidatesAction = new ArrayList<>();

		for(String action : actions) {

			if(action.equalsIgnoreCase("Applied")) {
				candidatesAction.addAll(candidateList);
				break;
			}
			
			for(Candidate candidate : candidateList) {
				if(candidate.getCandidateFeedback().getStatus().equalsIgnoreCase(action)) {
					candidatesAction.add(candidate);
				}
			}
		}
		return candidatesAction;

	@Override
	public List<Interviewer> getListOfData(String position) {
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
