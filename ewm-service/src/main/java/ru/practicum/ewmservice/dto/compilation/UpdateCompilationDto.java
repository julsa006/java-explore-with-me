package ru.practicum.ewmservice.dto.compilation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.dto.NullOrNotBlank;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {
    @NullOrNotBlank
    @Size(min = 1, max = 50)
    String title;
    Boolean pinned;
    List<Long> events;
}
