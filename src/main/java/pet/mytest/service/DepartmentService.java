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
        return departmentDaoService.addNewDepartment(department);
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
