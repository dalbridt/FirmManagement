package pet.util;

import pet.dto.EmployeeDto;
import pet.entity.Department;
import pet.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDto convertToDTO(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setRole(employee.getRole());
        dto.setLocation(employee.getLocation());
        dto.setSalary(employee.getSalary());
        dto.setHireDate(employee.getHireDate());
        dto.setDepartmentId(employee.getDepartment().getId());
        dto.setId(employee.getId());
        return dto;
    }
    public static Employee convertToEntity(EmployeeDto employeeDTO) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setRole(employeeDTO.getRole());
        employee.setLocation(employeeDTO.getLocation());
        employee.setSalary(employeeDTO.getSalary());
        employee.setHireDate(employeeDTO.getHireDate());
        Department department = new Department();
        department.setId(employeeDTO.getDepartmentId());
        employee.setDepartment(department); // todo департамент подставляется прокси !! нужно ли это?
        return employee;
    }
}
