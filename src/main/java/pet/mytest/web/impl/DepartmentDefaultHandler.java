package pet.mytest.web.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.dto.DepartmentDTO;
import pet.dto.DepartmentMapper;
import pet.entities.Department;
import pet.mytest.exceptions.InvalidDataException;
import pet.mytest.service.DepartmentService;
import pet.mytest.web.HandlerUtils;
import pet.mytest.web.ServletHandler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DepartmentDefaultHandler implements ServletHandler {
    private DepartmentService departmentService;
    ObjectMapper mapper;
    Logger logger = LoggerFactory.getLogger(DepartmentDefaultHandler.class);

    public DepartmentDefaultHandler(DepartmentService departmentService, ObjectMapper mapper) {
        this.departmentService = departmentService;
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        switch (method) {
            case "GET":
                handleGet(request, response);
                break;
            case "POST":
                handlePost(request, response);
                break;
            case "DELETE":
                handleDelete(request, response);
                break;
            default:
                HandlerUtils.sendResponse(response, "we don't have a handler for your operation, sorry", 404);
                break;
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)  {
        String idStr = request.getParameter("id");
        try {
            int id = Integer.parseInt(idStr);
            boolean deleted = departmentService.deleteDepartment(id);
            if (deleted) {
                HandlerUtils.sendResponse(response, "Department deleted: " + id, 200);
            } else {
                HandlerUtils.sendResponse(response, "Could not delete department : " + id, 404);
            }
        } catch (Exception e) {
            throw new InvalidDataException("Invalid id "  + idStr);
        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        String res;
        if (idStr == null) {
            List<DepartmentDTO> dtos = departmentService.getAllDepartments().stream()
                    .map(DepartmentMapper::convertToDTO).toList();
            res = mapper.writeValueAsString(dtos);
        } else {
            int id = Integer.parseInt(idStr);
            Department dep = departmentService.getDepartmentById(id);
            if (dep == null) {
                HandlerUtils.sendResponse(response, "NOT FOUND : " + id, 404);
                return;
            } else {
                DepartmentDTO dto = DepartmentMapper.convertToDTO(dep);
                res = mapper.writeValueAsString(dto);
            }
        }
        HandlerUtils.sendResponse(response, res, 200);
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (json.isEmpty()) {
            throw new InvalidDataException("json is empty");
        }
        DepartmentDTO dptDto;
        try {
            dptDto = mapper.readValue(json, DepartmentDTO.class);
            Department department = DepartmentMapper.convertToEntity(dptDto);
            int departmentId = departmentService.addDepartment(department);
            if (departmentId > 0) {
                HandlerUtils.sendResponse(response, "new depatrment added: " + departmentId, 200);
            } else {
                HandlerUtils.sendResponse(response, "couldn't add new department", 400);
            }
        } catch (Exception e) {
            throw new InvalidDataException("Invalid json " + json);
        }
    }
}
