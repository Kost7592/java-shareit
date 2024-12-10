package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * Контроллер BookingController для работы с бронированиями.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    /**
     * Метод для добавления бронирования.
     *
     * @param userId            — идентификатор пользователя, который выполняет бронирование.
     * @param bookingDtoRequest — объект с данными о бронировании.
     * @return ответ с информацией о бронировании в формате BookingDtoResponse.
     */
    @PostMapping
    public BookingDtoResponse addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        log.info("Получен запрос на создание бронирования");
        return service.addBooking(userId, bookingDtoRequest);
    }

    /**
     * Метод для обновления бронирования.
     *
     * @param bookingId — идентификатор бронирования, которое нужно обновить.
     * @param userId    — идентификатор пользователя, который выполняет обновление.
     * @param approved  — флаг, указывающий на статус одобрения бронирования (истина/ложь).
     * @return ответ с информацией о бронировании в формате BookingDtoResponse.
     */
    @PatchMapping("/{booking-id}")
    public BookingDtoResponse updateBooking(@PathVariable("booking-id") long bookingId,
                                            @RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam Boolean approved) {
        log.info("Получен запрос на обновление бронирования");
        return service.updateBooking(bookingId, userId, approved);
    }

    /**
     * Метод для получения информации о бронировании.
     *
     * @param bookingId — идентификатор бронирования, информацию о котором нужно получить.
     * @param userId    — идентификатор пользователя, который выполняет запрос.
     * @return ответ с информацией о бронировании в формате BookingDtoResponse.
     */
    @GetMapping("/{booking-id}")
    public BookingDtoResponse getBooking(@PathVariable("booking-id") long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение бронирования");
        return service.getBooking(bookingId, userId);
    }

    /**
     * Метод для получения списка бронирований пользователя.
     *
     * @param state  — статус бронирования (ALL — все бронирования, или другой указанный статус).
     * @param userId — идентификатор пользователя, чьи бронирования нужно получить.
     * @return список ответов с информацией о бронировании в формате BookingDtoResponse.
     */
    @GetMapping
    public List<BookingDtoResponse> getAllBookingByUser(@RequestParam(defaultValue = "ALL") String state,
                                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение списка бронирования пользователя с id: {} со статусом {}", userId, state);
        return service.getAllBookingByUser(state, userId);
    }

    /**
     * Метод для получения списка бронирований, принадлежащих определённому владельцу.
     *
     * @param state  — статус бронирования (ALL — все бронирования, или другой указанный статус).
     * @param userId — идентификатор пользователя, который выполняет запрос.
     * @return список ответов с информацией о бронировании в формате BookingDtoResponse.
     */
    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllBookingByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение бронирований владельца с id: {} со статусом {}", userId, state);
        return service.getAllBookingByOwner(state, userId);
    }
}
