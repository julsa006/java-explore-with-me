package ru.practicum.ewmservice.dto.category;

import ru.practicum.ewmservice.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
