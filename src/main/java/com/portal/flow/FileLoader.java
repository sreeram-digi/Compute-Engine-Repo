package com.portal.flow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileLoader {

	public JSONObject readWorkFlowJson() throws IOException {

		Resource res = new ClassPathResource("flow/flow.json");

		InputStream file = res.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(file));

		StringBuffer stBf = new StringBuffer();
		String st;
		while ((st = br.readLine()) != null) {
			stBf.append(st);
		}

		JSONObject json = new JSONObject(stBf.toString());
		log.info("----->"+json);
		return json;
	}
}
