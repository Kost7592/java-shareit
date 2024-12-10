package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс ItemRequest для хранения данных запроса на создание, обновление или поиск вещи.
 *  * Он содержит следующие поля:
 *  — id — уникальный идентификатор запроса;
 *  — description — описание запроса;
 *  — requestor — пользователь, запрашивающий вещь;
 *  — created — дата и время создания запроса;
 */
@Entity
@Table(name = "requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne
    private User requestor;
    private LocalDateTime created;
    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
    private List<Item> items;
}
