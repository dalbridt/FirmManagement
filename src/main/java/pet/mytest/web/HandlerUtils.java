package pet.mytest.web;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public class HandlerUtils {
    public static void sendResponse(HttpServletResponse response, String message, int code) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
        response.setStatus(code);
    }

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date);
    }

    public static void handleException(HttpServletResponse response, Exception e) throws IOException {
        switch (e.getClass().getName()) {
            case "DatabaseException":
                sendResponse(response, e.getMessage(), 500 );
                break;
            case "InvalidDataException":
                sendResponse(response, e.getMessage(), 400 );
        }
    }
}
