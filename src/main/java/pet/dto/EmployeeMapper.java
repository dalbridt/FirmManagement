package pet.dto;

import pet.entities.Department;
import pet.entities.Employee;

public class EmployeeMapper {

    public static EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName(employee.getName());
        dto.setRole(employee.getRole());
        dto.setLocation(employee.getLocation());
        dto.setSalary(employee.getSalary());
        dto.setHireDate(employee.getHireDate());
        dto.setDepartmentId(employee.getDepartment().getId());
        return dto;
    }
    public static Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setRole(employeeDTO.getRole());
        employee.setLocation(employeeDTO.getLocation());
        employee.setSalary(employeeDTO.getSalary());
        employee.setHireDate(employeeDTO.getHireDate());
        Department department = new Department();
        department.setId(employeeDTO.getDepartmentId());
        employee.setDepartment(department); // todo департамент подставляется отдельно!! нужно ли это?
        return employee;
    }
}
