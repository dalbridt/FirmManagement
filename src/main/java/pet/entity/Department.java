package pet.entity;

import jakarta.persistence.*;
import lombok.Data;

//@EqualsAndHashCode(of = "name", "managerId")
@Entity
@Table (name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dep_name")
    private String name;
    @Column(name = "manager_id", nullable = true)
    private Long managerId;
}


