package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс ItemRepository расширяет JpaRepository и предоставляет методы для работы с вещами.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Метод getItemsBySearch выполняет запрос к базе данных и возвращает список вещей Item, соответствующих
     * поисковому запросу.
     *
     * @param text — строка с поисковым запросом.
     * @return список вещей Item.
     */
    @Query("SELECT i FROM Item i WHERE i.available = true AND lower(i.name) LIKE lower(?1) OR lower(i.description) " +
            "LIKE lower(?1)")
    List<Item> getItemsBySearch(String text);

    /**
     * Метод findByOwnerId выполняет запрос к базе данных и возвращает список вещей Item, принадлежащих указанному
     * владельцу.
     *
     * @param ownerId — идентификатор владельца.
     * @return список вещей Item.
     */
    List<Item> findByOwnerId(Long ownerId);
}
