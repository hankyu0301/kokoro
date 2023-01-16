package com.janghankyu.kokoro.controller.category;

import com.janghankyu.kokoro.dto.category.CategoryCreateRequest;
import com.janghankyu.kokoro.dto.response.Response;
import com.janghankyu.kokoro.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll() {
        return Response.success(categoryService.readAll());
    }

    @PostMapping("/api/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody CategoryCreateRequest req) {
        categoryService.create(req);
        return Response.success();
    }

    @DeleteMapping("/api/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Response.success();
    }
}
