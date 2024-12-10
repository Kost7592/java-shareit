package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.error.UnsupportedStatusException;

/**
 * Контроллер BookingController для работы с бронированиями.
 */
@Slf4j
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    static final String userHeader = "X-Sharer-User-Id";
    static final String path = "/{booking-id}";
    private final BookingClient bookingClient;

    /**
     * Метод для добавления бронирования.
     *
     * @param userId     — идентификатор пользователя, который выполняет бронирование.
     * @param requestDto — объект с данными о бронировании.
     * @return ответ с информацией о бронировании в формате BookingDtoResponse.
     */
    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(userHeader) long userId,
                                             @RequestBody @Valid BookingDtoRequest requestDto) {
        log.info("POST запрос на создание бронирования {}, userId={}", requestDto, userId);
        return bookingClient.addBooking(userId, requestDto);
    }

    /**
     * Метод для обновления бронирования.
     *
     * @param bookingId — идентификатор бронирования, которое нужно обновить.
     * @param ownerId   — идентификатор пользователя, который выполняет обновление.
     * @param approved  — флаг, указывающий на статус одобрения бронирования (истина/ложь).
     * @return ответ с информацией о бронировании в формате ResponseEntity.
     */
    @PatchMapping(path)
    public ResponseEntity<Object> updateBooking(@PathVariable("booking-id") Long bookingId,
                                                @RequestHeader(userHeader) Long ownerId,
                                                @RequestParam(name = "approved") boolean approved) {
        log.info("PATCH запрос на обновление бронирования userId={} bookingId={}", ownerId, bookingId);
        return bookingClient.updateBooking(bookingId, ownerId, approved);
    }

    /**
     * Метод для получения информации о бронировании.
     *
     * @param userId    — идентификатор пользователя, который выполняет запрос.
     * @param bookingId — идентификатор бронирования, информацию о котором нужно получить.
     * @return ответ с информацией о бронировании в формате ResponseEntity.
     */
    @GetMapping(path)
    public ResponseEntity<Object> getBooking(@RequestHeader(userHeader) long userId,
                                             @PathVariable("booking-id") Long bookingId) {
        log.info("GET запрос на получение бронирования с id={}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    /**
     * Метод для получения списка бронирований пользователя.
     *
     * @param userId     — идентификатор пользователя, чьи бронирования нужно получить.
     * @param stateParam — статус бронирования (ALL — все бронирования, или другой указанный статус).
     * @param from       номер начальной позиции в списке результатов
     * @param size       количество объектов, которые должны быть возвращены в ответе
     * @return список ответов с информацией о бронировании в формате BookingDtoResponse.
     */
    @GetMapping
    public ResponseEntity<Object> getAllBookingByUser(
            @RequestHeader(userHeader) long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStatusException("Неизвестный статус: " + stateParam));
        log.info("GET запрос на получение всех бронирований state {}, userId={}, from={}, size={}", stateParam, userId,
                from, size);
        return bookingClient.getAllBookingByUser(userId, state, from, size);
    }

    /**
     * Метод для получения списка бронирований владельца.
     *
     * @param userId — идентификатор владельца, чьи бронирования нужно получить.
     * @param state  — статус бронирования (ALL — все бронирования, или другой указанный статус).
     * @param from   номер начальной позиции в списке результатов
     * @param size   количество объектов, которые должны быть возвращены в ответе
     * @return список ответов с информацией о бронировании в формате BookingDtoResponse.
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                        @RequestHeader(userHeader) Long userId,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(defaultValue = "20") Integer size) {
        BookingState stateParam = BookingState.from(state)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));
        log.info("GET запрос на получение бронирований владельца userId={},state {}, from={}, size={}", userId, state,
                from, size);
        return bookingClient.getAllBookingByOwner(userId, stateParam, from, size);
    }
}
