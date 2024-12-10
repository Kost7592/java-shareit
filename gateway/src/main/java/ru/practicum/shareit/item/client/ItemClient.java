package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;

import java.util.Map;

/**
 *  Класс представляет собой клиент для работы с вещами.
 */
@Service
public class ItemClient extends BaseClient {

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Метод добавления новой вещи.
     * @param itemDto объект с данными о вещи;
     * @param userId идентификатор владельца вещи.
     */
    public ResponseEntity<Object> postItem(ItemDtoRequest itemDto, Long userId) {
        return post("", userId, itemDto);
    }

    /**
     * Метод обновления информации об элементе по идентификатору
     * @param userId идентификатор владельца вещи;
     * @param itemId идентификатор обновляемой вещи;
     * @param itemDto объект с данными для обновления вещи.
     */
    public ResponseEntity<Object> patchItem(Long userId, Long itemId, ItemDtoRequest itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    /**
     * Метод для получения вещи по идентификатору;
     * @param itemId идентификатор вещи;
     * @param ownerId идентификатор владельца.
     */
    public ResponseEntity<Object> getItem(Long itemId, Long ownerId) {
        return get("/" + itemId, ownerId);
    }

    /**
     * Метод получения всех вещей одного владельца
     * @param userId идентификатор владельца;
     * @param from номер начальной позиции в списке результатов;
     * @param size количество объектов, которые должны быть возвращены в ответе.
     */
    public ResponseEntity<Object> getAllItemsUser(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    /**
     * Метод поиска и получения вещей через заданный текст
     * @param userId идентификатор пользователя;
     * @param text текст поиска;
     * @param from номер начальной позиции в списке результатов;
     * @param size количество объектов, которые должны быть возвращены в ответе.
     */
    public ResponseEntity<Object> getSearchOfText(long userId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    /**
     * Метод добавления комментария
     * @param itemId идентификатор комментируемой вещи;
     * @param userId идентификатор пользователя;
     * @param commentDto объект с данными комментария.
     */
    public ResponseEntity<Object> addComment(Long itemId, Long userId, CommentDtoRequest commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
