package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Интерфейс UserRepository расширяет JpaRepository и предоставляет методы для работы с пользователями.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
