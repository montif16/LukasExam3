package app.entities;

import app.entities.helper.Location;
import app.enums.Level;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@Entity
public class SkiLesson {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
    private int price;
    private Level level;

    @Embedded
    private Location location;

    @ManyToOne
    private Instructor instructor;
}
