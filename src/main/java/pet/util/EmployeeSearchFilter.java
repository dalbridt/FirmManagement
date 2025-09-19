package pet.util;

import lombok.Data;

import java.time.LocalDate;
@Data
public class EmployeeSearchFilter {
    private Long departmentId;
    private LocalDate hireDateFrom;
    private LocalDate hireDateBefore;
    private String role;
    private String location;
    private Boolean active;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EmployeeSearchFilter [departmentId=");
        builder.append(departmentId);
        builder.append(", hireDateFrom=");
        builder.append(hireDateFrom);
        builder.append(", role=");
        builder.append(role);
        builder.append(", location=");
        builder.append(location);
        builder.append("]");
        return builder.toString();
    }

}
