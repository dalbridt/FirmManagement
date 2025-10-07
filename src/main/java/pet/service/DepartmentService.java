package pet.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.dto.DepartmentDto;
import pet.entity.Department;
import pet.dao.DepartmentDaoService;
import pet.util.DepartmentMapper;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentDaoService departmentDaoService;
    private Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired // можно использовать просто анотацию над полем и не писать конструктор, но так более явно
    public DepartmentService(DepartmentDaoService departmentDaoService) {
        this.departmentDaoService = departmentDaoService;
    }

    public DepartmentDto createDepartment(DepartmentDto department) {
        Department entity = DepartmentMapper.convertToEntity(department);
        Department saved = departmentDaoService.createDepartment(entity);
        return DepartmentMapper.convertToDTO(saved);
    }

    public List<DepartmentDto> getAllDepartments() {
        List <Department> departments = departmentDaoService.getAllDepartments();
        return departments.stream().map(DepartmentMapper::convertToDTO).toList();
    }

    public DepartmentDto getDepartmentById(Long id) {
        Department entity = departmentDaoService.getDepartmentById(id);
        return DepartmentMapper.convertToDTO(entity);
    }

    public void deleteDepartment(Long id) {
         departmentDaoService.deleteDepartment(id);
    }
}
