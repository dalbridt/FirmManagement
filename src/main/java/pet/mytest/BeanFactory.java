//package pet.mytest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.Configuration;
//import pet.entities.Department;
//import pet.entities.Employee;
//import pet.mytest.dao.DepartmentDaoService;
//import pet.mytest.dao.EmployeeDaoService;
//import pet.mytest.service.DepartmentService;
//import pet.mytest.service.EmployeeService;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
//public class BeanFactory {
//    private static BeanFactory instance;
//    private DepartmentDaoService departmentDaoService; // added to spring MyConfig
//    private EmployeeDaoService employeeDaoService;// added to spring MyConfig
//    private ObjectMapper mapper;// added to spring MyConfig
//    private SessionFactory sessionFactory; // added to spring MyConfig
//    private Logger logger = LoggerFactory.getLogger(BeanFactory.class);
//
//    private BeanFactory() {
//        logger.debug("BeanFactory initialized");
//        try {
//            Configuration configuration = new Configuration().configure();
//            configuration.addAnnotatedClass(Department.class);
//            configuration.addAnnotatedClass(Employee.class);
//            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
//            this.sessionFactory = configuration.buildSessionFactory(builder.build());
//            logger.debug("SessionFactory for hibernate created");
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//        this.departmentDaoService = new DepartmentDaoService(sessionFactory);
//        this.employeeDaoService = new EmployeeDaoService(sessionFactory);
//        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        logger.debug("Time Module initialized");
//    }
//
//
//    public static BeanFactory getInstance() {
//        if (instance == null) {
//            instance = new BeanFactory();
//        }
//        return instance;
//    }
//
//    public <T> T getObject(Class<T> aClass) {
//        if (aClass == DepartmentDaoService.class) {
//            return (T) departmentDaoService;
//        } else if (aClass == EmployeeDaoService.class) {
//            return (T) employeeDaoService;
//        }
//        if (aClass == DepartmentService.class) {
//            return (T) new DepartmentService(departmentDaoService);
//        }
//        if (aClass == EmployeeService.class) {
//            return (T) new EmployeeService(employeeDaoService);
//        }
//
//        throw new RuntimeException("there's no such class");
//    }
//
//    public ObjectMapper getMapper() {
//        return mapper;
//    }
//}
