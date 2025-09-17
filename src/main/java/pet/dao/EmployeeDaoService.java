package pet.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import pet.entity.Employee;
import pet.util.EmployeeSearchFilter;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@Repository
public class EmployeeDaoService {
    private final SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(EmployeeDaoService.class);


    public EmployeeDaoService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Employee getEmployeeById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Employee.class, id);
        }
    }

    public Employee createEmployee(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
            logger.debug("Employee added: " + employee);
            return employee;
        }
    }

    public boolean deleteEmployee(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            session.remove(employee);
            tx.commit();
            logger.debug("Employee deleted: " + employee);
            return true;
        }
    }

    public List<Employee> getEmployeesByParams(EmployeeSearchFilter filter){
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class); // какой тип будет в ответе
            Root<Employee> root = query.from(Employee.class); // откуда будем брать данные
            List <Predicate> predicates = getPredicates(filter, cb, root);
            query.select(root).where(predicates.toArray(new Predicate[0]));
            List<Employee> result = session.createQuery(query).getResultList();

            tx.commit();
            return result;
        }

    }

    private List <Predicate> getPredicates(EmployeeSearchFilter filter, CriteriaBuilder cb,
                                           Root<Employee> root) {
        List <Predicate> predicates = new ArrayList<>();
        if (filter.getDepartmentId() != null) {
            predicates.add(cb.like(root.get("department_id"), "%" + filter.getDepartmentId() + "%"));
        }
        if (filter.getHireDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("hireDate"), filter.getHireDateFrom()));
        }
        if (filter.getRole() != null) {
            predicates.add(cb.like(root.get("role"), "%" + filter.getRole() + "%"));
        }
        if (filter.getLocation() != null) {
            predicates.add(cb.like(root.get("location"), "%" + filter.getLocation() + "%"));
        }
        if (filter.getActive() != null) {
            predicates.add(cb.like(root.get("active"), "%" + filter.getActive() + "%"));
        }

        return predicates;
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

        if (filter.getRole() != null && !filter.getRole().isBlank()) {
            query.setParameter("role", "%" + filter.getRole() + "%");
        }
        if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
            query.setParameter("location", "%" + filter.getLocation() + "%");
        }
        logger.debug("modify query: " + query);
    }
}
