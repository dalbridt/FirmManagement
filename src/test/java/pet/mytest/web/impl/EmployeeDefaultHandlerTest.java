package pet.mytest.web.impl;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pet.entities.Department;
import pet.entities.Employee;
import pet.mytest.exceptions.InvalidDataException;
import pet.mytest.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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

//    @Mock - так вообще норм  создавать мапер?
    static ObjectMapper objectMapper () {
       return new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

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
        Field mapperField = EmployeeDefaultHandler.class.getDeclaredField("mapper");
        mapperField.setAccessible(true);
        mapperField.set(employeeDefaultHandler, objectMapper());

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
//        System.out.println(responseBody);
        assertTrue(responseBody.contains("Test Name"));
        assertTrue(responseBody.contains("admin"));
//        нельзя проверить, потому что его устанавливает другой метод
//        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }


    @Test
    void handleDelete_failure() throws IOException {
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getParameter("id")).thenReturn("120");
        when(employeeService.deleteEmployee(120)).thenReturn(false);
        employeeDefaultHandler.handle(request, response);
        verify(employeeService).deleteEmployee(120);
        String responseBody = stringWriter.toString();
        System.out.println(responseBody);
        assertTrue(responseBody.contains("Could not delete employee"));

    }

    @Test
    @DisplayName("проверяет исключение Missing required parameter: id ")
    void checkExceptionsInHandle() throws IOException {
        when(request.getMethod()).thenReturn("GET");
        Exception exception = assertThrows(InvalidDataException.class, ()
                -> employeeDefaultHandler.handle(request, response));

        assertEquals("Missing required parameter: id", exception.getMessage());
    }

}