package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.entity.Todo;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

            Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 25);
            Paragraph titlePara = new Paragraph("GoalGlider", titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            document.add(titlePara);

            // Add username
            Font userFont = FontFactory.getFont(FontFactory.TIMES, 18);
            Paragraph userPara = new Paragraph("Username: " + username, userFont);
            userPara.setAlignment(Element.ALIGN_LEFT);
            document.add(userPara);

            // Add todos
            for (Todo todo : todos) {
                Font todoFont = FontFactory.getFont(FontFactory.TIMES, 18);
                Paragraph todoPara = new Paragraph("Description: " + todo.getDescription(), todoFont);
                todoPara.setAlignment(Element.ALIGN_LEFT);
                document.add(todoPara);

                Paragraph targetDatePara = new Paragraph("Target Date: " + todo.getTargetDate(), todoFont);
                targetDatePara.setAlignment(Element.ALIGN_LEFT);
                document.add(targetDatePara);

                // Add more details as needed
                Paragraph statusPara = new Paragraph("Status: " + (todo.isDone() ? "Done" : "Not Done"), todoFont);
                statusPara.setAlignment(Element.ALIGN_LEFT);
                document.add(statusPara);

                // Add some space between todos
                document.add(new Paragraph("\n"));
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
