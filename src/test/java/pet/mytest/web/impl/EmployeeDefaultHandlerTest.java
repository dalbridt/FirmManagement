package pet.mytest.web.impl;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pet.entities.Department;
import pet.entities.Employee;
import pet.mytest.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


/**
 * to mock a path for logger add parameter to config : -Dcatalina.base=./test_logs
 * to forbid idea create temporary configs : Settings -> Advanced -> set Temporary Run/Debug configurations limit to 0
 */


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmployeeDefaultHandlerTest {
    @Mock
    EmployeeService employeeService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    EmployeeDefaultHandler employeeDefaultHandler;

    PrintWriter printWriter;
    StringWriter stringWriter;


    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        when(request.getPathInfo()).thenReturn("/employee");
        stringWriter = new StringWriter();
        this.printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        // todo один раз на весь тест
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Field mapperField = EmployeeDefaultHandler.class.getDeclaredField("mapper");
        mapperField.setAccessible(true);
        mapperField.set(employeeDefaultHandler, objectMapper);

    }

    @Test
    void getEmployee() throws IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("id")).thenReturn("1");
        // create employee for result
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Test Name");
        employee.setRole("admin");
        Department department = new Department();
        department.setId(1);
        department.setName("Test department 1");
        department.setManagerId(1);
        employee.setDepartment(department);
        employee.setSalary(100);

        when(employeeService.getEmployeeById(1)).thenReturn(employee);

        employeeDefaultHandler.handle(request, response);

        printWriter.flush();
        String responseBody = stringWriter.toString();
        System.out.println(responseBody);
        assertTrue(responseBody.contains("Test Name"));
        assertTrue(responseBody.contains("admin"));
    }

}