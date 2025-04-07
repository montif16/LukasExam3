package app.controllers.impl;

import app.exceptions.ApiException;
import app.exceptions.Message;
import app.routes.AppRoutes;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController {
    private final Logger LOGGER = LoggerFactory.getLogger(AppRoutes.class);

    public void apiExceptionHandler(ApiException e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(e.getCode());
        ctx.json(new Message(e.getCode(), e.getMessage()));
    }
    public void exceptionHandler(Exception e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(500);
        ctx.json(new Message(500, e.getMessage()));
    }

}
