import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ServletTestWithRestAssured {
    static {
        RestAssured.baseURI = "http://localhost:8080/firm-management";
    }

    @Test
    void getEmployeeWithId() {
        given()
                .queryParam("id", 1)
                .when()
                .get("/employee/employee")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", notNullValue())
                .body("role", notNullValue());
    }

    @Test
    void getEmployeeFail() {
        given().queryParam("id", 150)
                .when()
                .get("/employee/employee")
                .then()
                .statusCode(400);
    }

    @Test
    void getEmployeeWithSearchFilter() {
        given().queryParam("departmentId", 1)
                .when()
                .get("/employee/search/")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(4));

    }
}
