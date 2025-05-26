package pet.mytest.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.entities.Department;
import pet.mytest.dao.DepartmentDaoService;
import pet.mytest.dao.EmployeeDaoService;
import pet.mytest.exceptions.InvalidDataException;

import java.util.List;

public class DepartmentService {
    private DepartmentDaoService departmentDaoService;
    private EmployeeDaoService employeeDaoService;
    Logger logger;


    public DepartmentService(DepartmentDaoService departmentDaoService, EmployeeDaoService employeeDaoService) {
        this.departmentDaoService = departmentDaoService;
        this.employeeDaoService = employeeDaoService;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public int addDepartment(Department department) {
        // todo добавляет только если валидный managerID? есть еще проверка от hibernate
        if (employeeDaoService.getEmployeeById(department.getManagerId()) != null) {
            return departmentDaoService.addNewDepartment(department);
        } else {
            throw new InvalidDataException("no manager Id stated");
        }
    }


    public List<Department> getAllDepartments() {
        return departmentDaoService.getAllDepartments();
    }


    public boolean deleteDepartment(int id) {
        return departmentDaoService.deleteDepartment(id);
    }

    public Department getDepartmentById(int id) {
        return departmentDaoService.getDepartmentById(id);
    }
}
