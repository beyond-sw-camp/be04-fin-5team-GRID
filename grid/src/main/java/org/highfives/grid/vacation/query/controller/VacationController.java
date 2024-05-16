package org.highfives.grid.vacation.query.controller;

import org.highfives.grid.vacation.query.dto.VacationHistoryDTO;
import org.highfives.grid.vacation.query.dto.VacationInfoDTO;
import org.highfives.grid.vacation.query.dto.VacationPolicyDTO;
import org.highfives.grid.vacation.query.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "VacationQueryController")
@RequestMapping("/vacation")
public class VacationController {
    private VacationService vacationService;

    @Autowired
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VacationInfoDTO>> getAllVacations() {
        List<VacationInfoDTO> vacations =  vacationService.getAllVacations();
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<VacationInfoDTO>> getUserVacations(@PathVariable int employeeId) {
        List<VacationInfoDTO> vacations = vacationService.getUserVacations(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }

    @GetMapping("/policy/{typeId}")
    public ResponseEntity<List<VacationPolicyDTO>> getVacationPolicy(@PathVariable int typeId) {
        List<VacationPolicyDTO> policies = vacationService.getVacationPolicy(typeId);
        return ResponseEntity.status(HttpStatus.OK).body(policies);
    }

    @GetMapping("/details")
    public ResponseEntity<List<VacationHistoryDTO>> getAllVacationHistories() {
        List<VacationHistoryDTO> histories = vacationService.getAllVacationHistory();
        return ResponseEntity.status(HttpStatus.OK).body(histories);
    }

    @GetMapping("/history/search")
    public ResponseEntity<List<VacationHistoryDTO>> searchVacationHistories(@RequestParam("typeId") int typeId, @RequestParam("changeTypeId") int changeTypeId, @RequestParam("employeeId") int employeeId ) {
        List<VacationHistoryDTO> histories = vacationService.searchVacationHistory(typeId, changeTypeId, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(histories);

    }

    @GetMapping("/name/search")
    public ResponseEntity<List<VacationInfoDTO>> searchVacationInfoByName(@RequestParam("name") String name) {
        List<VacationInfoDTO> vacations = vacationService.searchVacationInfoByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }

    @GetMapping("/dept/search")
    public ResponseEntity<List<VacationInfoDTO>> searchVacationInfoByDept(@RequestParam("dept") String dept) {
        List<VacationInfoDTO> vacations = vacationService.searchVacationInfoByDept(dept);
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }


}
