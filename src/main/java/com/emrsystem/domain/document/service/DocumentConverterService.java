package com.emrsystem.domain.document.service;

import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentConverterService {

    /**
     * HTML을 PDF로 변환
     */
    public Resource convertHtmlToPdf(String htmlContent, String templateName) throws IOException {
        // OpenPDF를 사용한 PDF 생성
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, baos);

        document.open();

        // HTML 파싱 및 PDF 변환
        org.jsoup.nodes.Document doc = Jsoup.parse(htmlContent);
        Elements paragraphs = doc.select("p, h1, h2, h3, h4, h5, h6");

        for (Element element : paragraphs) {
            com.lowagie.text.Paragraph para = new com.lowagie.text.Paragraph(element.text());
            document.add(para);
        }

        // 테이블 처리
        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            com.lowagie.text.pdf.PdfPTable pdfTable = new com.lowagie.text.pdf.PdfPTable(
                    rows.isEmpty() ? 1 : rows.get(0).select("td, th").size());

            for (Element row : rows) {
                Elements cells = row.select("td, th");
                for (Element cell : cells) {
                    pdfTable.addCell(cell.text());
                }
            }
            document.add(pdfTable);
        }

        document.close();

        return new ByteArrayResource(baos.toByteArray());
    }

    /**
     * HTML을 Word로 변환
     */
    public Resource convertHtmlToWord(String htmlContent, String templateName) throws IOException {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        org.jsoup.nodes.Document doc = Jsoup.parse(htmlContent);

        // 제목 처리
        Elements headings = doc.select("h1, h2, h3, h4, h5, h6");
        for (Element heading : headings) {
            XWPFParagraph para = document.createParagraph();
            XWPFRun run = para.createRun();
            run.setText(heading.text());
            run.setBold(true);
            run.setFontSize(getHeadingSize(heading.tagName()));
        }

        // 문단 처리
        Elements paragraphs = doc.select("p");
        for (Element p : paragraphs) {
            XWPFParagraph para = document.createParagraph();
            XWPFRun run = para.createRun();
            run.setText(p.text());
        }

        // 테이블 처리
        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            org.apache.poi.xwpf.usermodel.XWPFTable wordTable = document.createTable(rows.size(),
                    rows.isEmpty() ? 1 : rows.get(0).select("td, th").size());

            int rowIndex = 0;
            for (Element row : rows) {
                Elements cells = row.select("td, th");
                org.apache.poi.xwpf.usermodel.XWPFTableRow wordRow = wordTable.getRow(rowIndex);
                for (int i = 0; i < cells.size() && i < wordRow.getTableCells().size(); i++) {
                    wordRow.getCell(i).setText(cells.get(i).text());
                }
                rowIndex++;
            }
        }

        document.write(baos);
        document.close();

        return new ByteArrayResource(baos.toByteArray());
    }

    /**
     * HTML을 Excel로 변환
     */
    public Resource convertHtmlToExcel(String htmlContent, String templateName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(templateName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        org.jsoup.nodes.Document doc = Jsoup.parse(htmlContent);
        int rowNum = 0;

        // 테이블 처리
        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Row excelRow = sheet.createRow(rowNum++);
                Elements cells = row.select("td, th");
                int cellNum = 0;
                for (Element cell : cells) {
                    Cell excelCell = excelRow.createCell(cellNum++);
                    excelCell.setCellValue(cell.text());
                }
            }
            rowNum++; // 테이블 간 간격
        }

        // 테이블이 없는 경우 텍스트를 셀에 추가
        if (tables.isEmpty()) {
            Elements paragraphs = doc.select("p, h1, h2, h3, h4, h5, h6");
            for (Element p : paragraphs) {
                Row excelRow = sheet.createRow(rowNum++);
                Cell cell = excelRow.createCell(0);
                cell.setCellValue(p.text());
            }
        }

        workbook.write(baos);
        workbook.close();

        return new ByteArrayResource(baos.toByteArray());
    }

    private int getHeadingSize(String tagName) {
        return switch (tagName.toLowerCase()) {
            case "h1" -> 24;
            case "h2" -> 20;
            case "h3" -> 16;
            case "h4" -> 14;
            case "h5" -> 12;
            case "h6" -> 10;
            default -> 12;
        };
    }
}

