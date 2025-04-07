package app.dtos.mapper;

import app.dtos.SkiLessonDTO;
import app.entities.SkiLesson;

public class SkiLessonMapper {
    public static SkiLessonDTO toDTO(SkiLesson lesson) {
        // simple DTO conversion
        SkiLessonDTO dto = new SkiLessonDTO();
        dto.setId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setLevel(lesson.getLevel());
        dto.setLocation(lesson.getLocation());
        dto.setPrice(lesson.getPrice());
        dto.setStartTime(lesson.getStartTime());
        dto.setEndTime(lesson.getEndTime());

        var inst = lesson.getInstructor();
        if (inst != null) {
            SkiLessonDTO.InstructorInfo info = new SkiLessonDTO.InstructorInfo();
            info.setId(inst.getId());
            info.setFirstName(inst.getFirstName());
            info.setLastName(inst.getLastName());
            dto.setInstructor(info);
        }

        return dto;
    }
}
