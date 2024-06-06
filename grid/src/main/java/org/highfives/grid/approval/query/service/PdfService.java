package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;
import org.highfives.grid.approval.common.dto.VacationApprovalDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

public interface PdfService {

    void BTexportToPDF(BTApprovalDTO btApproval, ByteArrayOutputStream outputStream);
    void OexportToPDF(OvertimeApprovalDTO overtimeApproval, ByteArrayOutputStream outputStream);
    void RWexportToPDF(RWApprovalDTO rwApproval, ByteArrayOutputStream outputStream);
    void VexportToPDF(VacationApprovalDTO vacationApproval, ByteArrayOutputStream outputStream);
    Map<String, String> pdfS3Upload(ByteArrayOutputStream outputStream);
    ResponseEntity<InputStreamResource> downloadFile(int typeId, int approvalId);
}
