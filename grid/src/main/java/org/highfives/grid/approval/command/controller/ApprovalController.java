package org.highfives.grid.approval.command.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.highfives.grid.approval.command.aggregate.YN;
import org.highfives.grid.approval.command.service.ApprovalService;
import org.highfives.grid.approval.command.vo.*;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;
import org.highfives.grid.approval.common.vo.ResApprovalVO;
import org.highfives.grid.approval.query.service.PdfService;
import org.highfives.grid.user.command.service.ImgService;
import org.highfives.grid.user.command.vo.ResImgUploadVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController(value = "CommandApprovalController")
@RequestMapping("/approval")
public class ApprovalController {

    private final ApprovalService approvalService;
    private final PdfService pdfService;
    private final ImgService imgService;

    @Autowired
    public ApprovalController(ApprovalService approvalService, PdfService pdfService, ImgService imgService) {
        this.approvalService = approvalService;
        this.pdfService = pdfService;
        this.imgService = imgService;
    }

    @PostMapping("/bt")
    public ResponseEntity<ResApprovalVO> addBTApproval(@RequestBody BTApprovalVO btApprovalVO) {

        BTApprovalDTO result = approvalService.addBTApproval(btApprovalVO);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("출장 결재 생성 성공")
                .href("/approval/detail/1/" + result.getId())
                .btResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/overtime")
    public ResponseEntity<ResApprovalVO> addOvertimeApproval(@RequestBody OvertimeApprovalVO overtimeApprovalVO) {

        OvertimeApprovalDTO result = approvalService.addOvertimeApproval(overtimeApprovalVO);

        if (result == null) {
            ResApprovalVO response = ResApprovalVO.builder()
                    .success(false)
                    .message("주별 시간 외 근무 시간의 합계가 12시간을 초과했습니다.")
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        ResApprovalVO response = ResApprovalVO.builder()
                .success(true)
                .statusCode(201)
                .message("시간 외 근무 결재 생성 성공")
                .href("/approval/detail/2/" + result.getId())
                .overtimeResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rw")
    public ResponseEntity<ResApprovalVO> addRWApproval(@RequestPart("file") MultipartFile file, @RequestPart("postData") String rwApprovalVO) {

        ObjectMapper objectMapper = new ObjectMapper();
        ResApprovalVO response = null;

        try {
            RWApprovalVO rwApproval = objectMapper.readValue(rwApprovalVO, RWApprovalVO.class);

            Map<String, String> uploadResult = imgService.imgS3Upload(file);

            rwApproval.setOriginName(uploadResult.get("origin"));
            rwApproval.setRenameName(uploadResult.get("new"));
            rwApproval.setPath(uploadResult.get("path"));

            RWApprovalDTO result = approvalService.addRWApproval(rwApproval);

            response = ResApprovalVO.builder()
                    .statusCode(201)
                    .message("단축 근무 결재 생성 성공")
                    .href("/approval/detail/3/" + result.getId())
                    .rwResult(result)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/vacation")
    public ResponseEntity<ResApprovalVO> addVacationApproval(@RequestBody VacationApprovalVO vacationApprovalVO) {

        VacationApprovalDTO result = approvalService.addVacationApproval(vacationApprovalVO);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("휴가 결재 생성 성공")
                .href("/approval/detail/4/" + result.getId())
                .vacationResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/bt/{btApprovalId}")
    public ResponseEntity<ResApprovalVO> modifyBTApproval(@RequestBody BTApprovalVO btApprovalVO, @PathVariable int btApprovalId) {

        BTApprovalDTO result = approvalService.modifyBTApproval(btApprovalVO, btApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("출장 결재 수정 성공")
                .href("")
                .btResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/overtime/{overtimeApprovalId}")
    public ResponseEntity<ResApprovalVO> modifyOvertimeApproval(@RequestBody OvertimeApprovalVO overtimeApprovalVO, @PathVariable int overtimeApprovalId) {

        OvertimeApprovalDTO result = approvalService.modifyOvertimeApproval(overtimeApprovalVO, overtimeApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("시간 외 결재 수정 성공")
                .href("")
                .overtimeResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/rw/{rwApprovalId}")
    public ResponseEntity<ResApprovalVO> modifyRWApproval(@RequestBody RWApprovalVO rwApprovalVO, @PathVariable int rwApprovalId) {

        RWApprovalDTO result = approvalService.modifyRWApproval(rwApprovalVO, rwApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("단축 근무 결재 수정 성공")
                .href("")
                .rwResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/vacation/{vacationApprovalId}")
    public ResponseEntity<ResApprovalVO> modifyVacationApproval(@RequestBody VacationApprovalVO vacationApprovalVO, @PathVariable int vacationApprovalId) {

        VacationApprovalDTO result = approvalService.modifyVacationApproval(vacationApprovalVO, vacationApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("휴가 결재 수정 성공")
                .href("")
                .vacationResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/bt/{btApprovalId}")
    public ResponseEntity<ResApprovalVO> cancelBTApproval(@PathVariable int btApprovalId) {

        BTApprovalDTO result = approvalService.cancelBTApproval(btApprovalId);

        if (result.getCancelYn() == YN.Y) {
            ResApprovalVO response = ResApprovalVO.builder()
                    .statusCode(200)
                    .message("출장 결재 회수 성공")
                    .href("")
                    .btResult(result)
                    .build();
        }

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("출장 결재 취소 결재 생성 성공")
                .href("")
                .btResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/overtime/{overtimeApprovalId}")
    public ResponseEntity<ResApprovalVO> cancelOvertimeApproval(@PathVariable int overtimeApprovalId) {

        OvertimeApprovalDTO result = approvalService.cancelOvertimeApproval(overtimeApprovalId);

        if (result.getCancelYn() == YN.Y) {
            ResApprovalVO response = ResApprovalVO.builder()
                    .statusCode(200)
                    .message("시간 외 근무 결재 회수 성공")
                    .href("")
                    .overtimeResult(result)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("시간 외 근무 결재 취소 결재 생성 성공")
                .href("")
                .overtimeResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rw/{rwApprovalId}")
    public ResponseEntity<ResApprovalVO> cancelRWApproval(@PathVariable int rwApprovalId) {

        RWApprovalDTO result = approvalService.cancelRWApproval(rwApprovalId);

        if (result.getCancelYn() == YN.Y) {
            ResApprovalVO response = ResApprovalVO.builder()
                    .statusCode(200)
                    .message("단축 근무 결재 회수 성공")
                    .href("")
                    .rwResult(result)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("단축 근무 결재 취소 결재 생성 성공")
                .href("")
                .rwResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/vacation/{vacationApprovalId}")
    public ResponseEntity<ResApprovalVO> cancelVacationApproval(@PathVariable int vacationApprovalId) {

        VacationApprovalDTO result = approvalService.cancelVacationApproval(vacationApprovalId);

        if (result.getCancelYN() == YN.Y) {
            ResApprovalVO response = ResApprovalVO.builder()
                    .statusCode(200)
                    .message("휴가 결재 회수 성공")
                    .href("")
                    .vacationResult(result)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("휴가 결재 취소 결재 생성 성공")
                .href("")
                .vacationResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/bt-status/{approvalId}")
    public ResponseEntity<ResApprovalVO> viewBTApproval(@PathVariable int approvalId) {

        BTApprovalDTO result = approvalService.viewBTApproval(approvalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("출장 결재 열람 상태 변경 성공")
                .href("")
                .btResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/overtime-status/{approvalId}")
    public ResponseEntity<ResApprovalVO> viewOvertimeApproval(@PathVariable int approvalId) {

        OvertimeApprovalDTO result = approvalService.viewOvertimeApproval(approvalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("시간 외 근무 결재 열람 상태 변경 성공")
                .href("")
                .overtimeResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/rw-status/{approvalId}")
    public ResponseEntity<ResApprovalVO> viewRWApproval(@PathVariable int approvalId) {

        RWApprovalDTO result = approvalService.viewRWApproval(approvalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("단축 근무 결재 열람 상태 변경 성공")
                .href("")
                .rwResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/vacation-status/{approvalId}")
    public ResponseEntity<ResApprovalVO> viewVacationApproval(@PathVariable int approvalId) {

        VacationApprovalDTO result = approvalService.viewVacationApproval(approvalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("휴가 결재 열람 상태 변경 성공")
                .href("")
                .vacationResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/downloadPdf/{typeId}/{approvalId}")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable int typeId, @PathVariable int approvalId) {

         return pdfService.downloadFile(typeId, approvalId);
    }

    @GetMapping("/downloadRW/{approvalId}")
    public ResponseEntity<InputStreamResource> downloadRW(@PathVariable int approvalId) {

        return pdfService.downloadRW(approvalId);
    }
}
