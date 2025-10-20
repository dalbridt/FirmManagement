package pet.util;

import pet.dto.DepartmentDto;
import pet.entity.Department;
import pet.exception.InvalidDataException;

public class DepartmentMapper {

    public static DepartmentDto convertToDTO(Department department){
        DepartmentDto departmentDTO = new DepartmentDto();
        departmentDTO.setManagerId(department.getManagerId());
        departmentDTO.setName(department.getName());
        departmentDTO.setId(department.getId());
        return departmentDTO;
    }
    public static Department convertToEntity(DepartmentDto departmentDTO) {
        Department department = new Department();
        if(departmentDTO.getId() != null){
            department.setId(departmentDTO.getId());
        }
        if(departmentDTO.getManagerId() != null){
            department.setManagerId(departmentDTO.getManagerId());
        }
        if (departmentDTO.getName() != null && !departmentDTO.getName().isEmpty()) {
            department.setName(departmentDTO.getName());
        } else {
            throw new InvalidDataException("Department name is required");
        }
        return department;
    }
}
