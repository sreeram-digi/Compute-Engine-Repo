package com.portal;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileDeletionConfig {

	static int count=1;

	@Value("${resume.paths}")
	private String folderPath;

	@Value("${reserved.path}")
	private String reservedPath;

	/**
	 * While working on @scheduled annotation,its a method level annotation
	 * is used to exceute the below task continiously, very important to mention 
	 * @EnableScheduling which is a class level annotation, in the main class
	 * fixedDelay method delays the task for 3seconds and then executes the task. Hence for every 3s it waiting and then executes.
	 * fixedRate method executes task for mentioned milliseconds.
	 * 
	 */
	@Scheduled(cron = "0 0 0 * * ?") // Executes at 12:00 AM (midnight) every day
	public  void running() {

		log.info("task in its "+ count+ "iteration");

		List<File> foldersList = Arrays.asList(new File(folderPath).listFiles());
		deleteFolder(foldersList);

		log.info("Folder deleted");
		count++;
	} 

	/**
	 * delete folder() is helping us to iterate over all the files inside the folder, checking if the files are one year old
	 * and then deleting the files, if condition fails then it will not delete.
	 * @param folder
	 * @param savingFileName
	 */

	private  void deleteFolder(List<File> foldersList) {

		for(File file : foldersList) {

			if(file.isDirectory()) {

				for (File fileObject : file.listFiles()) {

					LocalDate fileCreationDate = new Date(fileObject.lastModified()).toLocalDate();
					LocalDate fileDeletionDate = fileCreationDate.plusYears(1);
					if(fileDeletionDate.equals(LocalDate.now())) {
						fileObject.delete();
						log.info("Folder deleted");
					}	
				}
			}else {
				log.info(file +"  is not a directory");
			}
		}
	}


}
