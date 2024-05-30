package org.highfives.grid.approval.query.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.highfives.grid.approval.command.aggregate.OvertimeApproval;
import org.highfives.grid.approval.command.aggregate.RWApproval;
import org.highfives.grid.approval.command.aggregate.VacationApproval;
import org.highfives.grid.approval.command.repository.BTApprovalRepository;
import org.highfives.grid.approval.command.repository.OApprovalRepository;
import org.highfives.grid.approval.command.repository.RWApprovalRepository;
import org.highfives.grid.approval.command.repository.VApprovalRepository;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.vacation.query.service.VacationService;
import org.highfives.grid.vacation.query.vo.ResOneVacationTypeVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    private final ModelMapper mapper;
    private final BTApprovalRepository btApprovalRepository;
    private final OApprovalRepository oApprovalRepository;
    private final RWApprovalRepository rwApprovalRepository;
    private final VApprovalRepository vApprovalRepository;
    private final UserService userService;
    private final VacationService vacationService;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String date = LocalDateTime.now().format(dateFormat);

    @Autowired
    public PdfServiceImpl(ModelMapper mapper, BTApprovalRepository btApprovalRepository, OApprovalRepository oApprovalRepository, RWApprovalRepository rwApprovalRepository, VApprovalRepository vApprovalRepository, UserService userService, VacationService vacationService) {
        this.mapper = mapper;
        this.btApprovalRepository = btApprovalRepository;
        this.oApprovalRepository = oApprovalRepository;
        this.rwApprovalRepository = rwApprovalRepository;
        this.vApprovalRepository = vApprovalRepository;
        this.userService = userService;
        this.vacationService = vacationService;
    }

    @Override
    public void exportToPDF(int typeId, int approvalId) {

        switch (typeId) {
            case 1:
                BTApproval btApproval = btApprovalRepository.findById(approvalId).orElseThrow();
                BTexportToPDF(mapper.map(btApproval, BTApprovalDTO.class));
                break;

            case 2:
                OvertimeApproval overtimeApproval = oApprovalRepository.findById(approvalId).orElseThrow();
                OexportToPDF(mapper.map(overtimeApproval, OvertimeApprovalDTO.class));
                break;

            case 3:
                RWApproval rwApproval = rwApprovalRepository.findById(approvalId).orElseThrow();
                RWexportToPDF(mapper.map(rwApproval, RWApprovalDTO.class));
                break;

            case 4:
                VacationApproval vacationApproval = vApprovalRepository.findById(approvalId).orElseThrow();
                VexportToPDF(mapper.map(vacationApproval, VacationApprovalDTO.class));
                break;
        }
    }

    @Override
    public void BTexportToPDF(BTApprovalDTO btApproval) {

        Document document = new Document();

        UserDTO user = userService.findUserById(btApproval.getRequesterId());

//        String filePath = "출장 신청서" + user.getName() + date + ".pdf";
        String filePath = "출장 신청서.pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            String fontPath = "src/main/resources/fonts/Sejong hospital Light.ttf";
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(bf, 20, Font.BOLD);
            Font headerFont = new Font(bf, 12, Font.BOLD);
            Font font = new Font(bf, 12);

            PdfPTable sigTable = new PdfPTable(3);
            PdfPTable table = new PdfPTable(2);

            List<PdfPCell> headers = new ArrayList<>();
            List<PdfPCell> contents = new ArrayList<>();

            headers.add(new PdfPCell(new Paragraph("기안자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("기안일", headerFont)));
            headers.add(new PdfPCell(new Paragraph("시작 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("종료 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("출장지", headerFont)));
            headers.add(new PdfPCell(new Paragraph("내용", headerFont)));
            headers.add(new PdfPCell(new Paragraph("부서장", headerFont)));
            headers.add(new PdfPCell(new Paragraph("팀장", headerFont)));

            contents.add(new PdfPCell(new Paragraph(user.getName(), font)));
            contents.add(new PdfPCell(new Paragraph(btApproval.getWriteTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(btApproval.getStartTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(btApproval.getEndTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(btApproval.getDestination(), font)));
            contents.add(new PdfPCell(new Paragraph(btApproval.getContent(), font)));

            for (PdfPCell header : headers) {
                header.setFixedHeight(30f);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                header.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            }

            headers.get(5).setFixedHeight(240f);
            headers.get(6).setFixedHeight(20f);
            headers.get(7).setFixedHeight(20f);

            table.setWidths(new float[]{2f, 5f});

            for (int i = 0; i < contents.size(); i++) {
                table.addCell(headers.get(i));
                table.addCell(contents.get(i));
            }

            PdfPCell mergedCell = new PdfPCell(new Paragraph("\n\n위와 같이 출장 결과를 보고합니다.\n" +
                    "\n\n\n신청자 소속 :     " + user.getTeam().getTeamName() + "               성명 :     " + user.getName() + "   (인)", font));

            mergedCell.setColspan(2);
            mergedCell.setFixedHeight(120f);
            mergedCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(mergedCell);

            PdfPCell mergedCell2 = new PdfPCell(new Paragraph("결\n\n재", font));
            mergedCell2.setRowspan(2);
            mergedCell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(mergedCell2);
            sigTable.addCell(headers.get(6));
            sigTable.addCell(headers.get(7));
            sigTable.addCell(new PdfPCell(new Paragraph("signature")));
            sigTable.addCell(new PdfPCell(new Paragraph("signature")));

            sigTable.setTotalWidth(160);
            sigTable.setLockedWidth(true);
            sigTable.setWidths(new float[]{2f, 7f, 7f});
            sigTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);

            Paragraph title = new Paragraph("출장 신청서", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(new Paragraph("\n\n", font));
            document.add(sigTable);
            document.add(new Paragraph("\n\n", font));
            document.add(title);
            document.add(new Paragraph("\n\n\n\n", font));
            document.add(table);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }

    @Override
    public void OexportToPDF(OvertimeApprovalDTO overtimeApproval) {

        Document document = new Document();

        UserDTO user = userService.findUserById(overtimeApproval.getRequesterId());

//        String filePath = "출장 신청서" + user.getName() + date + ".pdf";
        String filePath = "시간 외 근무 신청서.pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            String fontPath = "src/main/resources/fonts/Sejong hospital Light.ttf";
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(bf, 20, Font.BOLD);
            Font headerFont = new Font(bf, 12, Font.BOLD);
            Font font = new Font(bf, 12);

            PdfPTable sigTable = new PdfPTable(2);
            PdfPTable table = new PdfPTable(2);

            List<PdfPCell> headers = new ArrayList<>();
            List<PdfPCell> contents = new ArrayList<>();

            headers.add(new PdfPCell(new Paragraph("기안자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("기안일", headerFont)));
            headers.add(new PdfPCell(new Paragraph("시작 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("종료 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("내용", headerFont)));
            headers.add(new PdfPCell(new Paragraph("팀장", headerFont)));

            contents.add(new PdfPCell(new Paragraph(user.getName(), font)));
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getWriteTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getStartTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getEndTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getContent(), font)));

            for (PdfPCell header : headers) {
                header.setFixedHeight(30f);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                header.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            }

            headers.get(4).setFixedHeight(240f);
            headers.get(5).setFixedHeight(20f);

            table.setWidths(new float[]{2f, 5f});

            for (int i = 0; i < contents.size(); i++) {
                table.addCell(headers.get(i));
                table.addCell(contents.get(i));
            }

            PdfPCell mergedCell = new PdfPCell(new Paragraph("\n\n위와 같이 시간 외 근무 결과를 보고합니다.\n" +
                    "\n\n\n신청자 소속 :     " + user.getTeam().getTeamName() + "               성명 :     " + user.getName() + "   (인)", font));

            mergedCell.setColspan(2);
            mergedCell.setFixedHeight(120f);
            mergedCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(mergedCell);

            PdfPCell mergedCell2 = new PdfPCell(new Paragraph("결\n\n재", font));
            mergedCell2.setRowspan(2);
            mergedCell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(mergedCell2);
            sigTable.addCell(headers.get(5));
            sigTable.addCell(new PdfPCell(new Paragraph("signature")));

            sigTable.setTotalWidth(80);
            sigTable.setLockedWidth(true);
            sigTable.setWidths(new float[]{2f, 7f});
            sigTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);

            Paragraph title = new Paragraph("시간 외 근무 신청서", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(new Paragraph("\n\n", font));
            document.add(sigTable);
            document.add(new Paragraph("\n", font));
            document.add(title);
            document.add(new Paragraph("\n\n\n\n", font));
            document.add(table);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }

    @Override
    public void RWexportToPDF(RWApprovalDTO rwApproval) {

        Document document = new Document();

        UserDTO user = userService.findUserById(rwApproval.getRequesterId());

//        String filePath = "출장 신청서" + user.getName() + date + ".pdf";
        String filePath = "단축 근무 신청서.pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            String fontPath = "src/main/resources/fonts/Sejong hospital Light.ttf";
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(bf, 20, Font.BOLD);
            Font headerFont = new Font(bf, 12, Font.BOLD);
            Font font = new Font(bf, 12);

            PdfPTable sigTable = new PdfPTable(2);
            PdfPTable table = new PdfPTable(2);

            List<PdfPCell> headers = new ArrayList<>();
            List<PdfPCell> contents = new ArrayList<>();

            headers.add(new PdfPCell(new Paragraph("기안자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("기안일", headerFont)));
            headers.add(new PdfPCell(new Paragraph("시작 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("종료 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("내용", headerFont)));
            headers.add(new PdfPCell(new Paragraph("팀장", headerFont)));

            contents.add(new PdfPCell(new Paragraph(user.getName(), font)));
            contents.add(new PdfPCell(new Paragraph(rwApproval.getWriteTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(rwApproval.getStartTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(rwApproval.getEndTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(rwApproval.getContent(), font)));

            for (PdfPCell header : headers) {
                header.setFixedHeight(30f);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                header.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            }

            headers.get(4).setFixedHeight(240f);
            headers.get(5).setFixedHeight(20f);

            table.setWidths(new float[]{2f, 5f});

            for (int i = 0; i < contents.size(); i++) {
                table.addCell(headers.get(i));
                table.addCell(contents.get(i));
            }

            PdfPCell mergedCell = new PdfPCell(new Paragraph("\n\n위와 같이 단축 근무 결과를 보고합니다.\n" +
                    "\n\n\n신청자 소속 :     " + user.getTeam().getTeamName() + "               성명 :     " + user.getName() + "   (인)", font));

            mergedCell.setColspan(2);
            mergedCell.setFixedHeight(120f);
            mergedCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(mergedCell);

            PdfPCell mergedCell2 = new PdfPCell(new Paragraph("결\n\n재", font));
            mergedCell2.setRowspan(2);
            mergedCell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(mergedCell2);
            sigTable.addCell(headers.get(5));
            sigTable.addCell(new PdfPCell(new Paragraph("signature")));

            sigTable.setTotalWidth(80);
            sigTable.setLockedWidth(true);
            sigTable.setWidths(new float[]{2f, 7f});
            sigTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);

            Paragraph title = new Paragraph("단축 근무 신청서", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(new Paragraph("\n\n", font));
            document.add(sigTable);
            document.add(new Paragraph("\n", font));
            document.add(title);
            document.add(new Paragraph("\n\n\n\n", font));
            document.add(table);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }

    @Override
    public void VexportToPDF(VacationApprovalDTO vacationApproval) {
        Document document = new Document();

        ResOneVacationTypeVO vacation = vacationService.getVacationTypeById(vacationApproval.getInfoId());
        UserDTO user = userService.findUserById(vacationApproval.getRequesterId());

//        String filePath = "출장 신청서" + user.getName() + date + ".pdf";
        String filePath = "휴가 신청서.pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            String fontPath = "src/main/resources/fonts/Sejong hospital Light.ttf";
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(bf, 20, Font.BOLD);
            Font headerFont = new Font(bf, 12, Font.BOLD);
            Font font = new Font(bf, 12);

            PdfPTable sigTable = new PdfPTable(2);
            PdfPTable table = new PdfPTable(2);

            List<PdfPCell> headers = new ArrayList<>();
            List<PdfPCell> contents = new ArrayList<>();

            headers.add(new PdfPCell(new Paragraph("기안자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("기안일", headerFont)));
            headers.add(new PdfPCell(new Paragraph("시작 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("종료 일자", headerFont)));
            headers.add(new PdfPCell(new Paragraph("휴가 유형", headerFont)));
            headers.add(new PdfPCell(new Paragraph("내용", headerFont)));
            headers.add(new PdfPCell(new Paragraph("팀장", headerFont)));

            contents.add(new PdfPCell(new Paragraph(user.getName(), font)));
            contents.add(new PdfPCell(new Paragraph(vacationApproval.getWriteTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(vacationApproval.getStartTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(vacationApproval.getEndTime().substring(0, 10), font)));
            contents.add(new PdfPCell(new Paragraph(vacation.getResult().getTypeName(), font)));
            contents.add(new PdfPCell(new Paragraph(vacationApproval.getContent(), font)));

            for (PdfPCell header : headers) {
                header.setFixedHeight(30f);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                header.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            }

            headers.get(5).setFixedHeight(240f);
            headers.get(6).setFixedHeight(20f);

            table.setWidths(new float[]{2f, 5f});

            for (int i = 0; i < contents.size(); i++) {
                table.addCell(headers.get(i));
                table.addCell(contents.get(i));
            }

            PdfPCell mergedCell = new PdfPCell(new Paragraph("\n\n위와 같이 휴가를 보고합니다.\n" +
                    "\n\n\n신청자 소속 :     " + user.getTeam().getTeamName() + "               성명 :     " + user.getName() + "   (인)", font));

            mergedCell.setColspan(2);
            mergedCell.setFixedHeight(120f);
            mergedCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(mergedCell);

            PdfPCell mergedCell2 = new PdfPCell(new Paragraph("결\n\n재", font));
            mergedCell2.setRowspan(2);
            mergedCell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(mergedCell2);
            sigTable.addCell(headers.get(6));
            sigTable.addCell(new PdfPCell(new Paragraph("signature")));

            sigTable.setTotalWidth(80);
            sigTable.setLockedWidth(true);
            sigTable.setWidths(new float[]{2f, 7f});
            sigTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);

            Paragraph title = new Paragraph("휴가 신청서", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(new Paragraph("\n\n", font));
            document.add(sigTable);
            document.add(new Paragraph("\n", font));
            document.add(title);
            document.add(new Paragraph("\n\n\n\n", font));
            document.add(table);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }
}
