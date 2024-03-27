package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.entity.Todo;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    private Logger logger = LoggerFactory.getLogger(PdfService.class);

    public ByteArrayInputStream createPdf(List<Todo> todos, String username) {
        logger.info("Creating the PDF started....");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);

            HeaderFooter footer = new HeaderFooter(true, new Phrase(" LCWD"));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorderWidthBottom(0);
            document.setFooter(footer);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 25, Color.BLUE); // Adjust font color
            Paragraph titlePara = new Paragraph("GoalGlider", titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            document.add(titlePara);

            // Add a gap between title and username
            document.add(new Paragraph("\n"));

            // Add username
            Font userFont = FontFactory.getFont(FontFactory.TIMES, 18, Color.DARK_GRAY); // Adjust font color
            Paragraph userPara = new Paragraph("Username: " + username, userFont);
            userPara.setAlignment(Element.ALIGN_LEFT);
            document.add(userPara);

            // Add a gap between username and table
            document.add(new Paragraph("\n"));

            // Add table for todos
            PdfPTable table = new PdfPTable(4); // 4 columns: Description, Target Date, Status, Total Time
            table.setWidthPercentage(100); // Set table width to 100% of page width

            // Add table headers
            addTableHeader(table);

            // Add todos
            for (Todo todo : todos) {
                addTodoRow(table, todo);
            }

            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        String[] headers = {"Description", "Target Date", "Status", "Total Time"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }

    private void addTodoRow(PdfPTable table, Todo todo) {
        table.addCell(new Phrase(todo.getDescription(), FontFactory.getFont(FontFactory.TIMES, 12))); // Adjust font size
        table.addCell(new Phrase(todo.getTargetDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), FontFactory.getFont(FontFactory.TIMES, 12))); // Adjust font size
        table.addCell(new Phrase(todo.isDone() ? "Done" : "Not Done", FontFactory.getFont(FontFactory.TIMES, 12))); // Adjust font size
        table.addCell(new Phrase(String.valueOf(todo.getTotalTime()), FontFactory.getFont(FontFactory.TIMES, 12))); // Adjust font size
    }
}
