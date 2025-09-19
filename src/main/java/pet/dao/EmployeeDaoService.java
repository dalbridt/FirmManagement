package pet.dao;

import jakarta.persistence.criteria.*;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import pet.entity.Department;
import pet.entity.Employee;
import pet.util.EmployeeSearchFilter;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeDaoService {
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(EmployeeDaoService.class);


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

            Join<Employee, Department> deptJoin = root.join("department", JoinType.LEFT);
            predicates.add(cb.equal(deptJoin.get("id"),  + filter.getDepartmentId()));
        }

        if (filter.getHireDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("hireDate"), filter.getHireDateFrom()));
        }

        if(filter.getHireDateBefore() != null) {
            predicates.add(cb.lessThan(root.get("hireDate"), filter.getHireDateBefore()));
        }

        if (filter.getRole() != null) {
            predicates.add(cb.like(cb.lower(root.get("role")), "%" + filter.getRole().toLowerCase() + "%"));
        }

        if (filter.getLocation() != null) {
            predicates.add(cb.like(cb.lower(root.get("location")), "%" + filter.getLocation().toLowerCase() + "%"));
        }

        if (filter.getActive() != null) {
            predicates.add(cb.like(root.get("active"), "%" + filter.getActive() + "%"));
        }

        return predicates;
    }


}
