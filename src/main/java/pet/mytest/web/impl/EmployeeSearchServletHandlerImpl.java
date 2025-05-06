package pet.mytest.web.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.entities.Employee;
import pet.entities.EmployeeSearchFilter;
import pet.mytest.service.EmployeeService;
import pet.mytest.web.HandlerUtils;
import pet.mytest.web.ServletHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class EmployeeSearchServletHandlerImpl implements ServletHandler {

    private EmployeeService employeeService;
    private ObjectMapper objectMapper;
    private Logger logger;

    public EmployeeSearchServletHandlerImpl(EmployeeService employeeService, ObjectMapper objectMapper) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
        this.logger = LoggerFactory.getLogger(EmployeeSearchServletHandlerImpl.class);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        if(!"GET".equalsIgnoreCase(method)) {
           HandlerUtils.sendResponse(response, "MEthod " + method + " is not allowed " , 405);
        }

        EmployeeSearchFilter employeeSearchFilter = new EmployeeSearchFilter();

        String departmentId = request.getParameter("departmentId");
        if(departmentId != null) employeeSearchFilter.setDepartmentId(Integer.parseInt(departmentId));

        String hireDateFrom = request.getParameter("hireDateFrom");
        if(hireDateFrom != null) {
            LocalDate hireDate = HandlerUtils.parseDate(hireDateFrom);
            employeeSearchFilter.setHireDateFrom(hireDate);
        }

        String hireDateBefore = request.getParameter("hireDateBefore");
        if(hireDateBefore != null){
            LocalDate hireDate = HandlerUtils.parseDate(hireDateBefore);
            employeeSearchFilter.setHireDateBefore(hireDate);
        }
        String role = request.getParameter("role");
        if(role != null) employeeSearchFilter.setRole(role);

        String location = request.getParameter("location");
        if(location != null) employeeSearchFilter.setLocation(location);

        logger.debug("Search filters:" + employeeSearchFilter);

        List<Employee> employees = employeeService.getEmployeesByFilters(employeeSearchFilter);
        String json = objectMapper.writeValueAsString(employees);
        HandlerUtils.sendResponse(response, json, 200);
    }
}
