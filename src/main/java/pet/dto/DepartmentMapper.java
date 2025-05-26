package pet.dto;

import pet.entities.Department;
import pet.mytest.exceptions.InvalidDataException;

public class DepartmentMapper {

    public static DepartmentDTO convertToDTO(Department department){
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setManagerId(department.getManagerId());
        departmentDTO.setName(department.getName());
        return departmentDTO;
    }
    public static Department convertToEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        if(departmentDTO.getManagerId() != null){
            department.setManagerId(departmentDTO.getManagerId());
        }
        if (departmentDTO.getName() != null) {
            department.setName(departmentDTO.getName());
        } else {
            throw new InvalidDataException("Department name is required");
        }
        return department;
    }
}
