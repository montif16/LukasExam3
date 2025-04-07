package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AppRoutes {

    //private final StudentRoutes studentRoutes = new StudentRoutes();
    //private final ItemRoutes itemRoutes = new ItemRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            //path("/students", studentRoutes.getRoutes());
            //path("/items", itemRoutes.getRoutes());
        };
    }
}
