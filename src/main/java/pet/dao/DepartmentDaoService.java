package pet.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pet.entity.Department;

import java.util.List;

@Repository
public class DepartmentDaoService {

    private final String GET_ALL_DEPARTMENTS = "from Department";

    private final SessionFactory sessionFactory;
    private Logger logger = LoggerFactory.getLogger(DepartmentDaoService.class);

    public DepartmentDaoService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Department createDepartment(Department department) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(department);
            transaction.commit();
            return department;
        }
    }

    public Department getDepartmentById(Long departmentId) {
        try(Session session = sessionFactory.openSession()) {
            return session.get(Department.class, departmentId);
        }
    }


    public List<Department> getAllDepartments() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery(GET_ALL_DEPARTMENTS, Department.class).list();
        }
    }

    public boolean deleteDepartment(Long departmentId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Department department = session.get(Department.class, departmentId);
            if (department != null) {
                session.remove(department);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        }
    }
}
