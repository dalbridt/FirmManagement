package pet.mytest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pet.mytest.MyConfig;
import pet.mytest.service.EmployeeService;
import pet.mytest.web.impl.EmployeeDefaultHandler;
import pet.mytest.web.impl.EmployeeSearchServletHandlerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/employee/*")
public class EmployeeServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(EmployeeServlet.class);
    private EmployeeService employeeService;
    private ObjectMapper mapper;
    private Map<String, ServletHandler> handlers;

    @Override
    public void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        this.employeeService = (EmployeeService) context.getBean("employeeService");
        this.mapper = (ObjectMapper) context.getBean("objectMapper");

        handlers = new HashMap<>();
        EmployeeDefaultHandler defaultHandler = new EmployeeDefaultHandler(employeeService, mapper);
        handlers.put("/employee", defaultHandler);
        EmployeeSearchServletHandlerImpl employeeSearchServletHandler = new EmployeeSearchServletHandlerImpl(employeeService, mapper);
        handlers.put("/search", employeeSearchServletHandler);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("RECEIVED REQUEST EMPLOYEE SERVLET: " + req.getRequestURI() + "?" + req.getQueryString());
        String pathInfo = req.getPathInfo();
        ServletHandler handler = handlers.get(pathInfo);
        if (handler != null) {
            try {
                handler.handle(req, resp);
            } catch (Exception e) {
                HandlerUtils.handleException(resp, e);
            }
        } else {
            HandlerUtils.sendResponse(resp, "NOT FOUND", 404);
        }
    }
}
