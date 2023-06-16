package com.portal;

import java.io.File;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;

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
	@Scheduled(fixedDelay= 60000)
	public  void running() {

		log.info("task in its "+ count+ "iteration");

		String savingFileName="cb569050-6a56-41c8-b288-d70951730105.xlsx";
		File folder = new File(folderPath);

		if (folder.exists()) {
			deleteFolder(folder,savingFileName);
			log.info("Folder deleted");
		} else {
			log.error("Folder doesnot exists");
		}
		count++;
	} 

	/**
	 * delete folder() is helping us to iterate over all the files inside the folder, checking if the files are one year old
	 * and then deleting the files, if condition fails then it will not delete.
	 * @param folder
	 * @param savingFileName
	 */

	private  void deleteFolder(File folder,String savingFileName) {

		String folderPath = reservedPath+savingFileName;
		File[] files = folder.listFiles();

		if (files != null) {

			for (File fileObject : files) {
				Path path= Path.of(fileObject.toString());

				if (fileObject.isDirectory()) {	

					deleteFolder(fileObject,savingFileName);
				}

				else {
					LocalDate fileCreationDate=	new Date(fileObject.lastModified()).toLocalDate();

					LocalDate fileDeletionDate = fileCreationDate.plusYears(1);

					if(fileObject.getName().equals(savingFileName)) {

						log.info("File name matched moving to reserved folder"+folderPath);

						fileObject.renameTo(new File((folderPath)));

						if(fileDeletionDate.equals(LocalDate.now())) {

							fileObject.delete();

							log.info("Folder deleted");
						}
						else {
							log.error("Deletion date not arraived");
						}
					}
				}	
			} 

		}


	}

}
