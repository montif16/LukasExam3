package app.service;

import app.dtos.external.SkiInstructionDTO;
import app.dtos.external.SkiInstructionWrapperDTO;
import app.enums.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ExternalAPIService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<SkiInstructionDTO> fetchInstructionsByLevel(Level level) {
        String url = "https://apiprovider.cphbusinessapps.dk/skilesson/" + level.name();

        try {
            SkiInstructionWrapperDTO wrapper = mapper.readValue(new URL(url), SkiInstructionWrapperDTO.class);
            return wrapper.getLessons();
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch instructions: " + e.getMessage(), e);
        }
    }


}
