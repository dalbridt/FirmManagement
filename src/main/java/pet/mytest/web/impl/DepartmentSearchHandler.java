package pet.mytest.web.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import pet.mytest.service.DepartmentService;
import pet.mytest.web.HandlerUtils;
import pet.mytest.web.ServletHandler;

import java.io.IOException;

public class DepartmentSearchHandler implements ServletHandler {
    private DepartmentService departmentService;
    ObjectMapper mapper;
    Logger logger;

    public DepartmentSearchHandler(DepartmentService departmentService, ObjectMapper mapper) {
        this.departmentService = departmentService;
        this.mapper = mapper;
        this.logger = Logger.getLogger(DepartmentSearchHandler.class);
    }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HandlerUtils.sendResponse(response, "module is under construction", 500);
        // todo - написать обработчик
    }
}
