package org.highfives.grid.vacation.query.controller;

import org.highfives.grid.vacation.query.service.VacationService;
import org.highfives.grid.vacation.query.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "VacationQueryController")
@RequestMapping("/vacation")
public class VacationController {
    private VacationService vacationService;

    @Autowired
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/info")
    public ResponseEntity<ResVacationInfoVO> getAllVacations() {
        ResVacationInfoVO vacations = vacationService.getAllVacations();
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }

    @GetMapping("/info/{employeeId}")
    public ResponseEntity<ResVacationInfoVO> getUserVacations(@PathVariable int employeeId) {
        ResVacationInfoVO vacations = vacationService.getUserVacations(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }

    @GetMapping("/policy/{id}")
    public ResponseEntity<ResVacationPolicyVO> getVacationPolicy(@PathVariable int id) {
        ResVacationPolicyVO policies = vacationService.getVacationPolicy(id);
        return ResponseEntity.status(HttpStatus.OK).body(policies);
    }

    @GetMapping("/policy/all")
    public ResponseEntity<ResVacationPolicyWithTypeNameVO> getAllVacationPolicy() {
        ResVacationPolicyWithTypeNameVO policies = vacationService.getAllVacationPolicy();
        return ResponseEntity.status(HttpStatus.OK).body(policies);
    }

    @GetMapping("/details")
    public ResponseEntity<ResVacationHistoryVO> getAllVacationHistories() {
        ResVacationHistoryVO histories = vacationService.getAllVacationHistory();
        return ResponseEntity.status(HttpStatus.OK).body(histories);
    }

    @GetMapping("/details/{employeeId}")
    public ResponseEntity<ResVacationHistoryVO> getUserVacationHistories(@PathVariable int employeeId) {
        ResVacationHistoryVO histories = vacationService.getUserVacationHistory(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(histories);
    }

    @GetMapping("/details/search/name")
    public ResponseEntity<ResVacationHistoryVO> searchVacationHistoriesByName(@RequestParam("employeeName") String employeeName) {
        ResVacationHistoryVO histories = vacationService.searchVacationHistoryByName(employeeName);
        return ResponseEntity.status(HttpStatus.OK).body(histories);
    }

    @GetMapping("/details/search/id")
    public ResponseEntity<ResVacationHistoryVO> searchVacationHistoriesById(@RequestParam("employeeNumber") String employeeNumber) {
        ResVacationHistoryVO histories = vacationService.searchVacationHistoryById(employeeNumber);
        return ResponseEntity.status(HttpStatus.OK).body(histories);
    }

    @GetMapping("/history/search")
    public ResponseEntity<ResVacationHistoryVO> searchVacationHistories(@RequestParam("typeId") int typeId, @RequestParam("changeTypeId") int changeTypeId, @RequestParam("employeeId") int employeeId) {
        ResVacationHistoryVO histories = vacationService.searchVacationHistory(typeId, changeTypeId, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(histories);

    }

    @GetMapping("/name/search")
    public ResponseEntity<ResVacationInfoVO> searchVacationInfoByName(@RequestParam("name") String name) {
        ResVacationInfoVO vacations = vacationService.searchVacationInfoByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }

    @GetMapping("/dept/search")
    public ResponseEntity<ResVacationInfoVO> searchVacationInfoByDept(@RequestParam("dept") String dept) {
        ResVacationInfoVO vacations = vacationService.searchVacationInfoByDept(dept);
        return ResponseEntity.status(HttpStatus.OK).body(vacations);
    }

    @GetMapping("/type")
    public ResponseEntity<ResVacationTypeVO> getVacationType() {
        ResVacationTypeVO types = vacationService.getVacationType();
        return ResponseEntity.status(HttpStatus.OK).body(types);
    }

    @GetMapping("/all/type")
    public ResponseEntity<ResVacationTypeVO> getAllVacationType() {
        ResVacationTypeVO types = vacationService.getAllVacationType();
        return ResponseEntity.status(HttpStatus.OK).body(types);
    }

    @GetMapping("/type/{id}")
    public ResponseEntity<ResOneVacationTypeVO> getVacationTypeById(@PathVariable int id) {
        ResOneVacationTypeVO types = vacationService.getVacationTypeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(types);
    }

}