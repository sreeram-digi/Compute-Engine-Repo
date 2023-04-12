package com.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.portal.bean.Interviewer;

public interface InterviewerRepository extends MongoRepository<Interviewer, String> {

	Interviewer findByinterviewerName(String userName);

	Optional<Interviewer> findByInterviewerEmailAndInterviewerPassword(String userName, String password);
	
	@Query(value = "{$and:[{_id:{$ne:'Admin'}},{'selector':true}]}",sort = "{'interviewerName': 1}")
	List<Interviewer> findBySelector(boolean selector);

	List<Interviewer> findByInterviewerEmail(String email);
	
	@Query(value = "{$and:[{_id:{$ne:'Admin'}},{'admin':true}]}",sort = "{'interviewerName': 1}")
	List<Interviewer> findByAdmin(boolean admin);
	
	@Query(value = "{$and:[{_id:{$ne:'Admin'}},{'hr':true}]}",sort = "{'interviewerName': 1}")
	List<Interviewer> findByHr(boolean hr);
	
	@Query(value = "{_id:{$ne:'Admin'}}",sort = "{'interviewerName': 1}")
	Page<Interviewer> findAll(Pageable pageable);
	
	@Query(value = "{_id:{$ne:'Admin'}}",sort = "{'interviewerName': 1}")
	List<Interviewer> findAll();
}