package pet.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.dto.EmployeeDto;
import pet.entity.Employee;
import pet.exception.InvalidDataException;
import pet.util.EmployeeMapper;
import pet.dao.EmployeeDaoService;
import pet.util.EmployeeSearchFilter;

import java.util.List;


@Service
public class EmployeeService {
    private final EmployeeDaoService employeeDaoService;
    private Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeDaoService employeeDaoService) {
        this.employeeDaoService = employeeDaoService;
    }


    public void deleteEmployee(Long id) {
        if ( ! employeeDaoService.deleteEmployee(id)) {
            throw new InvalidDataException("Employee with id " + id + " not found");
        }
    }

    public EmployeeDto createEmployee(EmployeeDto emp) {
        if (isValid(emp)) {
            Employee entity = EmployeeMapper.convertToEntity(emp);
            Employee saved = employeeDaoService.createEmployee(entity);
            return EmployeeMapper.convertToDTO(saved);
        } else {
            throw new InvalidDataException("Not enough information. Employee name role and salary can't be empty");
        }
    }

    public EmployeeDto getEmployeeById(Long id) {
        Employee entity = employeeDaoService.getEmployeeById(id);
        return EmployeeMapper.convertToDTO(entity);
    }

    private boolean isValid(EmployeeDto emp) {
        return (!emp.getFirstName().isEmpty()
                && !emp.getLastName().isEmpty()
                && !emp.getRole().isEmpty()
                && emp.getSalary() > 0
                && emp.getDepartmentId() != null
        );

    }

    public List<EmployeeDto> getEmployeeByParams(EmployeeSearchFilter filter) {
        List<Employee> emplEntities = employeeDaoService.getEmployeesByParams(filter);
        return emplEntities.stream().map(EmployeeMapper::convertToDTO).toList();
    }
}


