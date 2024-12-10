package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс CommentRepository расширяет JpaRepository и предоставляет методы для работы с комментариями.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Метод findByItemIn возвращает список комментариев, связанных с одной или несколькими вещами из списка items.
     *
     * @param items   — список вещей, с которыми связаны комментарии.
     * @param created — сортировка по полю created.
     * @return список комментариев.
     */
    List<Comment> findByItemIn(List<Item> items, Sort created);
}
