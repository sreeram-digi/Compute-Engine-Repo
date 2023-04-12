package com.portal.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.portal.bean.Candidate;
import com.portal.controller.FeedBackController;
 
public class CandidateExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Candidate> listCandidate;
    
    private FeedBackController feedBackController;
     
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    public CandidateExcelExporter(List<Candidate> listCandidate) {
        this.listCandidate = listCandidate;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Candidates");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "ID", style);      
        createCell(row, 1, "firstName", style);       
        createCell(row, 2, "lastName", style);    
        createCell(row, 3, "phoneNumber", style);
        createCell(row, 4, "email", style);
        createCell(row, 5, "jobTitle", style);       
        createCell(row, 6, "currentCtc", style);    
        createCell(row, 7, "expectedCtc", style);
        createCell(row, 8, "noticePeriod", style);
        createCell(row, 9, "resumeUploadedByName", style);
        createCell(row, 10, "resumeUploadedByEmail", style);
        createCell(row, 11, "uploadedDate", style);
        createCell(row, 12, "currentInterviewer", style);
        createCell(row, 13, "currentStatus", style);
        createCell(row, 14, "immediateInterviewDate", style);
        createCell(row, 15, "interviewTime", style);
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (Candidate candidate : listCandidate) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, candidate.getId(), style);     
            createCell(row, columnCount++, candidate.getFirstName(), style);
            createCell(row, columnCount++, candidate.getLastName(), style);
            createCell(row, columnCount++, candidate.getPhoneNumber(), style);
            createCell(row, columnCount++, candidate.getEmail(), style); 
            createCell(row, columnCount++, candidate.getJobTitle(), style); 
            createCell(row, columnCount++, Double.toString(candidate.getCurrentCtc()), style);   
            createCell(row, columnCount++, Double.toString(candidate.getExpectedCtc()), style);
            createCell(row, columnCount++, candidate.getNoticePeriod(), style);
            createCell(row, columnCount++, candidate.getUploadedByName(), style); 
            createCell(row, columnCount++, candidate.getUploadedByEmail(), style); 
            createCell(row, columnCount++, ((candidate.getUploadedDate() == null)?"":formatter.format(candidate.getUploadedDate())), style);
            if(candidate.getCandidateFeedback() != null) {
               createCell(row, columnCount++, candidate.getCandidateFeedback().getInterviewerName(), style); 
               createCell(row, columnCount++, candidate.getCandidateFeedback().getStatus(), style);
               createCell(row, columnCount++, candidate.getCandidateFeedback().getNextInterviewDate(), style);
               createCell(row, columnCount++, candidate.getCandidateFeedback().getNextInterviewTime(), style);
        }
      }
    }
     
    public ByteArrayInputStream export() throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());         
    }
}