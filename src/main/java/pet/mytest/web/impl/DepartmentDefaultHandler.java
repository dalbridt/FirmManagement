package pet.mytest.web.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.entities.Department;
import pet.mytest.exceptions.InvalidDataException;
import pet.mytest.service.DepartmentService;
import pet.mytest.web.HandlerUtils;
import pet.mytest.web.ServletHandler;

import java.io.IOException;
import java.util.stream.Collectors;

public class DepartmentDefaultHandler implements ServletHandler {
    private DepartmentService departmentService;
    ObjectMapper mapper;
    Logger logger;

    public DepartmentDefaultHandler(DepartmentService departmentService, ObjectMapper mapper) {
        this.departmentService = departmentService;
        this.mapper = mapper;
        this.logger = LoggerFactory.getLogger(DepartmentDefaultHandler.class);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        if (method.equals("POST")) {
            handlePost(request, response);
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr == null) {
            throw new InvalidDataException("id parameter is required");
        }
        int id = Integer.parseInt(idStr);
        if (method.equals("GET")) {
            Department dep = departmentService.getDepartmentById(id);
            if (dep != null) {
                HandlerUtils.sendResponse(response, mapper.writeValueAsString(dep), 200);
            } else {
                HandlerUtils.sendResponse(response, "NOT FOUND : " + id, 404);
            }
        } else if (method.equals("DELETE")) {
            if (departmentService.deleteDepartment(id)) {
                HandlerUtils.sendResponse(response, "Department deleted: " + id, 200);
            } else {
                HandlerUtils.sendResponse(response, "Could not delete depatrment.no department with id" + id, 404);
            }
        }

    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (!json.isEmpty()) {
            // todo - попробовать перехватить и кинуть кастом исключение
            Department department = mapper.readValue(json, Department.class);
            int departmentId = departmentService.addDepartment(department);
            if (departmentId > 0) {
                HandlerUtils.sendResponse(response, "new depatrment added: " + departmentId, 200);
            } else {
                HandlerUtils.sendResponse(response, "couldn't add new department", 400);
            }
        } else {
            // todo throw invalid data exception
            HandlerUtils.sendResponse(response, "couldn't parse request,no json provided in POST", 400);
        }
    }
}
