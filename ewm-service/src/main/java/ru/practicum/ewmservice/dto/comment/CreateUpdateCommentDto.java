package ru.practicum.ewmservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateCommentDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 2000)
    private String text;
}
