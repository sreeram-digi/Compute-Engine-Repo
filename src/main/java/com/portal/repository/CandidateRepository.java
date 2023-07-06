package com.portal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.portal.bean.Candidate;

public interface CandidateRepository extends MongoRepository<Candidate, String> {

	Candidate findByEmail(String value); 
	
	Candidate findByphoneNumber(String value); 
	
	@Query(value ="{$or:[{'email':?0},{'phoneNumber':?1}]}")
	List<Candidate> findByEmailOrPhoneNumber(String email, String phoneNumber);
	
	@Query(value = "{}" ,sort = "{'lastModifiedDate':-1}")
	Page<Candidate> findAllWithPagination(Pageable pageable);
	
	@Query(value = "{$or:[{'upLoadedBy':?0},{'upLoader.$id':?0}]}", sort = "{'lastModifiedDate':-1}")
	Page<Candidate> findAllByUploadedByWithPagination(String uploadedBy,Pageable pageable);
	
	@Query(value = "{}" ,sort = "{'lastModifiedDate':-1}")
	List<Candidate> findAll();
	
	@Query(value = "{$or:[{'upLoadedBy':?0},{'upLoader.$id':?0}]}" ,sort = "{'lastModifiedDate':-1}")
	List<Candidate> findAllUploadedBy(String uploadedBy);
	
	@Query(value = "{'uploadedDate':{$gte:?0,$lte:?1}}",sort = "{'uploadedDate':-1}")
	List<Candidate> getAllBetweenDates(Date startDate, Date endDate);
	
	List<Candidate> findByJobId(String jobId);
	
	List<Candidate> findByJobTitle(String jobtitle);
}

