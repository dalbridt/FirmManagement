package pet.util;

import java.time.LocalDate;
// TODO - надо будет ?
public class EmployeeSearchFilter {
    private Integer departmentId;
    private LocalDate hireDateFrom;
    private LocalDate hireDateBefore;
    private String role;
    private String location;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId( Integer departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDate getHireDateFrom() {
        return hireDateFrom;
    }

    public void setHireDateFrom(LocalDate hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public LocalDate getHireDateBefore() {
        return hireDateBefore;
    }

    public void setHireDateBefore(LocalDate hireDateBefore) {
        this.hireDateBefore = hireDateBefore;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EmployeeSearchFilter [departmentId=");
        builder.append(departmentId);
        builder.append(", hireDateFrom=");
        builder.append(hireDateFrom);
        builder.append(", hireDateBefore=");
        builder.append(hireDateBefore);
        builder.append(", role=");
        builder.append(role);
        builder.append(", location=");
        builder.append(location);
        builder.append("]");
        return builder.toString();
    }

}
