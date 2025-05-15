package pet.dto;

// todo заменить в сервлете использлование энтити на использование дто
public class DepartmentDTO {
    private String name;
    private int managerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
}
