package ru.practicum.ewmservice.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.category.CategoryDto;
import ru.practicum.ewmservice.dto.category.CategoryMapper;
import ru.practicum.ewmservice.dto.category.CreateUpdateCategoryDto;
import ru.practicum.ewmservice.model.Category;
import ru.practicum.ewmservice.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateUpdateCategoryDto body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CategoryMapper.toCategoryDto(categoryService.createCategory(body.getName())));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{catId}")
    public Category updateCategory(@PathVariable Long catId, @Valid @RequestBody CreateUpdateCategoryDto body) {
        return categoryService.updateCategory(catId, body.getName());
    }
}
