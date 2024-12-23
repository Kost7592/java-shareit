package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemForItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserForItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс ItemRequestResponseDto представляет собой DTO (Data Transfer Object) для ответа на запрос на получение вещи.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private UserForItemRequestDto requestor;
    private LocalDateTime created;
    private List<ItemForItemRequestResponseDto> items;
}
