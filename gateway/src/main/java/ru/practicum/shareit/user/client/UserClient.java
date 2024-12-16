package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDtoRequest;

/**
 *  Класс представляет собой клиент для работы с пользователями.
 */
@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Метод отправляет запрос на добавление нового пользователя (user) в систему.
     * @param userDto объект с данными о новом пользователе.
     */
    public ResponseEntity<Object> postUser(UserDtoRequest userDto) {
        return post("", userDto);
    }

    /**
     * Метод отправляет запрос на изменение данных пользователя с идентификатором userId
     * @param userDto объект с обновленными данными о пользователе.
     * @param userId идентификатор обновляемого пользователя
     */
    public ResponseEntity<Object> patchUser(UserDtoRequest userDto, long userId) {
        return patch("/" + userId, userDto);
    }

    /**
     * Метод отправляет запрос на удаление данных пользователя с идентификатором userId.
     * @param userId идентификатор пользователя.
     */
    public ResponseEntity<Object> delete(long userId) {
        return delete("/" + userId);
    }

    /**
     * Метод отправляет запрос на получение данных пользователя с идентификатором userId.
     * @param userId идентификатор пользователя.
     */
    public ResponseEntity<Object> getUser(long userId) {
        return get("/" + userId);
    }

    /**
     * Метод отправляет запрос на получение данных всех пользователей.
     */
    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }
}
