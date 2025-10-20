package pet.controller;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.dto.EmployeeDto;
import pet.exception.InvalidDataException;
import pet.service.EmployeeService;
import pet.util.EmployeeSearchFilter;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto saved = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(saved, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable("id") Long id) {
        EmployeeDto empl = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(empl, HttpStatus.OK);

    }

    @GetMapping("/search")
    public ResponseEntity<?> getEmployeesByFilter(@ModelAttribute EmployeeSearchFilter filter) {
        List<EmployeeDto> filtered = employeeService.getEmployeeByParams(filter);
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }

    //TODO PUT

    @DeleteMapping
    public ResponseEntity<?> deleteEmployee(@RequestParam Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("Emmployee deleted.id : " + id, HttpStatus.OK);
    }
}
