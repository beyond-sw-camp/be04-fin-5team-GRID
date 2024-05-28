package org.highfives.grid.vacation.command.controller;

import org.highfives.grid.vacation.command.service.VacationService;
import org.highfives.grid.vacation.command.vo.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "VacationCommandController")
@RequestMapping("/vacation")
public class VacationController {

    private VacationService vacationService;
    private ModelMapper modelMapper;

    @Autowired
    public VacationController(VacationService vacationService, ModelMapper modelMapper) {
        this.vacationService = vacationService;
        this.modelMapper = modelMapper;
    }

    @PutMapping("/policy/{id}")
    public ResponseEntity<Object> modifyVacationPolicy(@RequestBody ModifyPolicy policyInfo, @PathVariable int id) {
        vacationService.modifyVacationPolicy(policyInfo, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/policy/{id}")
    public ResponseEntity<Object> deleteVacationPolicy(@PathVariable int id) {
        vacationService.deleteVacationPolicy(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/policy")
    public ResponseEntity<Object> registVacationPolicy(@RequestBody RegistPolicy policyInfo) {
        vacationService.registVacationPolicy(policyInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payments")
    public ResponseEntity<Object> giveVacationByManager(@RequestBody GiveVacation vacationInfo) {
        vacationService.giveVacationByManager(vacationInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/type")
    public ResponseEntity<Object> registVacationType(@RequestBody RegistVacationType typeInfo) {
        vacationService.registVacationType(typeInfo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/type/{id}")
    public ResponseEntity<Object> modifyVacationType(@RequestBody ModifyVacationType typeInfo,@PathVariable int id) {
        vacationService.modifyVacationType(typeInfo, id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/type/{id}")
    public ResponseEntity<Object> deleteVacationType(@PathVariable int id) {
        vacationService.deleteVacationType(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/annual_vacation")
    public ResponseEntity<Object> giveAnnualVacation() {
        vacationService.giveAnnualVacationAfterYear();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/month_vacation")
    public ResponseEntity<Object> giveMonthVacation() {
        vacationService.giveAnnualVacationBeforeYear();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/regular_vacation")
    public ResponseEntity<Object> giveRegularVacation() {
        vacationService.giveRegularVacation();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health_vacation")
    public ResponseEntity<Object> giveHealthVacation() {
        vacationService.giveHealthVacation();
        return ResponseEntity.ok().build();
    }

}
