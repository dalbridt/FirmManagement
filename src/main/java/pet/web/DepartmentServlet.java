//package pet.web;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import pet.config.MyConfig;
//import pet.service.DepartmentService;
//import pet.util.Utils;
//import pet.web.impl.DepartmentDefaultHandler;
//import pet.web.impl.DepartmentSearchHandler;
//
//import java.io.IOException;
//import java.util.HashMap;
//
//import java.util.Map;
//
//@WebServlet("/department/*")
//public class DepartmentServlet extends HttpServlet {
//    private Logger logger = LoggerFactory.getLogger(DepartmentServlet.class);
//    private DepartmentService departmentService;
//    private ObjectMapper mapper;
//    private Map<String, ServletHandler> handlers;
//
//
//    @Override
//    public void init() {
//        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
//        this.departmentService = context.getBean(DepartmentService.class);
//        this.mapper = context.getBean(ObjectMapper.class);
//
//
//        this.handlers = new HashMap<>();
//        DepartmentDefaultHandler defaultHandler = new DepartmentDefaultHandler(departmentService, mapper);
//        this.handlers.put("/department", defaultHandler);
//        DepartmentSearchHandler searchHandler = new DepartmentSearchHandler(departmentService, mapper);
//        this.handlers.put("/search", searchHandler);
//    }
//
//    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        logger.debug("RECEIVED REQUEST DEPARTMENT SERVLET: " + req.getRequestURI() + "?" + req.getQueryString());
//        req.getPathInfo();
//        String pathInfo = req.getPathInfo();
//        ServletHandler handler = handlers.get(pathInfo);
//        if(handler != null){
//            try {
//                handler.handle(req, resp);
//            } catch (Exception e) {
//                Utils.handleException(resp,e);
//            }
//        }else {
//            Utils.sendResponse(resp, "NOT FOUND", 404);
//        }
//    }
//}
