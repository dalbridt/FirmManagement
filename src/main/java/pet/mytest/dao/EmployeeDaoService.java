package pet.mytest.dao;

import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pet.entities.Employee;
import pet.entities.EmployeeSearchFilter;
import pet.mytest.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoService {
    private final SessionFactory sessionFactory;
    private static final String EMPLOYEE_WHERE_DEPARTMENT_ID = "select * from employee where department_id = ?";
    private final String DELETE_EMPLOYEE_WHERE_ID = "delete from employee where id = ?";

    private final DataSource ds;
    Logger logger;


    public EmployeeDaoService(DataSource ds, SessionFactory sessionFactory) {
        this.ds = ds;
        this.logger = LoggerFactory.getLogger(EmployeeDaoService.class);

        this.sessionFactory = sessionFactory;
    }

    // todo remake whole dao with hibernate methods
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

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setDepartmentId(rs.getInt("department_id"));
        if (rs.getString("role") != null) {
            employee.setRole(rs.getString("role"));
        }
        if (rs.getString("location") != null) {
            employee.setLocation(rs.getString("location"));
        }
        employee.setSalary(rs.getDouble("salary"));
        Timestamp hireDate = rs.getTimestamp("hire_date");
        if (hireDate != null) {
            LocalDate hiredate = LocalDate.ofInstant(hireDate.toInstant(), ZoneId.systemDefault());
            employee.setHireDate(hiredate);
        }
        return employee;
    }

    public List<Employee> getEmployeesByFilters(EmployeeSearchFilter filter) {
        logger.debug("getEmployeesByFilters called in dao");
        List<Employee> employees = new ArrayList<>();
        String sql = formQueryFromFilters(filter);
        logger.debug("Executing SQL:" + sql);
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ) {
            modifyPreparedStatement(pstmt, filter);
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't get employee by filters: " + e.getMessage());
        }
        return employees;
    }


    private String formQueryFromFilters(EmployeeSearchFilter filter) {
        logger.debug("formQueryFromFilters called  going to assemble query");
        StringBuilder sql = new StringBuilder();
        sql.append("select * from employee where 1 = 1");
        if (filter.getDepartmentId() != null && filter.getDepartmentId() > 0) {
            sql.append(" and department_id = ?");
        }
        logger.debug("1 ");
        if (filter.getHireDateFrom() != null) {
            sql.append(" and hire_date > ?");
        }
        logger.debug("2 ");
        if (filter.getHireDateBefore() != null) {
            sql.append(" and hire_date < ?");
        }
        logger.debug("3 ");
        if (filter.getRole() != null) {
            sql.append(" and role like ?");
        }
        logger.debug("4 ");
        if (filter.getLocation() != null) {
            sql.append(" and location like ?");
        }
        return sql.toString();
    }

    private void modifyPreparedStatement(PreparedStatement pstmt,EmployeeSearchFilter filter) throws SQLException {
        int count = 1;
        if (filter.getDepartmentId() !=null) {
            pstmt.setInt(count, filter.getDepartmentId());
            count++;
        }
        if (filter.getHireDateFrom() != null) {
            pstmt.setTimestamp(count, Timestamp.valueOf(filter.getHireDateFrom().atStartOfDay()));
            count++;
        }
        if (filter.getHireDateBefore() != null) {
            pstmt.setTimestamp(count, Timestamp.valueOf(filter.getHireDateBefore().atStartOfDay()));
            count++;
        }
        if (filter.getRole() != null) {
            pstmt.setString(count, filter.getRole());
            count++;
        }
        if (filter.getLocation() != null) {
            pstmt.setString(count, filter.getLocation());
        }
    }
}
