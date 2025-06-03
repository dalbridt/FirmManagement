package pet.mytest.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pet.mytest.web.impl.EmployeeDefaultHandler;
import pet.mytest.web.impl.EmployeeSearchServletHandlerImpl;

import java.io.IOException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * to mock a path for logger add parameter to launch config : -Dcatalina.base=./test_logs
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // need it to ignore UnnecessaryStubbingException - лишние моки, которые не использует тест
class EmployeeServletTestWithMocks {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    EmployeeDefaultHandler employeeDefaultHandler;

    @Mock
    EmployeeSearchServletHandlerImpl employeeSearchHandler;

    @InjectMocks
    EmployeeServlet employeeServlet;

    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, ServletHandler> mockHandlers = new HashMap<>();
        mockHandlers.put("/employee", employeeDefaultHandler);
        mockHandlers.put("/search", employeeSearchHandler);

        // reflection to add handlers to mocked servlet
        Field handlersField = EmployeeServlet.class.getDeclaredField("handlers");
        handlersField.setAccessible(true);
        handlersField.set(employeeServlet, mockHandlers);
    }

    @Test
    void serviceCallsSearchHandler() throws IOException {
        when(request.getRequestURI()).thenReturn("/search");
        when(request.getPathInfo()).thenReturn("/search");

        try{
            employeeServlet.service(request, response);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
      verify(employeeSearchHandler).handle(request, response);
    }

    @Test
    void servletCallsHandlerDefault() throws IOException {
        // set up conditions
        when(request.getRequestURI()).thenReturn("/employee");
        when(request.getPathInfo()).thenReturn("/employee");
        when(request.getParameter("id")).thenReturn("1");

        try {
            employeeServlet.service(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        verify(employeeDefaultHandler).handle(request, response);
    }

}