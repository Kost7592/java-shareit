package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

/**
 * Интерфейс BookingService для работы с бронированиями.
 */
public interface BookingService {
    /**
     * Метод addBooking для добавления бронирования.
     *
     * @param userId            — идентификатор пользователя, который выполняет бронирование.
     * @param bookingDtoRequest — объект с данными о бронировании.
     * @return ответ с информацией о бронировании в формате BookingDtoResponse.
     */
    BookingDtoResponse addBooking(long userId, BookingDtoRequest bookingDtoRequest);

    /**
     * Метод updateBooking для обновления бронирования.
     *
     * @param bookingId — идентификатор бронирования, которое нужно обновить.
     * @param userId    — идентификатор пользователя, который выполняет обновление.
     * @param approved  — флаг, указывающий на статус одобрения бронирования (истина/ложь).
     * @return ответ с информацией о бронировании в формате BookingDtoResponse.
     */
    BookingDtoResponse updateBooking(long bookingId, long userId, Boolean approved);

    /**
     * Метод getBooking для получения информации о бронировании.
     *
     * @param bookingId — идентификатор бронирования, информацию о котором нужно получить.
     * @param userId    — идентификатор пользователя, который выполняет запрос.
     * @return ответ с информацией о бронировании в формате BookingDtoResponse.
     */
    BookingDtoResponse getBooking(long bookingId, long userId);

    /**
     * Метод используется для получения всех бронирований, связанных с пользователем.
     *
     * @param state  — статус бронирования, который может быть использован для фильтрации результатов. Возможные
     *               значения зависят от реализации системы.
     * @param userId — идентификатор пользователя, чьи бронирования нужно получить.
     * @return возвращает список объектов BookingDtoResponse, содержащих информацию о бронированиях пользователя.
     */
    List<BookingDtoResponse> getAllBookingByUser(String state, long userId);

    /**
     * Метод используется для получения всех бронирований, принадлежащих пользователю.
     *
     * @param state  — статус бронирования, который может быть использован для фильтрации результатов. Возможные
     *               значения зависят от реализации системы.
     * @param userId — идентификатор пользователя, чьи бронирования нужно получить.
     * @return возвращает список объектов BookingDtoResponse, содержащих информацию о бронированиях пользователя.
     */
    List<BookingDtoResponse> getAllBookingByOwner(String state, long userId);
}
