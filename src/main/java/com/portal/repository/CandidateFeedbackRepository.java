package com.portal.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.portal.bean.CandidateFeedback;

public interface CandidateFeedbackRepository extends MongoRepository<CandidateFeedback, String> {
	
	@Query(value = " {$and:[{'currentInterviewId':?0},{'status':{$nin:['NotShortListed','NotSelectedRound1','NotSelectedRound2','SelectedRound1','SelectedRound2']} }]} ", fields="{'_id':1}")
	List<CandidateFeedback> getCandidateByInterviewerId(String id);
	
	@Query(value = "{'status':{$in:?0}}", fields="{'_id':1}")
	List<CandidateFeedback> findAllBystatus(Collection<String> status);
}
