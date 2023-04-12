package com.portal.service.impl;

import org.springframework.stereotype.Service;

import com.portal.bean.DemoPojo;
import com.portal.repository.DemoRepository;
import com.portal.service.DemoService;


@Service
public class DemoServiceImpl implements DemoService {

	DemoRepository demoRepository;

	public DemoServiceImpl(DemoRepository demoRepository) {
		super();
		this.demoRepository = demoRepository;
	}



	@Override
	public DemoPojo findById(String id) {
		
		return demoRepository.findById(id).get();
	}

	
}
