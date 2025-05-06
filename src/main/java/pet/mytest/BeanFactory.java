package pet.mytest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pet.entities.Department;
import pet.entities.Employee;
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
    private SessionFactory sessionFactory;
    private Logger logger;

    private BeanFactory() {
        // todo make new bean factory for hibernate
        this.logger = LoggerFactory.getLogger(BeanFactory.class);
        logger.debug("BeanFactory initialized ==== ");
        try {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Department.class);
            configuration.addAnnotatedClass(Employee.class);
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            this.sessionFactory = configuration.buildSessionFactory(builder.build());
            logger.debug("SessionFactory for hibernate created");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        // old algo
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
        this.employeeDaoService = new EmployeeDaoService(dataSource, sessionFactory);
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    public static BeanFactory getInstance() {
        if (instance == null) {
            instance = new BeanFactory();
        }
        return instance;
    }

    public <T> T getObject(Class<T> aClass) {
        if (aClass == DepartmentDaoService.class) {
            return (T) departmentDaoService;
        } else if (aClass == EmployeeDaoService.class) {
            return (T) employeeDaoService;
        }
        if (aClass == DepartmentService.class) {
            return (T) new DepartmentService(departmentDaoService, employeeDaoService);
        }
        if (aClass == EmployeeService.class) {
            return (T) new EmployeeService(employeeDaoService, departmentDaoService);
        }
//        if(aClass == SessionFactory.class) {
//            return (T) sessionFactory;
//        }
        throw new RuntimeException("there's no such class");
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
