package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Класс User представляет собой модель пользователя.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор пользователя;
 * - name — имя пользователя;
 * - email — электронная почта;
 */
@Entity
@Table(name = "USERS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String name;
    @Column(nullable = false, unique = true)
    @Email(message = "Указан некорректный Email")
    @NotBlank(message = "Поле не может быть пустым")
    private String email;
}
