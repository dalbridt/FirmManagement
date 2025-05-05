package pet.mytest.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public interface ServletHandler {
    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
