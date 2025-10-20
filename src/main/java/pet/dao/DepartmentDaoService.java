package pet.dao;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pet.entity.Department;
import pet.exception.DatabaseException;
import pet.exception.ObjectNotFoundException;

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
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Error when connecting to the database");
        }
    }

    public Department getDepartmentById(Long departmentId) {
        try(Session session = sessionFactory.openSession()) {
            Department department = session.get(Department.class, departmentId);
            if (department == null) {
                throw new ObjectNotFoundException("Department with id " + departmentId + " not found");
            }
            return department;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Error when connecting to the database");
        }
    }


    public List<Department> getAllDepartments() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery(GET_ALL_DEPARTMENTS, Department.class).list();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Error when connecting to the database");
        }
    }

    public void deleteDepartment(Long departmentId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Department department = session.get(Department.class, departmentId);
            if (department != null) {
                logger.info("Deleting department {}", departmentId);
                session.remove(department);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new ObjectNotFoundException("Department with id " + departmentId + " not found");
            }
        }catch (HibernateException e) {
            throw new DatabaseException("Error when connecting to the database");
        }
    }
}
