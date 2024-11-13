package ru.practicum.shareit.request.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс ItemRequest для хранения данных запроса на создание, обновление или поиск вещи.
 *  * Он содержит следующие поля:
 *  — id — уникальный идентификатор запроса;
 *  — description — описание запроса;
 *  — requestor — пользователь, запрашивающий вещь;
 *  — created — дата и время создания запроса;
 */
@Data
@Entity
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
