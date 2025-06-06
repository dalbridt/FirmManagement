package pet.mytest.service;


import org.springframework.stereotype.Service;
import pet.entities.Employee;
import pet.entities.EmployeeSearchFilter;
import pet.mytest.dao.EmployeeDaoService;
import pet.mytest.exceptions.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EmployeeService {
    private EmployeeDaoService employeeDaoService;
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    public EmployeeService(EmployeeDaoService employeeDaoService) {
        this.employeeDaoService = employeeDaoService;
    }


    public boolean deleteEmployee(int id) {
        if (employeeDaoService.getEmployeeById(id) != null) {
            return employeeDaoService.deleteEmployee(id);
        }
        return false;
    }

    public int addNewEmployee(Employee emp) {
        if((emp.getName() == null || emp.getName().isEmpty()) || (emp.getRole() == null || emp.getRole().isEmpty()) || emp.getSalary() <= 0) {
            throw new DatabaseException("Not enough information. Employee name role and salary can't be empty");
        }
        return employeeDaoService.addEmployee(emp);
    }

    public Employee getEmployeeById(int id) {
        return employeeDaoService.getEmployeeById(id);
    }

    public List<Employee> getEmployeesByFilters(EmployeeSearchFilter filter) {
        logger.debug("getEmployeesByFilters called");
        return employeeDaoService.getEmployeesByFilters(filter);
    }
}
