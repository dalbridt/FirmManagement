package pet.mytest.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.entities.Department;
import pet.mytest.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoService {

    private static final String ADD_DEPARTMENT = "insert into department (name, manager_id)values (?, ?)";
    private static final String GET_DEPARTMENT_BY_NAME = "select * from department where name = ?";
    private static final String GET_DEPARTMENT_BY_ID = "select * from department where id = ?";
    private final String GET_ALL_DEPARTMENTS = "select * from department";
    private final String DELETE_DEPARTMENT = "delete from department where id = ?";


    private final DataSource ds;
    private Logger logger;

    public DepartmentDaoService(DataSource ds) {
        this.ds = ds;
        logger = LoggerFactory.getLogger(DepartmentDaoService.class);
    }

    public int addNewDepartment(Department department) {
        try (PreparedStatement pst = ds.getConnection().prepareStatement(ADD_DEPARTMENT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, department.getName());
            pst.setInt(2, department.getManagerId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't add new department " + e.getMessage());
        }
        return -1;
    }

    public Department getDepartmentByName(String departmentName) {
        try (PreparedStatement pst = ds.getConnection().prepareStatement(GET_DEPARTMENT_BY_NAME)) {
            pst.setString(1, departmentName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    logger.debug("getDepartmentByName called and executed successfully " + rs.getString(2));
                    return mapResultSetToDepartment(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't get department by name" + e.getMessage());
        }
        return null;
    }

    public Department getDepartmentById(int departmentId) {
        try (PreparedStatement pst = ds.getConnection().prepareStatement(GET_DEPARTMENT_BY_ID)) {
            pst.setInt(1, departmentId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    logger.debug("getDepartmentByName called and executed successfully " + rs.getString(2));
                    return mapResultSetToDepartment(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't get department by id" + e.getMessage());
        }
        return null;
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        try (Connection conn = ds.getConnection(); PreparedStatement pst = conn.prepareStatement(GET_ALL_DEPARTMENTS)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    departments.add(mapResultSetToDepartment(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("couldn't get departments" + e.getMessage());
        }
        return departments;
    }

    public boolean deleteDepartment(int departmentId) {
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_DEPARTMENT)) {
            pstmt.setInt(1, departmentId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new DatabaseException("couldn't delete department " + e.getMessage());
        }
    }

    private Department mapResultSetToDepartment(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int managerId = rs.getInt("manager_id");
        return new Department(id, name, managerId);
    }
}
