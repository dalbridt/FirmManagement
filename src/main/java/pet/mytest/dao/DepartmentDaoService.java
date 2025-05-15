package pet.mytest.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.entities.Department;

import java.util.List;

public class DepartmentDaoService {

    private final String GET_ALL_DEPARTMENTS = "from Department";

    private final SessionFactory sessionFactory;
    private Logger logger;

    public DepartmentDaoService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        logger = LoggerFactory.getLogger(DepartmentDaoService.class);
    }


    public int addNewDepartment(Department department) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(department);
            transaction.commit();
            return department.getId();
        }
    }

    public Department getDepartmentById(int departmentId) {
        try(Session session = sessionFactory.openSession()) {
            return session.get(Department.class, departmentId);
        }
    }

    public List<Department> getAllDepartments() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery(GET_ALL_DEPARTMENTS, Department.class).list();
        }
        // todo нужно ли убрать макрос?
        // todo не используется
    }

    public boolean deleteDepartment(int departmentId) {
        //  todo - если департамент не удалился - нужно вернуть 404
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Department department = session.get(Department.class, departmentId);
            session.remove(department);
            transaction.commit();
            return true;
        }
    }
}
