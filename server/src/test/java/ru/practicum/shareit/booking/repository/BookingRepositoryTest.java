package ru.practicum.shareit.booking.repository;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    protected TestEntityManager entityManager;

    public static User makeUser(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public static Item makeItem(Long id, String name, String description, User user, boolean available) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setOwner(user);
        item.setAvailable(available);
        return item;
    }

    public static Booking makeBooking(
            Long id,
            LocalDateTime start,
            LocalDateTime end,
            Item item,
            User user,
            BookingStatus status
    ) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(status);
        return booking;
    }

    @Test
    public void shouldFindNoBookingsIfRepositoryIsEmptyTest() {
        Iterable<Booking> bookings = bookingRepository.findAll();

        assertThat(bookings).isEmpty();
    }

    @Test
    public void shouldStoreBookingTest() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Booking booking = bookingRepository.save(makeBooking(null,
                start,
                end,
                item,
                booker,
                BookingStatus.WAITING));

        assertThat(booking)
                .hasFieldOrPropertyWithValue("start", start)
                .hasFieldOrPropertyWithValue("end", end)
                .hasFieldOrPropertyWithValue("status", BookingStatus.WAITING)
                .hasFieldOrProperty("item")
                .hasFieldOrProperty("booker");
        assertThat(booking.getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
    }

    @Test
    public void shouldFindAllBookingsByOwnerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item 1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.REJECTED));

        List<Booking> listBookings = bookingRepository.findByItem_OwnerId(owner.getId());

        assertThat(listBookings)
                .hasSize(2)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.get(0).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
        assertThat(listBookings.get(1).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
    }

    @Test
    public void shouldCurrentByOwnerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item 1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(1),
                now.plusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        Pageable pageable = PageRequest.of(0, 20);
        List<Booking> listBookings = bookingRepository.findAllCurrentBookingsByOwner(owner.getId(),
                LocalDateTime.now());

        assertThat(listBookings)
                .hasSize(1)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.getFirst().getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
    }

    @Test
    public void shouldFindPastByOwnerTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner1 = entityManager.persist(makeUser(null,
                "Owner1",
                "owner1@gmail.com"));
        User owner2 = entityManager.persist(makeUser(null,
                "Owner2",
                "owner2@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner1,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item2 description",
                owner2,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                owner1,
                BookingStatus.APPROVED));
        entityManager.persist(makeBooking(null,
                now.minusDays(3),
                now.minusDays(2),
                item2,
                owner2,
                BookingStatus.WAITING));

        List<Booking> listBookings = bookingRepository.findByItem_OwnerIdAndEndBeforeAndStatus(owner1.getId(),
                LocalDateTime.now(), BookingStatus.APPROVED);

        assertThat(listBookings)
                .hasSize(1)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.getFirst().getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
    }

    @Test
    public void shouldFindFutureByOwnerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        List<Booking> listBookings = bookingRepository.findByItem_OwnerIdAndStartAfter(owner.getId(),
                LocalDateTime.now());

        assertThat(listBookings)
                .hasSize(1)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.getFirst().getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
    }

    @Test
    public void shouldFindWaitingByOwnerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        Pageable pageable = PageRequest.of(0, 20);
        List<Booking> listBookings = bookingRepository.findByItem_OwnerIdAndStatus(owner.getId(),
                BookingStatus.WAITING);

        assertThat(listBookings)
                .hasSize(2)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.get(0).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
        ObjectAssert<Item> itemObjectAssert = assertThat(listBookings.get(1).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
    }

    @Test
    public void shouldFindRejectedByOwnerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.CANCELED));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.REJECTED));

        Pageable pageable = PageRequest.of(0, 20);
        List<Booking> listBookings = bookingRepository.findByItem_OwnerIdAndStatusOrStatus(owner.getId(),
                BookingStatus.REJECTED, BookingStatus.CANCELED);

        assertThat(listBookings)
                .hasSize(2)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.get(0).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
        assertThat(listBookings.get(1).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
    }

    @Test
    public void shouldFindAllBookingsByBookerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.APPROVED));

        Pageable pageable = PageRequest.of(0, 20);
        List<Booking> listBookings = bookingRepository.findByBookerId(booker.getId(), Sort.by(DESC, "start"));

        assertThat(listBookings)
                .hasSize(2)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.get(0).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
        assertThat(listBookings.get(1).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
    }

    @Test
    public void shouldCurrentByBookerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(1),
                now.plusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        Pageable pageable = PageRequest.of(0, 20);
        List<Booking> listBookings = bookingRepository.findByBookerAllCurrentBookings(booker.getId(),
                LocalDateTime.now());

        assertThat(listBookings)
                .hasSize(1)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.getFirst().getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
    }

    @Test
    public void shouldFindPastByBookerTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.APPROVED));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        List<Booking> listBookings = bookingRepository.findByBookerIdAndEndBeforeAndStatus(booker.getId(),
                LocalDateTime.now(), BookingStatus.APPROVED);

        assertThat(listBookings)
                .hasSize(1)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.getFirst().getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
    }

    @Test
    public void shouldFindFutureByBookerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        List<Booking> listBookings = bookingRepository.findByBookerIdAndStartAfter(booker.getId(),
                LocalDateTime.now());

        assertThat(listBookings)
                .hasSize(1)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.getFirst().getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
    }

    @Test
    public void shouldFindWaitingByBookerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        List<Booking> listBookings = bookingRepository.findByBookerIdAndStatus(booker.getId(),
                BookingStatus.WAITING);

        assertThat(listBookings)
                .hasSize(2)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.get(0).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
        assertThat(listBookings.get(1).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
    }

    @Test
    public void shouldFindRejectedByBookerIdTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.minusDays(1),
                item1,
                booker,
                BookingStatus.CANCELED));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.REJECTED));

        List<Booking> listBookings = bookingRepository.findByBookerIdAndStatusOrStatus(booker.getId(),
                BookingStatus.REJECTED, BookingStatus.CANCELED);

        assertThat(listBookings)
                .hasSize(2)
                .element(0)
                .hasFieldOrProperty("item");
        assertThat(listBookings.get(0).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item1 name");
        assertThat(listBookings.get(1).getItem())
                .isInstanceOf(Item.class)
                .hasFieldOrPropertyWithValue("name", "Item2 name");
    }

    @Test
    public void shouldValidateBookingTest() {
        LocalDateTime now = LocalDateTime.now();
        User owner = entityManager.persist(makeUser(null,
                "Owner",
                "owner@gmail.com"));
        User booker = entityManager.persist(makeUser(null,
                "Booker",
                "booker@gmail.com"));
        Item item1 = entityManager.persist(makeItem(null,
                "Item1 name",
                "Item1 description",
                owner,
                true));
        Item item2 = entityManager.persist(makeItem(null,
                "Item2 name",
                "Item 2 description",
                owner,
                true));
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest
                .builder()
                .itemId(owner.getId())
                .start(now.minusDays(2))
                .end(now.minusDays(1))
                .build();
        entityManager.persist(makeBooking(null,
                now.minusDays(2),
                now.plusDays(1),
                item1,
                booker,
                BookingStatus.WAITING));
        entityManager.persist(makeBooking(null,
                now.plusDays(1),
                now.plusDays(2),
                item2,
                booker,
                BookingStatus.WAITING));

        try {
            bookingRepository.checkValidateBookings(item1.getId(), bookingDtoRequest.getStart());
        } catch (BadRequestException ex) {
            assertThatExceptionOfType(BadRequestException.class);
        }
    }
}