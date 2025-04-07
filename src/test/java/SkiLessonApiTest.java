import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class SkiLessonApiTest {
    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7070;
    }

    @BeforeEach
    public void resetDatabase() {
        RestAssured
                .given()
                .when()
                .post("/skilessons/clear-and-populate")
                .then()
                .statusCode(201);
    }


    @Test
    public void testPopulate() {
        RestAssured
                .given()
                .when()
                .post("/skilessons/populate")
                .then()
                .statusCode(201);
    }

    @Test
    public void testGetAllLessons() {
        RestAssured
                .given()
                .when()
                .get("/skilessons")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", not(empty()));
    }

    @Test
    public void testCreateLesson() {
        String body = """
                {
                    "name": "Test Lesson",
                    "level": "beginner",
                    "price": 100,
                    "startTime": "2025-04-10T10:00:00",
                    "endTime": "2025-04-10T12:00:00",
                    "location": { "latitude": 45.0, "longitude": 10.0 }
                }
                """;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/skilessons")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Lesson"));
    }

    @Test
    public void testGetLessonById() {
        int id = createLesson();

        RestAssured
                .given()
                .when()
                .get("/skilessons/" + id)
                .then()
                .statusCode(200)
                .body("lesson.id", equalTo(id));
    }

    @Test
    public void testUpdateLesson() {
        int id = createLesson();

        String updated = """
                {
                    "name": "Updated Lesson",
                    "level": "advanced",
                    "price": 999,
                    "startTime": "2025-04-11T10:00:00",
                    "endTime": "2025-04-11T12:00:00",
                    "location": { "latitude": 45.0, "longitude": 10.0 }
                }
                """;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put("/skilessons/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Lesson"));
    }

    @Test
    public void testDeleteLesson() {
        int id = createLesson();

        RestAssured
                .given()
                .when()
                .delete("/skilessons/" + id)
                .then()
                .statusCode(204);
    }

    @Test
    public void testAddInstructorToLesson() {
        RestAssured
                .given()
                .when()
                .put("/skilessons/2/instructors/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetLessonsByInstructor() {
        RestAssured
                .given()
                .when()
                .get("/skilessons/instructor/1")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    public void testFilterByLevel() {
        RestAssured
                .given()
                .when()
                .get("/skilessons/level/beginner")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTotalPricePerInstructor() {
        RestAssured
                .given()
                .when()
                .get("/skilessons/price/instructors")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    public void testTotalDurationPerInstructor() {
        RestAssured
                .given()
                .when()
                .get("/skilessons/duration/instructors")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    private int createLesson() {
        String body = """
                {
                    "name": "Temp Lesson",
                    "level": "beginner",
                    "price": 100,
                    "startTime": "2025-04-10T10:00:00",
                    "endTime": "2025-04-10T12:00:00",
                    "location": { "latitude": 45.0, "longitude": 10.0 }
                }
                """;

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/skilessons")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
}
