package pet.mytest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pet.entities.Department;
import pet.entities.Employee;
import pet.mytest.dao.DepartmentDaoService;
import pet.mytest.dao.EmployeeDaoService;
import pet.mytest.service.DepartmentService;
import pet.mytest.service.EmployeeService;

@Configuration
@ComponentScan
//@EntityScan("pet.entities")
public class MyConfig {
    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
        configuration.addAnnotatedClass(Department.class);
        configuration.addAnnotatedClass(Employee.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }

    @Bean
    public DepartmentDaoService departmentDaoService() {
        return new DepartmentDaoService(sessionFactory());
    }

    @Bean
    public EmployeeDaoService employeeDaoService() {
        return new EmployeeDaoService(sessionFactory());
    }

    @Bean
    public EmployeeService employeeService(EmployeeDaoService employeeDao) {
        return new EmployeeService(employeeDao);
    }

    @Bean
    public DepartmentService departmentService(DepartmentDaoService departmentDao) {
        return new DepartmentService(departmentDao);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return   new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
