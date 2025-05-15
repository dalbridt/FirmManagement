package pet.mytest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pet.mytest.BeanFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.mytest.service.DepartmentService;
import pet.mytest.web.impl.DepartmentDefaultHandler;
import pet.mytest.web.impl.DepartmentSearchHandler;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

// todo @Slf4j - логгер через аннотацию
@WebServlet("/department/*")
public class DepartmentServlet extends HttpServlet {
    private Logger logger;
    private DepartmentService departmentService;
    private ObjectMapper mapper;
    private Map<String, ServletHandler> handlers;


    @Override
    public void init() {
        this.logger = LoggerFactory.getLogger(DepartmentServlet.class);
        BeanFactory factory = BeanFactory.getInstance();
        this.departmentService = factory.getObject(DepartmentService.class);
        this.mapper = factory.getMapper();
        this.handlers = new HashMap<>();
        DepartmentDefaultHandler defaultHandler = new DepartmentDefaultHandler(departmentService, mapper);
        this.handlers.put("/department", defaultHandler);
        DepartmentSearchHandler searchHandler = new DepartmentSearchHandler(departmentService, mapper);
        this.handlers.put("/search", searchHandler);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("RECEIVED REQUEST DEPARTMENT SERVLET: " + req.getRequestURI() + "?" + req.getQueryString());
        req.getPathInfo();
        String pathInfo = req.getPathInfo();
        ServletHandler handler = handlers.get(pathInfo);
        if(handler != null){
            try {
                handler.handle(req, resp);
            } catch (Exception e) {
                HandlerUtils.handleException(resp,e);
            }
        }else {
            HandlerUtils.sendResponse(resp, "NOT FOUND", 404);
        }
    }
}
