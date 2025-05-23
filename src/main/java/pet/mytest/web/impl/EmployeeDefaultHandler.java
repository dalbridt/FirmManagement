package pet.mytest.web.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.dto.EmployeeDTO;
import pet.dto.EmployeeMapper;
import pet.entities.Employee;
import pet.mytest.exceptions.InvalidDataException;
import pet.mytest.service.EmployeeService;
import pet.mytest.web.HandlerUtils;
import pet.mytest.web.ServletHandler;


import java.io.IOException;
import java.util.stream.Collectors;

public class EmployeeDefaultHandler implements ServletHandler {
    private EmployeeService employeeService;
    ObjectMapper mapper;
    Logger logger = LoggerFactory.getLogger(EmployeeDefaultHandler.class);

    public EmployeeDefaultHandler(EmployeeService employeeService, ObjectMapper mapper) {
        this.employeeService = employeeService;
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, IOException {
        String method = request.getMethod();
        if (method.equals("POST")) {
            logger.debug("default handler: method: " + method);
            handlePost(request, response);
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr == null) {
            throw new InvalidDataException("Missing required parameter: id");
        }
        try {
            int id = Integer.parseInt(idStr);
            if (method.equals("GET")) {
                handleGet(response, id);
            } else if (method.equals("DELETE")) {
                handleDelete(response, id);
            }
        } catch (Exception e) {
            throw new InvalidDataException("couldn't process request: " + e.getMessage());
        }
    }

    // todo везде где отправляютя http - ответы длжны быть дто, маппер не конвертирует теперь entity по-нормальному
    private void  handleGet(HttpServletResponse response, int id) throws IOException {
        Employee empl = employeeService.getEmployeeById(id);
        if (empl != null) {
            EmployeeDTO employeeDTO = EmployeeMapper.convertToDTO(empl);
            try{
                String res = mapper.writeValueAsString(employeeDTO);
                HandlerUtils.sendResponse(response, res, 200);
            } catch (Exception e) {
                throw new InvalidDataException("couldn't serialize to JSON");
            }
            // todo перехватывать ошибки от mapper вездде

        } else {
            HandlerUtils.sendResponse(response, "NOT FOUND : " + id, 400);
        }
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, IOException {
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (!json.isEmpty()) {
            EmployeeDTO employeeDTO = mapper.readValue(json, EmployeeDTO.class);

            // todo как быть с депратаментом тут? сходить в базу и взять объект?
            Employee employee = EmployeeMapper.convertToEntity(employeeDTO);
            try {
                int newEmployeeId = employeeService.addNewEmployee(employee);
                HandlerUtils.sendResponse(response, " new employee added:  " + newEmployeeId + " " + employeeDTO.getName() + " " + employeeDTO.getRole() + "\n", 200);
            } catch (Exception e) {
                HandlerUtils.sendResponse(response, e.getMessage(), 500);
            }
        } else {
            HandlerUtils.sendResponse(response, "couldn't parse employee from json", 400);
        }
    }

    private void handleDelete(HttpServletResponse response, int id) throws IllegalArgumentException, IOException {
        if (employeeService.deleteEmployee(id)) {
            HandlerUtils.sendResponse(response, "Employee deleted: " + id, 200);
        } else {
            // todo нужно ли везде где 404 заменить на ошибку?
            HandlerUtils.sendResponse(response, "Could not delete employee.no employee with id" + id, 404);
        }
    }
}
