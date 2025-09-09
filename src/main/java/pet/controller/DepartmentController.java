package pet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.dto.DepartmentDto;
import pet.service.DepartmentService;
import pet.util.DepartmentMapper;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto) {
        try{
            DepartmentDto saved = departmentService.createDepartment(departmentDto);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDepartments() {
        try {
            return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable("id") Long id) {
        try {
            DepartmentDto depDto = departmentService.getDepartmentById(id);
            return new ResponseEntity<>(depDto, HttpStatus.OK);
        } catch (Exception e ) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public void deleteDepartmentById(@PathVariable("id") Long id) {
        try {
            departmentService.deleteDepartment(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
