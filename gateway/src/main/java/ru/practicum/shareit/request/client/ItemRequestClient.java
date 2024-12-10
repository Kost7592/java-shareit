package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

/**
 * Класс ItemRequestClient расширяет класс BaseClient и представляет собой клиентскую часть для работы с запросами к
 * системе
 */
@Service
public class ItemRequestClient extends BaseClient {

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Метод отправляет запрос на добавление нового элемента (item) в систему.
     * @param userId  уникальный идентификатор пользователя, который добавляет элемент.
     * @param itemRequestDto объект с данными о новом элементе.
     */
    public ResponseEntity<Object> addItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    /**
     * Метод отправляет запрос на получение информации о запросе на добавление элемента (item) в систему.
     * @param requestId уникальный идентификатор запроса на добавление нового элемента.
     * @param userId уникальный идентификатор пользователя, отправившего запрос.
     */
    public ResponseEntity<Object> getItemRequest(Long requestId, Long userId) {
        return get("/" + requestId, userId);
    }

    /**
     * Метод отправляет запрос на получение информации обо всех запросах на добавление элементов (item requests) в
     * систему
     * @param userId уникальный идентификатор пользователя, чьи запросы необходимо получить.
     */
    public ResponseEntity<Object> getItemRequestsByUserId(Long userId) {
        return get("", userId);
    }

    /**
     * Метод отправляет запрос на получение информации обо всех запросах на добавление элементов (item requests) в
     * систему
     * @param userId уникальный идентификатор пользователя, чьи запросы необходимо получить.
     * @param from смещение (с какого элемента начинать).
     * @param size количество элементов для получения.
     */
    public ResponseEntity<Object> getAllItemRequests(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all", userId, parameters);
    }
}
