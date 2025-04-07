package app.routes;


import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AppRoutes {

    private final SkiLessonRoutes skiLessonRoutes = new SkiLessonRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/skilessons", skiLessonRoutes.getRoutes());
        };
    }
}
