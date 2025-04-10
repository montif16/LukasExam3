package app.dtos.mapper;

import app.dtos.SkiLessonDTO;
import app.entities.SkiLesson;

public class SkiLessonMapper {
    public static SkiLessonDTO toDTO(SkiLesson lesson) {
        // simple DTO conversion
        // Takes a lesson entity as parameter. Creates a DTO from the entity
        SkiLessonDTO dto = new SkiLessonDTO();
        dto.setId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setLevel(lesson.getLevel());
        dto.setLocation(lesson.getLocation());
        dto.setPrice(lesson.getPrice());
        dto.setStartTime(lesson.getStartTime());
        dto.setEndTime(lesson.getEndTime());

        var inst = lesson.getInstructor();
        // If the lessons has an instructor with it then we executes this.
        if (inst != null) {
            // We use our nested Instructor info to parse it nicely in the Json
            SkiLessonDTO.InstructorInfo info = new SkiLessonDTO.InstructorInfo();
            info.setId(inst.getId());
            info.setFirstName(inst.getFirstName());
            info.setLastName(inst.getLastName());
            // We set the instructor of the dto variable with the nice nested helper.
            dto.setInstructor(info);
        }

        return dto;
    }
}
