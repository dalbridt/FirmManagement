package pet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.dto.EmployeeDto;
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
    public ResponseEntity <?> createEmployee(@RequestBody EmployeeDto employeeDto) {
        try {
            EmployeeDto saved =  employeeService.createEmployee(employeeDto);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }catch(Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity <?> getEmployee(@PathVariable("id") Long id) {
        try {
            EmployeeDto empl = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(empl, HttpStatus.OK);
        } catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity <?> getEmployeesByFilter(@ModelAttribute EmployeeSearchFilter filter) {
        try{

            List<EmployeeDto> filtered = employeeService.getEmployeeByParams(filter);
            return new ResponseEntity<>(filtered, HttpStatus.OK);
        }catch(Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public void deleteEmployee(@RequestParam Long id) {
        try {
            employeeService.deleteEmployee(id);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

    }
}
