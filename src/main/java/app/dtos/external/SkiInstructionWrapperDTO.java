package app.dtos.external;

import lombok.Data;

import java.util.List;

@Data
public class SkiInstructionWrapperDTO {
    private List<SkiInstructionDTO> lessons;
}
