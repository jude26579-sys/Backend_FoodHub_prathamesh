package com.cts.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.dtos.OrderReportEntryDto;
import com.cts.dtos.OverallReportDto;
import com.cts.dtos.RestaurantReportDto;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfService {
    public byte[] generateReportPdf(OverallReportDto dto) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Report ID: " + dto.getReportId()));
            document.add(new Paragraph("Duration: " + dto.getDuration()));
            document.add(new Paragraph("Total Sales: " + dto.getTotalSales()));
            document.add(new Paragraph("Restaurant Count: " + dto.getRestaurantCount()));
            document.add(new Paragraph("Order Count: " + dto.getOrderCount()));
            document.add(new Paragraph("Generated At: " + dto.getReportGeneratedAt()));

            for (RestaurantReportDto restaurant : dto.getRestaurants()) {
                document.add(new Paragraph("Restaurant ID: " + restaurant.getRestaurantId()));
                document.add(new Paragraph("Orders:"));
                for (OrderReportEntryDto order : restaurant.getOrders()) {
                    document.add(new Paragraph(" - Order ID: " + order.getOrderId() +
                            ", Amount: " + order.getOrderSalesAmt() +
                            ", Status: " + order.getOrderStatus() +
                            ", Created At: " + order.getCreatedAt()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return out.toByteArray();
    }
    
    public byte[] generateAllReportsPdf(List<OverallReportDto> reports) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("All Reports"));
            document.add(new Paragraph("===================================="));

            for (OverallReportDto dto : reports) {
                document.add(new Paragraph("Report ID: " + dto.getReportId()));
                document.add(new Paragraph("Duration: " + dto.getDuration()));
                document.add(new Paragraph("Total Sales: " + dto.getTotalSales()));
                document.add(new Paragraph("Restaurant Count: " + dto.getRestaurantCount()));
                document.add(new Paragraph("Order Count: " + dto.getOrderCount()));
                document.add(new Paragraph("------------------------------------"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return out.toByteArray();
    }
}