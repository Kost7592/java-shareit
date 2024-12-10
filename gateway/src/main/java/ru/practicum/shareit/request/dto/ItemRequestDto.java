package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Класс представляет собой DTO (объект передачи данных) для запросов на добавление элементов (item requests) в систему
 *  Он содержит следующие поля:
 *  - description - текстовое описание запроса.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {
    @NotBlank
    private String description;
}