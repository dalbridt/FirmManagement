//package pet.web.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import pet.service.DepartmentService;
//import pet.util.Utils;
//import pet.web.ServletHandler;
//
//import java.io.IOException;
//
//@Component
//public class DepartmentSearchHandler implements ServletHandler {
//    private DepartmentService departmentService;
//    ObjectMapper mapper;
//    Logger logger =  LoggerFactory.getLogger(DepartmentSearchHandler.class);
//
//    public DepartmentSearchHandler(DepartmentService departmentService, ObjectMapper mapper) {
//        this.departmentService = departmentService;
//        this.mapper = mapper;
//
//    }
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Utils.sendResponse(response, "module is under construction", 500);
//    }
//}
