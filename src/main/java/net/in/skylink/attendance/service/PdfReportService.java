package net.in.skylink.attendance.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PdfReportService {

	public byte[] generateAttendanceReport() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20); // Landscape

        PdfWriter.getInstance(document, baos);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        // Header
        Paragraph title = new Paragraph("SkyLink - Attendance Summary Report with Zoho License Assignment", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Paragraph pDate = new Paragraph("Date: " + date, normalFont);
        pDate.setAlignment(Element.ALIGN_CENTER);
        document.add(pDate);

        document.add(Chunk.NEWLINE);

        // Summary Table
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(40);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        summaryTable.setSpacingAfter(10f);
        summaryTable.setWidths(new float[]{3, 2});

        addCell(summaryTable, "Metric", headerFont, BaseColor.LIGHT_GRAY);
        addCell(summaryTable, "Value", headerFont, BaseColor.LIGHT_GRAY);

        addCell(summaryTable, "Total Strength", normalFont);
        addCell(summaryTable, "240", normalFont);

        addCell(summaryTable, "Present", normalFont);
        addCell(summaryTable, "187", normalFont);

        addCell(summaryTable, "Leave", normalFont);
        addCell(summaryTable, "27", normalFont);

        addCell(summaryTable, "Absent", normalFont);
        addCell(summaryTable, "7", normalFont);

        addCell(summaryTable, "Others", normalFont);
        addCell(summaryTable, "Weekly Off / Other Shift: 19", normalFont);

        document.add(summaryTable);
        document.add(Chunk.NEWLINE);

        // Department Table
        PdfPTable deptTable = new PdfPTable(13); // 13 columns
        deptTable.setWidthPercentage(100);
        deptTable.setWidths(new float[]{4,2,2,2,2,2,2,2,3,3,3,5,4});
        deptTable.setHeaderRows(1);

        // Add headers
        String[] headers = {
                "Department", "Actual", "Present", "Leave", "Other Shift",
                "W. Off / Holiday", "Absent", "Total", "Remarks",
                "Zoho Licence Assigned", "Zoho License Required", "Zoho Employee Licence Required",
                "Details of staff on Leave, OS, WO and Absent"
        };
        for (String h : headers) {
            addCell(deptTable, h, headerFont, BaseColor.LIGHT_GRAY);
        }

        // Sample: Sales
        addCell(deptTable, "Sales", normalFont);
        addCell(deptTable, "68", normalFont);
        addCell(deptTable, "51", normalFont);
        addCell(deptTable, "14", normalFont);
        addCell(deptTable, "", normalFont);
        addCell(deptTable, "", normalFont);
        addCell(deptTable, "3", normalFont);
        addCell(deptTable, "68", normalFont);
        addCell(deptTable, "Removed from list", normalFont);
        addCell(deptTable, "55", normalFont);
        addCell(deptTable, "15", normalFont);
        addCell(deptTable, "", normalFont);
        addCell(deptTable, "", normalFont);

        // Customer Care
        addCell(deptTable, "Customer Care", normalFont);
        addCell(deptTable, "32", normalFont);
        addCell(deptTable, "12", normalFont);
        addCell(deptTable, "1", normalFont);
        addCell(deptTable, "12", normalFont);
        addCell(deptTable, "7", normalFont);
        addCell(deptTable, "0", normalFont);
        addCell(deptTable, "32", normalFont);
        addCell(deptTable, "", normalFont);
        addCell(deptTable, "32", normalFont);
        addCell(deptTable, "0", normalFont);
        addCell(deptTable, "", normalFont);
        addCell(deptTable, "Afternoon shift: 1. Arun Prakash, 2. Ajay, 3. Pavithra, 4. Sathiyan, 5. Pooja, 6. Sethu, 7. Suresh, 8. Natrayan, 9. Nandhini, 10. Venkatesh\n" +
                "Night shift: 1. Pavithran, 2. Prabhakaran\n" +
                "Week-off: 1. Janaranjana, 2. Niranjana, 3. Sakthivel, 4. Gokul, 5. Jayashree, 6. Kavi Arasu, 7. Arbin Albert", normalFont);

        // Add other departments similarly...

        document.add(deptTable);

        document.close();
        return baos.toByteArray();
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5f);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(5f);
        table.addCell(cell);
    }
}
