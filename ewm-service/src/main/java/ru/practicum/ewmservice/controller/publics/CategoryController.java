package ru.practicum.ewmservice.controller.publics;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.model.Category;
import ru.practicum.ewmservice.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public Category getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }

    @GetMapping
    public List<Category> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size) {
        return categoryService.getCategories(from, size);
    }
}
