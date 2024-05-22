package org.highfives.grid.approval.query.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service(value = "QueryApprovalService")
public class ApprovalServiceImpl implements ApprovalService{

    private final ModelMapper mapper;
    private final BTApprovalRepository btApprovalRepository;

    @Autowired
    public ApprovalServiceImpl(ModelMapper mapper, BTApprovalRepository btApprovalRepository) {
        this.mapper = mapper;
        this.btApprovalRepository = btApprovalRepository;
    }

    @Override
    public BTApprovalDTO findBTApprovalById(int btApprovalId) {

        BTApproval btApproval = btApprovalRepository.findById(btApprovalId).orElseThrow();
        if (btApproval.getApprovalStatus() == ApprovalStatus.A) return mapper.map(btApproval, BTApprovalDTO.class);

        return null;
    }

    @Override
    public void exportToPDF(BTApprovalDTO btApproval, String filePath) {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            String fontPath = "C:/Users/Playdata/Documents/be04-fin-5team-GRID/grid/src/main/resources/fonts/Sejong hospital Light.ttf";
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font font = new Font(bf, 12);
            Font tableFont = new Font(bf, 12, Font.BOLD);
            Font titleFont = new Font(bf, 18, Font.BOLD);

            Paragraph title = new Paragraph("출장 신청서", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(title);
            document.add(new Paragraph("\n\n", font));

            PdfPTable table = new PdfPTable(2);

            PdfPCell t_cell1 = new PdfPCell(new Paragraph("시작 날짜", tableFont));
            PdfPCell t_cell2 = new PdfPCell(new Paragraph("종료 날짜", tableFont));
            PdfPCell t_cell3 = new PdfPCell(new Paragraph("출장지", tableFont));
            t_cell1.setBackgroundColor(Color.LIGHT_GRAY);
            t_cell2.setBackgroundColor(Color.LIGHT_GRAY);
            t_cell3.setBackgroundColor(Color.LIGHT_GRAY);

            table.addCell(t_cell1);
            table.addCell(btApproval.getStartTime().substring(0, 10));

            table.addCell(t_cell2);
            table.addCell(btApproval.getEndTime().substring(0, 10));

            table.addCell(t_cell3);
            table.addCell(btApproval.getDestination());

            document.add(table);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }
}
