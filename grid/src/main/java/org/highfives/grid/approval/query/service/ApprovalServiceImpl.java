package org.highfives.grid.approval.query.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;
import org.highfives.grid.approval.query.repository.ApprovalMapper;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service(value = "QueryApprovalService")
public class ApprovalServiceImpl implements ApprovalService{

    private final ModelMapper mapper;
    private final ApprovalMapper approvalMapper;
    private final BTApprovalRepository btApprovalRepository;
    private final UserService userService;

    @Autowired
    public ApprovalServiceImpl(ModelMapper mapper, ApprovalMapper approvalMapper, BTApprovalRepository btApprovalRepository, UserService userService) {
        this.mapper = mapper;
        this.approvalMapper = approvalMapper;
        this.btApprovalRepository = btApprovalRepository;
        this.userService = userService;
    }

    @Override
    public List<BTApprovalDTO> findAllBTApproval() {
        return approvalMapper.findAllBTApproval();
    }

    @Override
    public List<ApprovalEmpDTO> findAllApprovalByEmployeeId(int typeId, int employeeId) {

        List<ApprovalEmpDTO> approvalEmpList = new ArrayList<>();

        switch (typeId) {
            case 1:
                approvalEmpList = approvalMapper.findAllBTApprovalByEmployeeId(employeeId);
                break;

            case 2:
                approvalEmpList = approvalMapper.findAllOApprovalByEmployeeId(employeeId);
                break;

            case 3:
                approvalEmpList = approvalMapper.findAllRWApprovalByEmployeeId(employeeId);
                break;

            case 4:
                approvalEmpList = approvalMapper.findAllVApprovalByEmployeeId(employeeId);
        }

        return approvalEmpList;
    }

    @Override
    public ApprovalEmpDTO findDetailByApprovalId(int typeId, int approvalId) {

        ApprovalEmpDTO approvalEmp = new ApprovalEmpDTO();

        switch (typeId) {
            case 1:
                approvalEmp = approvalMapper.findBTDetailByApprovalId(approvalId);
                break;

            case 2:
                approvalEmp = approvalMapper.findODetailByApprovalId(approvalId);
                break;

            case 3:
                approvalEmp = approvalMapper.findRWDetailByApprovalId(approvalId);
                break;

            case 4:
                approvalEmp = approvalMapper.findVDetailByApprovalId(approvalId);
                break;
        }

        UserDTO user = userService.findUserById(approvalEmp.getEmployeeId());
        approvalEmp.setUser(user);

        return approvalEmp;
    }

    @Override
    public int countOvertimeInWeek(OvertimeInWeekDTO overtimeInWeek) {

        List<OvertimeApprovalDTO> overtimeApprovalList = approvalMapper.findOInWeekByEmployeeId(overtimeInWeek);

        System.out.println(overtimeApprovalList);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int sum = 0;

        for (OvertimeApprovalDTO overtimeApproval : overtimeApprovalList) {
            LocalDateTime startTime = LocalDateTime.parse(overtimeApproval.getStartTime(), dateFormat);
            LocalDateTime endTime = LocalDateTime.parse(overtimeApproval.getEndTime(), dateFormat);

            sum += ChronoUnit.HOURS.between(startTime, endTime);
            System.out.println(startTime + " " + endTime);
        }

        return sum;
    }

    @Override
    public void BTexportToPDF(BTApprovalDTO btApproval, String filePath) {

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
