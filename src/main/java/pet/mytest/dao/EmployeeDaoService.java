package pet.mytest.dao;

import org.apache.log4j.Logger;
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
    private static final String SELECT_FROM_EMPLOYEE_WHERE_ID = "select * from employee where id = ?";
    private static final String INSERT_INTO_EMPLOYEE = "insert into employee (name, department_id, role, location, salary, hire_date) values(?,?,?,?,?, ?)";
    private static final String EMPLOYEE_WHERE_DEPARTMENT_ID = "select * from employee where department_id = ?";
    private final String DELETE_EMPLOYEE_WHERE_ID = "delete from employee where id = ?";

    private final DataSource ds;
    Logger logger;


    public EmployeeDaoService(DataSource ds) {
        this.ds = ds;
        logger = Logger.getLogger(this.getClass().getName());
    }

    public Employee getEmployeeById(int id) {
        try (Connection conn = ds.getConnection(); PreparedStatement pst = conn.prepareStatement(SELECT_FROM_EMPLOYEE_WHERE_ID)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't get employee by id" + e.getMessage());
        }
        return null;
    }

    public int addEmployee(Employee employee) {
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_EMPLOYEE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getDepartmentId());
            pstmt.setString(3, employee.getRole());
            if (employee.getLocation() != null) {
                pstmt.setString(4, employee.getLocation());
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            pstmt.setDouble(5, employee.getSalary());
            if (employee.getHireDate() != null) {
                Timestamp hireDateTmstmp = Timestamp.valueOf(employee.getHireDate().atStartOfDay());
                pstmt.setTimestamp(6, hireDateTmstmp);
            } else {
                pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            }
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't add employee: " + e.getMessage());
        }
        return -1;
    }

    public List<Employee> getEmployeesByDepartmentId(int id) {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(EMPLOYEE_WHERE_DEPARTMENT_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't get employee by department id" + e.getMessage());
        }
        return employees;
    }

    public boolean deleteEmployee(int id) {
        try (Connection conn = ds.getConnection(); PreparedStatement pst = conn.prepareStatement(DELETE_EMPLOYEE_WHERE_ID)) {
            pst.setInt(1, id);
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new DatabaseException("couldn't delete employee: " + e.getMessage());
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
