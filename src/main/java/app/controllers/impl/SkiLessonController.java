package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.dao.daos.SkiLessonDAO;
import app.dao.daos.InstructorDAO;
import app.dtos.SkiLessonDTO;
import app.dtos.external.SkiInstructionDTO;
import app.dtos.mapper.SkiLessonMapper;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.enums.Level;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import app.service.ExternalAPIService;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SkiLessonController implements IController<SkiLesson, SkiLessonDTO> {

    private final SkiLessonDAO skiLessonDAO;
    private final InstructorDAO instructorDAO;
    public SkiLessonController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("exam3");
        this.skiLessonDAO = SkiLessonDAO.getInstance(emf);
        this.instructorDAO = InstructorDAO.getInstance(emf);
    }


        public void getById(Context ctx) {
            int id = Integer.parseInt(ctx.pathParam("id"));
            SkiLesson lesson = skiLessonDAO.getById(id);
            if (lesson == null)
                throw new ApiException(404, "Ski lesson not found");

            SkiLessonDTO dto = SkiLessonMapper.toDTO(lesson);
            List<SkiInstructionDTO> external = ExternalAPIService.fetchInstructionsByLevel(lesson.getLevel());

            ctx.json(Map.of("lesson", dto, "externalInstructions", external));
        }


    @Override
    public void getAll(Context ctx) {
        List<SkiLessonDTO> lessons = skiLessonDAO.getAll().stream()
                .map(SkiLessonMapper::toDTO)
                .collect(Collectors.toList());
        ctx.json(lessons);
    }

    @Override
    public void create(Context ctx) {
        SkiLesson lesson = ctx.bodyAsClass(SkiLesson.class);
        SkiLesson created = skiLessonDAO.create(lesson);
        ctx.status(HttpStatus.CREATED).json(SkiLessonMapper.toDTO(created));
    }

    @Override
    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        SkiLesson lesson = ctx.bodyAsClass(SkiLesson.class);
        SkiLesson updated = skiLessonDAO.update(id, lesson);
        ctx.json(SkiLessonMapper.toDTO(updated));
    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        SkiLesson lesson = skiLessonDAO.getById(id);
        if (lesson == null) throw new ApiException(404, "Cannot delete: ski lesson not found");
        skiLessonDAO.delete(id);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    // Additional methods for instructor
    public void addInstructorToLesson(Context ctx) {
        int lessonId = Integer.parseInt(ctx.pathParam("lessonId"));
        int instructorId = Integer.parseInt(ctx.pathParam("instructorId"));
        skiLessonDAO.addInstructorToSkiLesson(lessonId, instructorId);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    public void getByInstructorId(Context ctx) {
        int instructorId = Integer.parseInt(ctx.pathParam("instructorId"));
        Set<SkiLessonDTO> result = skiLessonDAO.getSkiLessonsByInstructor(instructorId);
        ctx.json(result);
    }
    public void populate(Context ctx) {
        app.config.Populate.populate(); // calls your existing Populator
        ctx.status(HttpStatus.CREATED).result("Database populated");
    }
    public void getByLevel(Context ctx) {
        String levelParam = ctx.pathParam("level");
        Level level;

        try {
            level = Level.valueOf(levelParam.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid level: " + levelParam);
        }

        List<SkiLessonDTO> filtered = skiLessonDAO.getAll().stream()
                .filter(s -> s.getLevel() == level)
                .map(SkiLessonMapper::toDTO)
                .collect(Collectors.toList());

        ctx.json(filtered);
    }
    public void getTotalPricePerInstructor(Context ctx) {
        List<Instructor> instructors = InstructorDAO.getInstance(HibernateConfig.getEntityManagerFactory("exam3")).getAll();

        var result = instructors.stream()
                .map(i -> {
                    int totalPrice = i.getLessons().stream().mapToInt(SkiLesson::getPrice).sum();
                    return Map.of("instructorId", i.getId(), "totalPrice", totalPrice);
                }).toList();

        ctx.json(result);
    }
    public void getTotalExternalDurationPerInstructor(Context ctx) {
        List<Instructor> instructors = instructorDAO.getAll();

        List<Map<String, Integer>> result = instructors.stream()
                .map(instructor -> {
                    int totalMinutes = instructor.getLessons().stream()
                            .flatMap(lesson -> ExternalAPIService.fetchInstructionsByLevel(lesson.getLevel()).stream())
                            .mapToInt(SkiInstructionDTO::getDurationMinutes)
                            .sum();

                    return Map.of(
                            "instructorId", instructor.getId(),
                            "totalDurationMinutes", totalMinutes
                    );
                }).toList();

        ctx.json(result);
    }
}