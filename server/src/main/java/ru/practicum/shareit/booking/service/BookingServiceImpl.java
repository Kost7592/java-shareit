package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateTypes;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static ru.practicum.shareit.booking.model.BookingStatus.REJECTED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

/**
 * Класс BookingServiceImpl реализация интерфейса BookingService для работы с бронированиями (bookings).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public BookingDtoResponse addBooking(long userId, BookingDtoRequest bookingDtoRequest) {
        User user = checkUser(userId);
        Item item = itemRepository.findById(bookingDtoRequest.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь с id " + bookingDtoRequest.getItemId() + " не найдена"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь не доступна для бронирования");
        }
        validateBooking(bookingDtoRequest, item, user);
        Booking booking = BookingMapper.toBooking(bookingDtoRequest, item, user);
        booking.setStatus(WAITING);
        booking.setItem(item);
        booking.setBooker(user);
        Booking result = bookingRepository.save(booking);
        return BookingMapper.toBookingForResponseMapper(result);
    }

    @Transactional
    @Override
    public BookingDtoResponse updateBooking(long bookingId, long userId, Boolean approved) {
        Booking booking = checkBooking(bookingId);
        Item item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new ValidationException("Пользователь не является владельцем вещи");
        }
        if (!booking.getStatus().equals(WAITING)) {
            throw new ValidationException("Данное бронирование уже внесено и имеет статус "
                    + booking.getStatus());
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(REJECTED);
        }
        checkUser(userId);
        return BookingMapper.toBookingForResponseMapper(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDtoResponse getBooking(long bookingId, long userId) {
        checkUser(userId);
        checkBooking(bookingId);
        Booking booking = bookingRepository.findById(bookingId).filter(booking1 ->
                booking1.getBooker().getId() == userId
                        || booking1.getItem().getOwner().getId() == userId).orElseThrow(() ->
                new NotFoundException("Пользователь не является владельцем вещи"));
        ;
        return BookingMapper.toBookingForResponseMapper(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoResponse> getAllBookingByUser(String state, long userId) {
        checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        StateTypes stateBooking = StateTypes.getStateFromText(state);
        List<Booking> result = switch (stateBooking) {
            case ALL -> bookingRepository.findByBookerId(userId, Sort.by(DESC, "start"));
            case CURRENT -> bookingRepository.findByBookerAllCurrentBookings(userId, now);
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeAndStatus(userId, now, BookingStatus.APPROVED);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfter(userId, now);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(userId, WAITING);
            case REJECTED ->
                    bookingRepository.findByItem_OwnerIdAndStatusOrStatus(userId, REJECTED, BookingStatus.CANCELED);
        };
        return result.stream().map(BookingMapper::toBookingForResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoResponse> getAllBookingByOwner(String state, long userId) {
        checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        StateTypes stateBooking = StateTypes.getStateFromText(state);
        List<Booking> result = switch (stateBooking) {
            case ALL -> bookingRepository.findByItem_OwnerId(userId);
            case CURRENT -> bookingRepository.findAllCurrentBookingsByOwner(userId, now);
            case PAST -> bookingRepository.findByItem_OwnerIdAndEndBeforeAndStatus(userId, now, BookingStatus.APPROVED);
            case FUTURE -> bookingRepository.findByItem_OwnerIdAndStartAfter(userId, now);
            case WAITING -> bookingRepository.findByItem_OwnerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrStatus(userId, BookingStatus.REJECTED,
                    BookingStatus.CANCELED);
        };
        return result.stream().map(BookingMapper::toBookingForResponseMapper)
                .collect(Collectors.toList());
    }

    /**
     * Метод для проверки пользователя.
     *
     * @param userId — идентификатор проверяемого бронирования.
     */
    private User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    /**
     * Метод для проверки бронирования.
     *
     * @param bookingId — идентификатор проверяемого бронирования.
     */
    private Booking checkBooking(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
    }

    /**
     * Метод проверяет данные, полученные от пользователя через BookingDtoRequest, на соответствие требованиям
     * системы.
     *
     * @param bookingDtoRequest — объект с данными о бронировании.
     * @param item              — вещь, которая бронируется.
     * @param booker            — пользователь, который выполняет бронирование.
     */
    public void validateBooking(BookingDtoRequest bookingDtoRequest, Item item, User booker) {
        if (item.getOwner().getId().equals(booker.getId())) {
            throw new ValidationException("Нельзя забронировать свою вещь");
        }
        List<Booking> intersectBookings = bookingRepository.findByItemIdAndStartAfterAndEndBefore(item.getId(),
                bookingDtoRequest.getStart(), bookingDtoRequest.getEnd());
        if (intersectBookings != null && !intersectBookings.isEmpty()) {
            throw new ValidationException("Найдено пересечение бронирований на вещь " + item.getName());
        }
    }
}
