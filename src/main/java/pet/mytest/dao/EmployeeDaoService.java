package pet.mytest.dao;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import pet.entities.Employee;
import pet.entities.EmployeeSearchFilter;
import java.util.List;

@Repository
public class EmployeeDaoService {
    private final SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(EmployeeDaoService.class);


    public EmployeeDaoService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Employee getEmployeeById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Employee.class, id);
        }
    }

    public int addEmployee(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
            logger.debug("Employee added: " + employee);
            return employee.getId();
        }
        // todo нужно ли перехватывать и бросать database exception?
    }

    public boolean deleteEmployee(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            session.remove(employee);
            tx.commit();
            logger.debug("Employee deleted: " + employee);
            return true;
        }
    }

    public List<Employee> getEmployeesByFilters(EmployeeSearchFilter filter) {
        try (Session session = sessionFactory.openSession()) {
            String hql = formHqlFromFilters(filter);
            Query<Employee> query  = session.createQuery(hql,Employee.class);
            modifyQuery(query, filter);
            return query.getResultList();
        }
    }

    private String formHqlFromFilters(EmployeeSearchFilter filter) {
        logger.debug("assemble query from filters execute: ");
        StringBuilder hql = new StringBuilder();
        hql.append("from Employee e join fetch e.department where 1=1"); // to avoid lazy initialization exception
        if (filter.getDepartmentId() != null && filter.getDepartmentId() > 0) {
            hql.append(" and e.department.id = :departmentId");
        }

        if (filter.getHireDateFrom() != null) {
            hql.append(" and hireDate > :hireDateFrom");
        }

        if (filter.getHireDateBefore() != null) {
            hql.append(" and hireDate < :hireDateBefore");
        }

        if (filter.getRole() != null) {
            hql.append(" and role like :role");
        }

        if (filter.getLocation() != null) {
            hql.append(" and location like :location");
        }
        return hql.toString();
    }

    private void modifyQuery(Query<Employee> query,EmployeeSearchFilter filter) {
        if (filter.getDepartmentId() != null && filter.getDepartmentId() > 0) {
            query.setParameter("departmentId", filter.getDepartmentId());
        }
        if (filter.getHireDateFrom() != null) {
            query.setParameter("hireDateFrom", filter.getHireDateFrom());
        }
        if (filter.getHireDateBefore() != null) {
            query.setParameter("hireDateBefore", filter.getHireDateBefore());
        }
        if (filter.getRole() != null && !filter.getRole().isBlank()) {
            query.setParameter("role", "%" + filter.getRole() + "%");
        }
        if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
            query.setParameter("location", "%" + filter.getLocation() + "%");
        }
        logger.debug("modify query: " + query);
    }
}
