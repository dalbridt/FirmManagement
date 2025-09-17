package pet.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long departmentId;
    private String role;
    private String location;
    private double salary;
    private LocalDate hireDate;
    private boolean active;
}
