package pet.mytest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.entities.Department;
import pet.mytest.dao.DepartmentDaoService;

import java.util.List;

@Service
public class DepartmentService {
    private DepartmentDaoService departmentDaoService;
    Logger logger =  LoggerFactory.getLogger(this.getClass());;

    @Autowired // todo использовать только анотацию, а не конструктор? не использовать анотацию вообще?
    public DepartmentService(DepartmentDaoService departmentDaoService) {
        this.departmentDaoService = departmentDaoService;
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
