package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * Класс Item представляет запись о вещи.
 * Он содержит следующие поля:
 * — id — уникальный идентификатор вещи;
 * - name — название вещи;
 * - description — описание вещи;
 * - available — статус доступности;
 * - ownerId — идентификатор пользователя-владельца;
 * - request — запрос, в ответ на который создана вещь.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Поле не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank(message = "Поле не может быть пустым")
    @Column(name = "description", nullable = false)
    private String description;
    @NotNull(message = "Поле не может быть null")
    @Column(name = "available", nullable = false)
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
