package pet.mytest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.dbcp2.BasicDataSource;
import pet.mytest.dao.DepartmentDaoService;
import pet.mytest.dao.EmployeeDaoService;
import pet.mytest.service.DepartmentService;
import pet.mytest.service.EmployeeService;

import java.io.IOException;
import java.util.Properties;

public class BeanFactory {
    private static BeanFactory instance;
    private BasicDataSource dataSource;
    private Properties properties;
    private DepartmentDaoService departmentDaoService;
    private EmployeeDaoService employeeDaoService;
    private ObjectMapper mapper;

    private  BeanFactory() {
            this.properties = new Properties();
            try {
                properties.load(this.getClass().getClassLoader().getResourceAsStream("servlet.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.dataSource = new BasicDataSource();
            try {
                properties.load(this.getClass().getClassLoader().getResourceAsStream("servlet.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dataSource.setDriverClassName(properties.getProperty("db.driver"));
            dataSource.setUrl(properties.getProperty("db.url"));
            dataSource.setUsername(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));
            dataSource.setMaxWaitMillis(3000);
            this.departmentDaoService = new DepartmentDaoService(dataSource);
            this.employeeDaoService = new EmployeeDaoService(dataSource);
            this.mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
    }

    public static BeanFactory getInstance() {
        if (instance == null) {
            instance = new BeanFactory();
        }
        return instance;
    }

    public <T> T getObject(Class<T> aClass){
        if(aClass == DepartmentDaoService.class){
            return (T) departmentDaoService;
        } else if(aClass == EmployeeDaoService.class){
            return (T) employeeDaoService;
        } if (aClass == DepartmentService.class){
            return (T) new DepartmentService(departmentDaoService, employeeDaoService);
        } if (aClass == EmployeeService.class){
            return (T) new EmployeeService(employeeDaoService, departmentDaoService);
        }
        throw new RuntimeException("there's no such class");
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
