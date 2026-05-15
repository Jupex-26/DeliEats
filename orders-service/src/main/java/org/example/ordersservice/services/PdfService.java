package org.example.ordersservice.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.DetallePedido;
import org.example.ordersservice.models.Pedido;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfService {

    // Paleta de colores de la guía de estilos
    private static final BaseColor COLOR_PRIMARY = new BaseColor(32, 74, 51);     // #204A33
    private static final BaseColor COLOR_SECONDARY = new BaseColor(126, 30, 21);   // #7E1E15
    private static final BaseColor COLOR_TEXT_MAIN = new BaseColor(27, 46, 36);    // #1B2E24
    private static final BaseColor COLOR_BG_BASE = new BaseColor(249, 246, 239);   // #F9F6EF
    private static final BaseColor COLOR_BG_ALT = new BaseColor(218, 215, 208);    // #DAD7D0

    public byte[] generarFactura(Pedido pedido) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Agregar Logo
            try {
                Image logo = Image.getInstance(new ClassPathResource("static/logo.png").getURL());
                logo.scaleToFit(150, 150);
                logo.setAlignment(Element.ALIGN_CENTER);
                logo.setSpacingAfter(20);
                document.add(logo);
            } catch (Exception e) {
                System.err.println("No se pudo cargar el logo: " + e.getMessage());
            }

            // Fuentes personalizadas
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, COLOR_PRIMARY);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COLOR_SECONDARY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_TEXT_MAIN);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, COLOR_TEXT_MAIN);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

            // Título principal
            Paragraph title = new Paragraph("FACTURA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(5);
            document.add(title);
            
            Paragraph subtitle = new Paragraph("Pedido #" + pedido.getId(), subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(30);
            document.add(subtitle);

            // --- SECCIÓN DE INFO (EMPRESA Y CLIENTE) ---
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(30);
            infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Celda Empresa
            PdfPCell cellEmpresa = new PdfPCell();
            cellEmpresa.setBorder(Rectangle.NO_BORDER);
            cellEmpresa.setBackgroundColor(COLOR_BG_BASE);
            cellEmpresa.setPadding(10);
            cellEmpresa.addElement(new Paragraph("EMPRESA", subtitleFont));
            cellEmpresa.addElement(new Paragraph(pedido.getEmpresa().getNombre(), boldFont));
            cellEmpresa.addElement(new Paragraph(pedido.getEmpresa().getDireccion(), normalFont));
            cellEmpresa.addElement(new Paragraph("Tel: " + pedido.getEmpresa().getTelefonoContacto(), normalFont));
            cellEmpresa.addElement(new Paragraph("Email: " + pedido.getEmpresa().getCorreoContacto(), normalFont));
            infoTable.addCell(cellEmpresa);

            // Celda Cliente
            PdfPCell cellCliente = new PdfPCell();
            cellCliente.setBorder(Rectangle.NO_BORDER);
            cellCliente.setBackgroundColor(COLOR_BG_BASE);
            cellCliente.setPadding(10);
            cellCliente.addElement(new Paragraph("FACTURAR A", subtitleFont));
            cellCliente.addElement(new Paragraph(pedido.getCliente().getNombre(), boldFont));
            cellCliente.addElement(new Paragraph(pedido.getCliente().getDireccion(), normalFont));
            cellCliente.addElement(new Paragraph("Tel: " + pedido.getCliente().getTelefono(), normalFont));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            cellCliente.addElement(new Paragraph("Fecha: " + pedido.getFechaCompra().format(formatter), normalFont));
            infoTable.addCell(cellCliente);

            document.add(infoTable);

            // --- TABLA DE PRODUCTOS ---
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 2f, 2f, 2f});
            table.setSpacingBefore(10f);
            table.setSpacingAfter(20f);

            // Cabeceras de la tabla
            String[] headers = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
                cell.setBackgroundColor(COLOR_PRIMARY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                cell.setBorderColor(COLOR_BG_ALT);
                table.addCell(cell);
            }

            // Filas de productos
            boolean isBgAlt = false;
            for (DetallePedido detalle : pedido.getDetalles()) {
                BaseColor bgColor = isBgAlt ? COLOR_BG_ALT : COLOR_BG_BASE;
                
                PdfPCell nameCell = new PdfPCell(new Phrase(detalle.getProducto().getNombre(), normalFont));
                nameCell.setPadding(8);
                nameCell.setBackgroundColor(bgColor);
                nameCell.setBorderColor(COLOR_BG_ALT);
                table.addCell(nameCell);
                
                PdfPCell qtyCell = new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()), normalFont));
                qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                qtyCell.setPadding(8);
                qtyCell.setBackgroundColor(bgColor);
                qtyCell.setBorderColor(COLOR_BG_ALT);
                table.addCell(qtyCell);
                
                PdfPCell priceCell = new PdfPCell(new Phrase(String.format("%.2f €", detalle.getPrecioUnitario()), normalFont));
                priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                priceCell.setPadding(8);
                priceCell.setBackgroundColor(bgColor);
                priceCell.setBorderColor(COLOR_BG_ALT);
                table.addCell(priceCell);
                
                BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
                PdfPCell subtotalCell = new PdfPCell(new Phrase(String.format("%.2f €", subtotal), normalFont));
                subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                subtotalCell.setPadding(8);
                subtotalCell.setBackgroundColor(bgColor);
                subtotalCell.setBorderColor(COLOR_BG_ALT);
                table.addCell(subtotalCell);
                
                isBgAlt = !isBgAlt; // Alternar colores de fondo
            }

            document.add(table);

            // --- TOTAL ---
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{7f, 3f});
            
            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(emptyCell);
            
            PdfPCell totalTextCell = new PdfPCell(new Phrase("PRECIO TOTAL:", titleFont));
            totalTextCell.setBorder(Rectangle.NO_BORDER);
            totalTextCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalTextCell);
            
            totalTable.addCell(emptyCell);
            
            Font totalAmountFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COLOR_SECONDARY);
            PdfPCell totalAmountCell = new PdfPCell(new Phrase(String.format("%.2f €", pedido.getPrecio()), totalAmountFont));
            totalAmountCell.setBorder(Rectangle.NO_BORDER);
            totalAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalAmountCell);

            document.add(totalTable);
            
            // Pie de página
            Paragraph footer = new Paragraph("¡Gracias por su pedido en DeliEats!", subtitleFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(50);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF de la factura", e);
        }

        return out.toByteArray();
    }
}
