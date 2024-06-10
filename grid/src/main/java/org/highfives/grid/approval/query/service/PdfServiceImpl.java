package org.highfives.grid.approval.query.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
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
import org.highfives.grid.approval_chain.query.service.ApprovalChainService;
import org.highfives.grid.user.command.service.ImgService;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.repository.ImgMapper;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.vacation.query.service.VacationService;
import org.highfives.grid.vacation.query.vo.ResOneVacationTypeVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final ImgMapper imgMapper;
    private final ApprovalChainService approvalChainService;
    private final AmazonS3Client amazonS3Client;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    ResourceLoader resourceLoader;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String date = LocalDateTime.now().format(dateFormat);

    @Autowired
    public PdfServiceImpl(ModelMapper mapper, BTApprovalRepository btApprovalRepository, OApprovalRepository oApprovalRepository, RWApprovalRepository rwApprovalRepository, VApprovalRepository vApprovalRepository, UserService userService, VacationService vacationService, ImgMapper imgMapper, ImgService imgService, ApprovalChainService approvalChainService, AmazonS3Client amazonS3Client, AmazonS3 amazonS3) {
        this.mapper = mapper;
        this.btApprovalRepository = btApprovalRepository;
        this.oApprovalRepository = oApprovalRepository;
        this.rwApprovalRepository = rwApprovalRepository;
        this.vApprovalRepository = vApprovalRepository;
        this.userService = userService;
        this.vacationService = vacationService;
        this.imgMapper = imgMapper;
        this.approvalChainService = approvalChainService;
        this.amazonS3Client = amazonS3Client;
        this.amazonS3 = amazonS3;
    }

    @Override
    public String BTexportToPDF(BTApprovalDTO btApproval, ByteArrayOutputStream outputStream) {

        Document document = new Document();


        UserDTO user = userService.findUserById(btApproval.getRequesterId());

        String fileName = "business_trip_" + user.getEmployeeNumber() + "_" + btApproval.getId() + ".pdf";

        String d_imgPath = imgMapper.getSealImg(approvalChainService.findLeaderByEmployeeId(user.getId(), 1));
//        String d_imgPath = "https://png.pngtree.com/png-clipart/20220113/ourmid/pngtree-cartoon-hand-drawn-default-avatar-png-image_4156500.png";
        String t_imgPath = imgMapper.getSealImg(approvalChainService.findLeaderByEmployeeId(user.getId(), 2));
//        String t_imgPath =  "https://png.pngtree.com/png-clipart/20220113/ourmid/pngtree-cartoon-hand-drawn-default-avatar-png-image_4156500.png";

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image d_img = com.lowagie.text.Image.getInstance(d_imgPath);
            Image t_img = com.lowagie.text.Image.getInstance(t_imgPath);

            d_img.scaleToFit(60, 100);
            t_img.scaleToFit(60, 100);

            String fontPath = "build/resources/main/static/fonts/malgun.ttf";
//            String fontPath = "src/main/resources/fonts/NotoSansKR-Regular.ttf";
//            String fontPath = "src/main/resources/fonts/malgun.ttf";

            String fontPath2 = resourceLoader.getResource("classpath:static/fonts/malgun.ttf").getURI().getPath();
            System.out.println(fontPath2);
            BaseFont bf = BaseFont.createFont(fontPath2, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

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
            contents.add(new PdfPCell(new Paragraph(btApproval.getWriteTime(), font)));
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

            headers.get(5).setFixedHeight(200f);
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

            PdfPCell d_leader = new PdfPCell(d_img);
            PdfPCell t_leader = new PdfPCell(t_img);

            t_leader.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            t_leader.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            d_leader.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            d_leader.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(d_leader);
            sigTable.addCell(t_leader);

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

        return fileName;
    }

    @Override
    public String OexportToPDF(OvertimeApprovalDTO overtimeApproval, ByteArrayOutputStream outputStream) {

        Document document = new Document();

        UserDTO user = userService.findUserById(overtimeApproval.getRequesterId());

        String fileName = "overtime_" + user.getEmployeeNumber() + "_" + overtimeApproval.getId() + ".pdf";

        String imagePath = imgMapper.getSealImg(approvalChainService.findLeaderByEmployeeId(user.getId(), 3));

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image img = com.lowagie.text.Image.getInstance(imagePath);
            img.scaleToFit(60, 100);

            String fontPath = "src/main/resources/fonts/NotoSansCJKkr-Regular.otf";
//            String fontPath = "src/main/resources/fonts/NotoSansKR-Regular.ttf";
//            String fontPath = "src/main/resources/fonts/malgun.ttf";
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(bf, 20, Font.BOLD);
            Font headerFont = new Font(bf, 12, Font.BOLD);
            Font font = new Font(bf, 12);

//            Font titleFont = new Font(bf, 20, Font.BOLD);
//            Font headerFont = new Font(bf, 12, Font.BOLD);
//            Font font = new Font(bf, 12);

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
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getWriteTime(), font)));
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getStartTime(), font)));
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getEndTime(), font)));
            contents.add(new PdfPCell(new Paragraph(overtimeApproval.getContent(), font)));

            for (PdfPCell header : headers) {
                header.setFixedHeight(30f);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                header.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            }

            headers.get(4).setFixedHeight(200f);
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

            PdfPCell t_leader = new PdfPCell(img);

            t_leader.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            t_leader.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(t_leader);

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

        return fileName;
    }

    @Override
    public String RWexportToPDF(RWApprovalDTO rwApproval, ByteArrayOutputStream outputStream) {

        Document document = new Document();

        UserDTO user = userService.findUserById(rwApproval.getRequesterId());

        String fileName = "reduced_work_" + user.getEmployeeNumber() + "_" + rwApproval.getId() + ".pdf";

        String imagePath = imgMapper.getSealImg(approvalChainService.findLeaderByEmployeeId(user.getId(), 4));
//        String imagePath =  "https://png.pngtree.com/png-clipart/20220113/ourmid/pngtree-cartoon-hand-drawn-default-avatar-png-image_4156500.png";

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image img = com.lowagie.text.Image.getInstance(imagePath);
            img.scaleToFit(60, 100);

            String fontPath = "src/main/resources/fonts/NotoSansCJKkr-Regular.otf";
//            String fontPath = "src/main/resources/fonts/NotoSansKR-Regular.ttf";
//            String fontPath = "src/main/resources/fonts/malgun.ttf"
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
            contents.add(new PdfPCell(new Paragraph(rwApproval.getWriteTime(), font)));
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

            PdfPCell t_leader = new PdfPCell(img);

            t_leader.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            t_leader.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(t_leader);

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

        return fileName;
    }

    @Override
    public String VexportToPDF(VacationApprovalDTO vacationApproval, ByteArrayOutputStream outputStream) {
        Document document = new Document();

        ResOneVacationTypeVO vacation = vacationService.getVacationTypeById(vacationApproval.getInfoId());
        UserDTO user = userService.findUserById(vacationApproval.getRequesterId());

        String fileName = "vacation_" + user.getEmployeeNumber() + "_" + vacationApproval.getId() + ".pdf";

        String imagePath = imgMapper.getSealImg(approvalChainService.findLeaderByEmployeeId(user.getId(), 5));
//        String imagePath =  "https://png.pngtree.com/png-clipart/20220113/ourmid/pngtree-cartoon-hand-drawn-default-avatar-png-image_4156500.png";

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image img = com.lowagie.text.Image.getInstance(imagePath);
            img.scaleToFit(60, 100);

            String fontPath = "src/main/resources/fonts/NotoSansCJKkr-Regular.otf";
//            String fontPath = "src/main/resources/fonts/malgun.ttf";
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
            contents.add(new PdfPCell(new Paragraph(vacationApproval.getWriteTime(), font)));
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

            PdfPCell t_leader = new PdfPCell(img);

            t_leader.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            t_leader.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            sigTable.addCell(t_leader);

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

        return fileName;
    }

    @Override
    public Map<String, String> pdfS3Upload(ByteArrayOutputStream outputStream) {

        Map<String, String> result = new HashMap<>();

        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + ".pdf";

        long size = outputStream.size(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentLength(size);
        objectMetaData.setContentType("application/pdf");

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
            // S3에 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, newFileName, inputStream, objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            // 업로드된 파일의 URL을 가져옵니다.
            String fileUrl = amazonS3Client.getUrl(bucketName, newFileName).toString();
            System.out.println("File URL = " + fileUrl);

            result.put("origin", newFileName);
            result.put("new", newFileName);
            result.put("path", fileUrl);

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        } catch (AmazonClientException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(result);

        return result;
    }

    public ResponseEntity<InputStreamResource> downloadFile(int typeId, int approvalId) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String fileName = "";

        switch (typeId) {
            case 1:
                BTApproval btApproval = btApprovalRepository.findById(approvalId).orElseThrow();
                System.out.println("btApproval = " + btApproval);

                fileName = BTexportToPDF(mapper.map(btApproval, BTApprovalDTO.class), outputStream);
                System.out.println("fileName = " + fileName);

                break;

            case 2:
                OvertimeApproval overtimeApproval = oApprovalRepository.findById(approvalId).orElseThrow();
                System.out.println("overtimeApproval = " + overtimeApproval);

                fileName = OexportToPDF(mapper.map(overtimeApproval, OvertimeApprovalDTO.class), outputStream);
                System.out.println("fileName = " + fileName);

                break;

            case 3:
                RWApproval rwApproval = rwApprovalRepository.findById(approvalId).orElseThrow();
                System.out.println("rwApproval = " + rwApproval);

                fileName = RWexportToPDF(mapper.map(rwApproval, RWApprovalDTO.class), outputStream);
                System.out.println("fileName = " + fileName);

                break;

            case 4:
                VacationApproval vacationApproval = vApprovalRepository.findById(approvalId).orElseThrow();
                System.out.println("vacationApproval = " + vacationApproval);

                fileName = VexportToPDF(mapper.map(vacationApproval, VacationApprovalDTO.class), outputStream);
                System.out.println("fileName = " + fileName);

                break;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        System.out.println("inputStream: " + inputStream);

        try {
            // InputStreamResource로 변환하여 ResponseEntity로 반환
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            System.out.println("resource" + resource);
            System.out.println(headers);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadRW(int approvalId) {

        RWApproval rwApproval = rwApprovalRepository.findById(approvalId).orElseThrow();

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, rwApproval.getRenameName()));
        InputStream inputStream = s3Object.getObjectContent();

        try {
            // InputStreamResource로 변환하여 ResponseEntity로 반환
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + rwApproval.getRenameName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
