package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Метод находит все бронирования для указанных вещей, которые соответствуют заданному статусу.
     *
     * @param items   — список вещей, для которых нужно найти бронирования.
     * @param status  — статус, который должны иметь бронирования, чтобы быть включёнными в результат.
     * @param created — порядок сортировки результатов по дате создания.
     * @return возвращает список всех бронирований для указанных вещей и статуса.
     */
    List<Booking> findByItemInAndStatus(List<Item> items, BookingStatus status, Sort created);

    /**
     * Метод находит все бронирования, сделанные пользователем с указанным идентификатором.
     *
     * @param userId — идентификатор пользователя, чьи бронирования нужно найти.
     * @param sort   — порядок сортировки результатов.
     * @return возвращает список всех бронирований, сделанных указанным пользователем.
     */
    List<Booking> findByBookerId(Long userId, Sort sort);

    /**
     * Метод находит все бронирования, сделанные пользователем с указанным идентификатором и статусом.
     *
     * @param userId — идентификатор пользователя, чьи бронирования нужно найти.
     * @param status — статус, который должны иметь бронирования, чтобы быть включёнными в результат.
     * @return возвращает список всех бронирований, сделанных указанным пользователем и имеющих указанный статус.
     */
    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus status);

    /**
     * Метод находит все бронирования, связанные с определённым владельцем вещи.
     *
     * @param ownerId — идентификатор владельца вещи, чьи бронирования нужно найти.
     * @return возвращает список всех бронирований, связанных с указанным владельцем вещи.
     */
    List<Booking> findByItem_OwnerId(Long ownerId);

    /**
     * Метод находит все бронирования, сделанные пользователем с указанным идентификатором, которые заканчиваются
     * до указанной даты и имеют указанный статус.
     *
     * @param userId — идентификатор пользователя, чьи бронирования нужно найти.
     * @param now    — дата, до которой должны заканчиваться бронирования, чтобы быть включёнными в результат.
     * @param status — статус, который должны иметь бронирования, чтобы быть включёнными в результат.
     * @return возвращает список всех бронирований, сделанных указанным пользователем, которые заканчиваются до
     * указанной даты и имеют указанный статус.
     */
    List<Booking> findByBookerIdAndEndBeforeAndStatus(Long userId, LocalDateTime now, BookingStatus status);

    /**
     * Метод находит все бронирования для вещей конкретного владельца, которые заканчиваются до указанной даты
     * и имеют указанный статус.
     *
     * @param userId — идентификатор владельца вещи, чьи бронирования нужно найти.
     * @param now    — дата, до которой должны заканчиваться бронирования, чтобы быть включёнными в результат.
     * @param status — статус, который должны иметь бронирования, чтобы быть включёнными в результат.
     * @return возвращает список всех бронирований для вещей указанного владельца, которые заканчиваются до
     * указанной даты и имеют указанный статус.
     */
    List<Booking> findByItem_OwnerIdAndEndBeforeAndStatus(Long userId, LocalDateTime now, BookingStatus status);

    /**
     * Этот метод находит все бронирования, сделанные пользователем с указанным идентификатором, которые начинаются
     * после указанной даты.
     *
     * @param userId — идентификатор пользователя, чьи бронирования нужно найти.
     * @param now    — дата, после которой должны начинаться бронирования, чтобы быть включёнными в результат.
     * @return возвращает список всех бронирований, сделанных указанным пользователем, которые начинаются после
     * указанной даты.
     */
    List<Booking> findByBookerIdAndStartAfter(Long userId, LocalDateTime now);

    /**
     * Этот метод находит все бронирования для вещей конкретного владельца, которые имеют указанный статус или
     * другой указанный статус.
     *
     * @param userId      — идентификатор владельца вещи, чьи бронирования нужно найти.
     * @param status      — первый статус, который должны иметь бронирования, чтобы быть включёнными в результат.
     * @param otherStatus — второй статус, который также должен быть у бронирований, чтобы они были включены в
     *                    результат.
     * @return возвращает список всех бронирований для вещей указанного владельца, которые имеют один из указанных
     * статусов.
     */
    List<Booking> findByItem_OwnerIdAndStatusOrStatus(Long userId, BookingStatus status, BookingStatus otherStatus);

    /**
     * Этот метод находит все бронирования, сделанные пользователем с указанным идентификатором, которые имеют указанный
     * статус или другой указанный статус.
     *
     * @param userId      — идентификатор пользователя, чьи бронирования нужно найти.
     * @param status      — первый статус, который должны иметь бронирования, чтобы быть включёнными в результат.
     * @param otherStatus — второй статус, который также должен быть у бронирований, чтобы они были включены в
     *                    результат.
     * @return возвращает список всех бронирований, сделанных указанным пользователем, которые имеют один из указанных
     * статусов.
     */
    List<Booking> findByBookerIdAndStatusOrStatus(Long userId, BookingStatus status, BookingStatus otherStatus);

    /**
     * Этот метод находит все бронирования для вещи конкретного владельца, которые имеют указанный статус.
     *
     * @param userId — идентификатор владельца вещи, чьи бронирования нужно найти.
     * @param status — статус, который должны иметь бронирования, чтобы быть включёнными в результат.
     * @return возвращает список всех бронирований для вещей указанного владельца, которые имеют указанный статус.
     */
    List<Booking> findByItem_OwnerIdAndStatus(Long userId, BookingStatus status);

    /**
     * Метод находит все бронирования для вещей конкретного владельца, которые начинаются после указанной даты.
     *
     * @param userId — идентификатор владельца вещи, чьи бронирования нужно найти.
     * @param now    — дата, после которой должны начинаться бронирования, чтобы быть включёнными в результат.
     * @return возвращает список всех бронирований для вещей указанного владельца, которые имеют указанный статус.
     */
    List<Booking> findByItem_OwnerIdAndStartAfter(Long userId, LocalDateTime now);

    /**
     * Метод находит все бронирования конкретной вещи, которые начинаются после указанной даты.
     *
     * @param itemId — идентификатор владельца вещи, чьи бронирования нужно найти.
     * @param start  — дата, после которой должны начинаться бронирования, чтобы быть включёнными в результат.
     * @param end    — дата, до которой должны начинаться бронирования, чтобы быть включёнными в результат.
     * @return возвращает список всех бронирований для вещей указанного владельца, которые имеют указанный статус.
     */
    List<Booking> findByItemIdAndStartAfterAndEndBefore(Long itemId, LocalDateTime start, LocalDateTime end);

    /**
     * Проверяет наличие бронирований для определённого товара, пользователя и статуса.
     *
     * @param itemId идентификатор товара
     * @param userId идентификатор пользователя
     * @param status статус бронирования
     * @param end    дата окончания периода проверки
     * @return true, если есть бронирования для данного товара, пользователя, статуса и даты окончания, иначе false
     */
    @Query("select new java.lang.Boolean(COUNT(b) > 0) from Booking b where (b.item.id = ?1 and b.status = ?3 and " +
            "b.end = ?4 or b.end < ?4) and b.booker.id = ?2")
    Boolean checkValidateBookingsFromItemAndStatus(Long itemId, Long userId, BookingStatus status, LocalDateTime end);

    /**
     * Находит все текущие бронирования пользователя, упорядоченные по времени начала.
     *
     * @param userId идентификатор пользователя
     * @param now    текущая дата и время
     * @return список текущих бронирований пользователя, отсортированный по времени начала
     */
    @Query("select b from Booking b where b.booker.id = ?1 and ?2 between b.start and b.end order by b.start DESC")
    List<Booking> findByBookerAllCurrentBookings(Long userId, LocalDateTime now);

    /**
     * Находит все текущие бронирования, принадлежащие определённому владельцу, упорядоченные по времени начала.
     *
     * @param userId идентификатор владельца
     * @param now    текущая дата и время
     * @return список текущих бронирований, принадлежащих указанному владельцу, отсортированный по времени начала
     */
    @Query("select b from Booking b where b.item.owner.id = ?1 and ?2 between b.start and b.end order by b.start DESC")
    List<Booking> findAllCurrentBookingsByOwner(Long userId, LocalDateTime now);

    @Query
            ("select b from Booking b " +
                    "where b.item.id = ?1 " +
                    "and ?2 between b.start and b.end")
    List<Booking> checkValidateBookings(
            Long itemId, LocalDateTime bookingDtoStartIsBeforeOrAfter);
}
