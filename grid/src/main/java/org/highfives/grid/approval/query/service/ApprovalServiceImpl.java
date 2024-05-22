package org.highfives.grid.approval.query.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service(value = "QueryApprovalService")
public class ApprovalServiceImpl implements ApprovalService{

    private final BTApprovalRepository btApprovalRepository;

    @Autowired
    public ApprovalServiceImpl(BTApprovalRepository btApprovalRepository) {
        this.btApprovalRepository = btApprovalRepository;
    }



    @Override
    public BTApprovalDTO findBTApprovalById(int btApprovalId) {
        return null;
    }

    public void exportToPDF(BTApproval btApproval, String filePath) {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            String fontPath = "path/to/NanumGothic.ttf"; // 폰트 파일 경로
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12);

            PdfPTable table = new PdfPTable(2);

            table.addCell(new Paragraph("시작시간", font));
            table.addCell(btApproval.getStartTime());

            table.addCell(new Paragraph("종료시간", font));
            table.addCell(btApproval.getEndTime());

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
