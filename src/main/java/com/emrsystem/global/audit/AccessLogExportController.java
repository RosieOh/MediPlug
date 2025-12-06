package com.emrsystem.global.audit;

import com.emrsystem.global.common.controller.BaseController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@RestController
@RequestMapping("/audit/export")
@RequiredArgsConstructor
public class AccessLogExportController extends BaseController {

    private final AccessLogRepository accessLogRepository;

    @GetMapping(value = "/csv", produces = "text/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        List<AccessLog> logs = accessLogRepository.findAll();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=access_logs.csv");
        var writer = response.getWriter();
        writer.println("id,username,resource,action,details,accessedAt");
        for (AccessLog l : logs) {
            writer.printf("%d,%s,%s,%s,%s,%s%n",
                    l.getId(), l.getUsername(), l.getResource(), l.getAction(),
                    safe(l.getDetails()), l.getAccessedAt());
        }
        writer.flush();
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws IOException, DocumentException {
        List<AccessLog> logs = accessLogRepository.findAll();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=access_logs.pdf");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("Access Logs"));
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        addHeader(table);
        for (AccessLog l : logs) {
            table.addCell(String.valueOf(l.getId()));
            table.addCell(l.getUsername());
            table.addCell(l.getResource());
            table.addCell(l.getAction());
            table.addCell(safe(l.getDetails()));
            table.addCell(String.valueOf(l.getAccessedAt()));
        }
        document.add(table);
        document.close();
    }

    private void addHeader(PdfPTable table) {
        table.addCell(new PdfPCell(new Phrase("id")));
        table.addCell(new PdfPCell(new Phrase("username")));
        table.addCell(new PdfPCell(new Phrase("resource")));
        table.addCell(new PdfPCell(new Phrase("action")));
        table.addCell(new PdfPCell(new Phrase("details")));
        table.addCell(new PdfPCell(new Phrase("accessedAt")));
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replaceAll(",", " ").replaceAll("\n", " ");
    }
}


