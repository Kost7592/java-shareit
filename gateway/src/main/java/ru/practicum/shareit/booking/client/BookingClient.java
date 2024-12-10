package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

/**
 * Класс представляет собой клиент для работы с бронированиями.
 */
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Метод возвращает список объектов бронирования для указанного пользователя.
     *
     * @param userId идентификатор пользователя
     * @param state  состояние бронирования, которое определяет, какие объекты будут включены в ответ
     * @param from   номер начальной позиции в списке результатов
     * @param size   количество объектов, которые должны быть возвращены в ответе
     */
    public ResponseEntity<Object> getAllBookingByUser(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    /**
     * Метод добавляет новое бронирование для указанного пользователя.
     *
     * @param userId     идентификатор пользователя
     * @param requestDto объект с данными о бронировании
     */
    public ResponseEntity<Object> addBooking(long userId, BookingDtoRequest requestDto) {
        return post("", userId, requestDto);
    }

    /**
     * Метод возвращает информацию о бронировании по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @param bookingId уникальный идентификатор бронирования
     */
    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    /**
     * Метод обновляет информацию о бронировании по идентификатору.
     *
     * @param bookingId уникальный идентификатор бронирования
     * @param ownerId идентификатор владельца бронирования
     * @param approved @param approved новое значение статуса бронирования
     */
    public ResponseEntity<Object> updateBooking(Long bookingId, Long ownerId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters);
    }

    /**
     * Метод получает все бронирования по идентификатору владельца
     * @param userId идентификатор владельца
     * @param state состояние бронирования, которое определяет, какие объекты будут включены в ответ
     * @param from номер начальной позиции в списке результатов
     * @param size количество объектов, которые должны быть возвращены в ответе
     */
    public ResponseEntity<Object> getAllBookingByOwner(Long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}