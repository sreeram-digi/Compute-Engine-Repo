package com.knf.dev.demo.serviceImpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.knf.dev.demo.entity.Hello;
import com.knf.dev.demo.repository.HelloRepository;
import com.knf.dev.demo.service.HelloService;

@Service
public class HelloServiceImpl implements HelloService {

	private HelloRepository helloRepository;

	public HelloServiceImpl(HelloRepository helloRepository) {
		super();
		this.helloRepository = helloRepository;
	}

	@Override
	public List<Hello> findAll() {
		// TODO Auto-generated method stub
		List<Hello> hello = helloRepository.findAll();
		return hello;
		
	}

	@Override
	public ResponseEntity<String> findById(String id) {
		// TODO Auto-generated method stub
		Hello hello= helloRepository.findById(id).get();
		if(hello==null) {
			return new ResponseEntity<String>(" Id not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(hello,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> findByName(String name) {
		// TODO Auto-generated method stub
		Hello hello = helloRepository.findByName(name);
		if(hello==null) {
			return new ResponseEntity<String>("Name not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(hello,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> save(Hello hello) {
		// TODO Auto-generated method stub
		
helloRepository.save(hello);
		return new ResponseEntity(hello,HttpStatus.OK);
	}

}
