package org.example.ordersservice.dtos.cliente;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.user.UserInputDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteInputDto extends UserInputDto {

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @JsonDeserialize(using = FechaNacimientoDeserializer.class)
    private LocalDateTime fechaNacimiento;

    public static class FechaNacimientoDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws java.io.IOException {
            String value = p.getValueAsString();
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            if (value.contains("T")) {
                return LocalDateTime.parse(value);
            }
            return java.time.LocalDate.parse(value).atStartOfDay();
        }
    }

}

