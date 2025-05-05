package pet.mytest.service;

import org.apache.log4j.Logger;
import pet.entities.Employee;
import pet.entities.EmployeeSearchFilter;
import pet.mytest.dao.DepartmentDaoService;
import pet.mytest.dao.EmployeeDaoService;
import pet.mytest.exceptions.DatabaseException;

import java.util.HashMap;
import java.util.List;


public class EmployeeService {
    private EmployeeDaoService employeeDaoService;
    private DepartmentDaoService departmentDaoService;
    private Logger logger;

    public EmployeeService(EmployeeDaoService employeeDaoService, DepartmentDaoService departmentDaoService) {
        this.employeeDaoService = employeeDaoService;
        this.departmentDaoService = departmentDaoService;
        this.logger = Logger.getLogger(this.getClass());
    }

    public List<Employee> getEmployeesByDepartmentId(int departmentId) {
        return employeeDaoService.getEmployeesByDepartmentId(departmentId);
    }

    public boolean deleteEmployee(int id) {
        if (employeeDaoService.getEmployeeById(id) != null) {
            return employeeDaoService.deleteEmployee(id);
        }
        return false;
    }

    public int addNewEmployee(Employee emp) {
        if((emp.getName() == null || emp.getName().isEmpty()) || (emp.getRole() == null || emp.getRole().isEmpty()) || emp.getSalary() <= 0) {
           //todo ошибка валидации, сдлеать ошибку
            throw new DatabaseException("Not enough information. Employee name role and salary can't be empty");
        }
        if(departmentDaoService.getDepartmentById(emp.getDepartmentId()) == null) {
            throw new DatabaseException("Not enough information. Department not found");
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
