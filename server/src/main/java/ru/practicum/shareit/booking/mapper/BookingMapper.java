package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserWithIdDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

/**
 * Класс-маппер для работы с данными о бронированиях.
 */
@UtilityClass
public class BookingMapper {
    /**
     * Преобразует объект DTO запроса на бронирование в объект бронирования.
     *
     * @param bookingDtoRequest объект DTO запроса на бронирование
     * @param item              вещь
     * @param user              пользователь
     * @return объект бронирования
     */
    public static Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }

    /**
     * Преобразует объект бронирования в DTO-объект для ответа.
     *
     * @param booking объект бронирования
     * @return DTO-объект бронирования
     */
    public BookingDtoResponse toBookingDtoResponseMapper(Booking booking) {
        return BookingDtoResponse.builder()
                .id(booking.getId())
                .booker(UserMapper.toUserWithIdDto(booking.getBooker()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(new ItemWithBookingDto(booking.getItem().getId(), booking.getItem().getName()))
                .status(booking.getStatus())
                .build();
    }

    /**
     * Преобразует объект бронирования в DTO-объект информации о бронировании товара.
     *
     * @param booking объект бронирования
     * @return DTO-объект информации о бронировании вещи
     */
    public BookingItemDto toItemBookingInfoDto(Booking booking) {
        return new BookingItemDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd());
    }

    /**
     * Преобразует объект бронирования в DTO-объект информации о бронировании товара.
     *
     * @param booking объект бронирования
     * @return DTO-объект информации о бронировании вещи
     */
    public BookingDtoResponse toBookingForResponseMapper(Booking booking) {
        return BookingDtoResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemWithBookingDto(booking.getItem().getId(), booking.getItem().getName()))
                .booker(new UserWithIdDto(booking.getBooker().getId()))
                .build();
    }
}
