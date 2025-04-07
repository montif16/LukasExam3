package app.dtos.external;

import lombok.Data;

@Data
public class SkiInstructionDTO {
    private String title;
    private String description;
    private String level;
    private int durationMinutes;
    private String createdAt;
    private String updatedAt;
}
