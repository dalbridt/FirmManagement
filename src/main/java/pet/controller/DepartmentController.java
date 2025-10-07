package pet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.dto.DepartmentDto;
import pet.service.DepartmentService;


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
        DepartmentDto saved = departmentService.createDepartment(departmentDto);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllDepartments() {
        return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable("id") Long id) {
        DepartmentDto depDto = departmentService.getDepartmentById(id);
        return new ResponseEntity<>(depDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartmentById(@PathVariable("id") Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>("Department successfully deleted. id: " + id, HttpStatus.OK);

    }
}
