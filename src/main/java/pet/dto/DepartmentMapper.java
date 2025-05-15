package pet.dto;

import pet.entities.Department;

public class DepartmentMapper {

    public static DepartmentDTO convertToDTO(Department department){
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setManagerId(department.getManagerId());
        departmentDTO.setName(department.getName());
        return departmentDTO;
    }
    public static Department convertToEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setManagerId(departmentDTO.getManagerId());
        department.setName(departmentDTO.getName());
        return department;
    }
}
