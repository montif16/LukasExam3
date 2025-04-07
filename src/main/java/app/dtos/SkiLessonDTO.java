package app.dtos;

import app.entities.helper.Location;
import app.enums.Level;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class SkiLessonDTO {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
    private int price;
    private Level level;
    private Location location;
    private InstructorInfo instructor; // simplified instructor info

    @Data
    @NoArgsConstructor
    //Nested helper class keeps Json responds more clean when reading a lesson from endpoint
    public static class InstructorInfo {
        private int id;
        private String firstName;
        private String lastName;
    }
}
