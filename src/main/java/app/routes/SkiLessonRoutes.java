package app.routes;

import app.config.HibernateConfig;
import app.config.Populate;
import app.controllers.impl.SkiLessonController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class SkiLessonRoutes {

    private final SkiLessonController controller = new SkiLessonController();

    public EndpointGroup getRoutes() {
        return () -> {
            post("/populate", controller::populate);
            get("/level/{level}", controller::getByLevel);
            get("/price/instructors", controller::getTotalPricePerInstructor);
            get("/duration/instructors", controller::getTotalExternalDurationPerInstructor);
            put("/{lessonId}/instructors/{instructorId}", controller::addInstructorToLesson);
            get("/instructor/{instructorId}", controller::getByInstructorId);
            post("/", controller::create);
            get("/", controller::getAll);
            put("/{id}", controller::update);
            delete("/{id}", controller::delete);
            get("/{id}", controller::getById);
            post("/clear-and-populate", ctx -> {
                Populate.clearAndPopulate();
                ctx.status(201);
            });
        };
    }

}
