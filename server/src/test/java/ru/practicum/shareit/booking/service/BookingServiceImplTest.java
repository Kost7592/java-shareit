package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toItemBookingInfoDto;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookingServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final BookingServiceImpl bookingServiceImpl;
    private UserDto testUser;
    private UserDto secondTestUser;
    private ItemDto itemDtoFromDB;
    private BookingDtoRequest bookItemRequestDto;
    private BookingDtoRequest secondBookItemRequestDto;
    User user;
    Item item;
    private ItemDto itemAvailableFalseDto;
    Item itemAvailableFalse;

    UserDto owner;
    UserDto booker;
    ItemDtoRequest itemDtoToCreate;
    BookingDtoRequest bookingToCreate;

    @BeforeEach
    public void setUp() {

        ItemDto itemDto = ItemDto.builder()
                .name("Item1")
                .description("Item1 description")
                .available(true)
                .build();

        UserDto userDto = UserDto.builder()
                .name("user1")
                .email("user1@gmail.com")
                .build();

        UserDto secondUserDto = UserDto.builder()
                .name("user2")
                .email("user2@gmail.com")
                .build();

        testUser = userService.createUser(userDto);
        user = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@gmail.com")
                .build();
        secondTestUser = userService.createUser(secondUserDto);
        itemDtoFromDB = itemService.createItem(testUser.getId(), itemDto);

        item = Item.builder()
                .id(1L)
                .name("Item1")
                .description("Item1 description")
                .available(true)
                .owner(user)
                .build();
        itemAvailableFalseDto = ItemDto.builder()
                .id(2L)
                .name("Item2")
                .description("Item2 description")
                .available(false)
                .requestId(user.getId())
                .build();

        itemAvailableFalse = Item.builder()
                .id(2L)
                .name("Item2")
                .description("Item2 description")
                .available(false)
                .owner(user)
                .build();


        bookItemRequestDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusNanos(1))
                .end(LocalDateTime.now().plusNanos(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        secondBookItemRequestDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusHours(5))
                .end(LocalDateTime.now().plusHours(10))
                .itemId(itemDtoFromDB.getId())
                .build();

        owner = new UserDto(null, "owner", "owner@email.com");
        booker = new UserDto(null, "booker", "booker@email.com");
        itemDtoToCreate = ItemDtoRequest.builder()
                .name("testItem")
                .description("testDescription")
                .available(true)
                .build();
        bookingToCreate = BookingDtoRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
    }

    @SneakyThrows
    @Test
    void checkRequestTest() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.createItem(user.getId(), itemAvailableFalseDto));
        assertEquals("Запрос не найден", ex.getMessage());
    }

    @SneakyThrows
    @Test
    void createBookingTest() {
        BookingDtoResponse addBooking = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);

        assertNotNull(addBooking);
        checkBookings(addBooking, bookItemRequestDto, secondTestUser, itemDtoFromDB, BookingStatus.WAITING);
    }

    @SneakyThrows
    @Test
    void updateBookingTest() {
        BookingDtoResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingDtoResponse approveBooking = bookingService.updateBooking(bookingDtoFromDB.getId(), testUser.getId(),
                true);

        assertNotNull(approveBooking);
        checkBookings(approveBooking, bookItemRequestDto, secondTestUser, itemDtoFromDB, BookingStatus.APPROVED);
    }

    @Test
    void updateBookingForStatusApprovedTest() {
        BookingDtoResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingDtoResponse waitingBooking = bookingService.updateBooking(bookingDtoFromDB.getId(), testUser.getId(),
                true);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.updateBooking(testUser.getId(), bookingDtoFromDB.getId(),
                        true));
        assertEquals("Данное бронирование уже внесено и имеет статус APPROVED", ex.getMessage());
    }

    @SneakyThrows
    @Test
    void getBookingByIdTest() {
        BookingDtoResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingDtoResponse getBooking = bookingService.getBooking(bookingDtoFromDB.getId(), secondTestUser.getId());

        assertNotNull(getBooking);
        checkBookings(getBooking, bookItemRequestDto, secondTestUser, itemDtoFromDB, BookingStatus.WAITING);
    }

    @Test
    void getBookingByIdTestException() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBooking(999L, 1L));
        assertEquals("Бронирование с id 999 не найдено", ex.getMessage());
    }

    @SneakyThrows
    @Test
    void getAllBookingsTest() {
        BookingDtoResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingDtoResponse bookingDtoFromDB2 = bookingService.addBooking(secondTestUser.getId(), secondBookItemRequestDto);
        List<BookingDtoResponse> bookingDtos = List.of(bookingDtoFromDB, bookingDtoFromDB2);
        List<BookingDtoResponse> bookings = bookingService.getAllBookingByUser("ALL",
                secondTestUser.getId());

        assertNotNull(bookings);
        assertEquals(bookings.size(), bookingDtos.size());

    }

    @Test
    void getAllBookingsExceptionTest() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingByUser("ALL",
                        3L));
        assertEquals("Пользователь с id 3 не найден", ex.getMessage());
    }

    @SneakyThrows
    @Test
    void getAllOwnerBookingsTest() {
        BookingDtoResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingDtoResponse bookingDtoFromDB2 = bookingService.addBooking(secondTestUser.getId(), secondBookItemRequestDto);
        List<BookingDtoResponse> bookingDtos = List.of(bookingDtoFromDB, bookingDtoFromDB2);
        List<BookingDtoResponse> bookings = bookingService
                .getAllBookingByOwner("ALL", testUser.getId());

        assertNotNull(bookings);
        assertEquals(bookings.size(), bookingDtos.size());
    }

    @SneakyThrows
    @Test
    void approveBookingWrongOwnerTest() {
        BookingDtoResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.updateBooking(bookingDtoFromDB.getId(), secondTestUser.getId(), true));
        assertEquals("Пользователь не является владельцем вещи", ex.getMessage());
    }

    @SneakyThrows
    @Test
    void getAllBookingsCurrentStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingDtoResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);
        List<BookingDtoResponse> currentBookings = bookingService.getAllBookingByUser("CURRENT",
                secondTestUser.getId());
        BookingDtoResponse currentBooking = currentBookings.getFirst();

        assertEquals(currentBookings.size(), bookingDtos.size());
    }

    @SneakyThrows
    @Test
    void getAllBookingsFutureStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingDtoResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        List<BookingDtoResponse> futureBookings = bookingService.getAllBookingByUser("FUTURE",
                secondTestUser.getId());
        BookingDtoResponse futureBooking = futureBookings.getFirst();

        assertEquals(futureBookings.size(), bookingDtos.size());
        assertEquals(futureBooking.getId(), firstBooking.getId());
    }

    @SneakyThrows
    @Test
    void getAllBookingsPastStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingDtoResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);
        List<BookingDtoResponse> pastBookings = bookingService.getAllBookingByUser("PAST",
                secondTestUser.getId());
        BookingDtoResponse pastBooking = pastBookings.getFirst();

        assertEquals(pastBookings.size(), bookingDtos.size());
        assertEquals(pastBooking.getId(), firstBooking.getId());
    }


    @SneakyThrows
    @Test
    void getAllOwnerBookingsFutureStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingDtoResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);
        List<BookingDtoResponse> futureBookings = bookingService.getAllBookingByOwner("FUTURE",
                testUser.getId());
        BookingDtoResponse futureBooking = futureBookings.getFirst();

        assertEquals(futureBookings.size(), bookingDtos.size());
        assertEquals(futureBooking.getId(), firstBooking.getId());
    }

    @SneakyThrows
    @Test
    void getAllOwnerBookingsPastStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingDtoResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);

        List<BookingDtoResponse> pastBookings = bookingService.getAllBookingByOwner("PAST",
                testUser.getId());
        BookingDtoResponse pastBooking = pastBookings.getFirst();

        assertEquals(pastBookings.size(), bookingDtos.size());
        assertEquals(pastBooking.getId(), firstBooking.getId());
    }

    @Test
    public void checkDatesNegativeTestCaseTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(itemDtoFromDB.getId())
                .build();

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingServiceImpl.validateBooking(bookingDto, item, user));
        assertEquals("Нельзя забронировать свою вещь", ex.getMessage());
    }

    @Test
    public void toItemBookingDtoResponsePositiveTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .item(Item.builder().id(1L).name("Hole").build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingDtoResponse itemBookingInfoDto = BookingMapper.toBookingDtoResponseMapper(booking);

        assertEquals(1L, itemBookingInfoDto.getId());
        assertEquals(2L, itemBookingInfoDto.getBooker().getId());
        assertEquals(1L, itemBookingInfoDto.getItem().getId());
        assertEquals("Hole", itemBookingInfoDto.getItem().getName());
        assertEquals(booking.getStart(), itemBookingInfoDto.getStart());
        assertEquals(booking.getEnd(), itemBookingInfoDto.getEnd());
    }

    @Test
    public void toItemBookingDtoResponseNegativeTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .item(Item.builder().id(1L).name("Hole").build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingDtoResponse itemBookingInfoDto = BookingMapper.toBookingDtoResponseMapper(booking);

        assertNotEquals(2L, itemBookingInfoDto.getId());
        assertNotEquals(1L, itemBookingInfoDto.getBooker().getId());
        assertNotEquals(2L, itemBookingInfoDto.getItem().getId());
        assertNotEquals("Hol", itemBookingInfoDto.getItem().getName());
        assertNotEquals(booking.getStart().plusDays(1), itemBookingInfoDto.getStart());
        assertNotEquals(booking.getEnd().minusHours(2), itemBookingInfoDto.getEnd());
    }

    private void checkBookings(BookingDtoResponse booking, BookingDtoRequest secondBooking,
                               UserDto user, ItemDto item, BookingStatus status) {
        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStatus(), equalTo(status));
        assertThat(booking.getStart(), equalTo(secondBooking.getStart()));
        assertThat(booking.getEnd(), equalTo(secondBooking.getEnd()));
        assertThat(booking.getBooker().getId(), equalTo(user.getId()));
        assertThat(booking.getItem().getId(), equalTo(item.getId()));
        assertThat(booking.getItem().getName(), equalTo(item.getName()));
    }

    @Test
    void bookerNotAvailableItem2Test() {
        UserDto createdBooker = userService.createUser(booker);
        BookingDtoRequest bookDto = new BookingDtoRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                2L
        );
        Exception exception = assertThrows(NotFoundException.class, ()
                -> bookingService.addBooking(createdBooker.getId(), bookDto));
        assertEquals("Вещь с id 2 не найдена", exception.getMessage());
    }

    @Test
    void addBookingItemAvailableFalseTest() {
        UserDto createdBooker = userService.createUser(booker);
        BookingDtoRequest bookDto = new BookingDtoRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                itemAvailableFalse.getId()
        );
        Exception exception = assertThrows(NotFoundException.class, ()
                -> bookingService.addBooking(createdBooker.getId(), bookDto));
        assertEquals("Вещь с id 2 не найдена", exception.getMessage());
    }

    @Test
    public void toItemBookingInfoDtoPositiveTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingItemDto itemBookingInfoDto = toItemBookingInfoDto(booking);

        assertEquals(1L, itemBookingInfoDto.getId());
        assertEquals(2L, itemBookingInfoDto.getBookerId());
        assertEquals(booking.getStart(), itemBookingInfoDto.getStart());
        assertEquals(booking.getEnd(), itemBookingInfoDto.getEnd());
    }

    @Test
    public void toItemBookingInfoDtoTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingItemDto itemBookingInfoDto = toItemBookingInfoDto(booking);

        assertNotEquals(2L, itemBookingInfoDto.getId());
        assertNotEquals(1L, itemBookingInfoDto.getBookerId());
        assertNotEquals(booking.getStart(), itemBookingInfoDto.getEnd());
        assertNotEquals(booking.getEnd(), itemBookingInfoDto.getStart());
    }

    @Test
    public void approveWithInvalidOwnerIdShouldThrowNotFoundExceptionTest() {
        long ownerId = 3L;
        long bookingId = 2L;
        boolean approved = true;

        Exception exception = assertThrows(NotFoundException.class, () -> bookingService.updateBooking(ownerId, bookingId, approved));

        assertEquals("Бронирование с id 3 не найдено", exception.getMessage());
    }

    @Test
    public void approveWithInvalidBookingIdShouldThrowNotFoundExceptionTest() {
        long ownerId = 1L;
        long bookingId = 4L;
        boolean approved = true;

        Exception exception = assertThrows(NotFoundException.class, () ->
                bookingService.updateBooking(ownerId, bookingId, approved));

        assertEquals("Бронирование с id 1 не найдено", exception.getMessage());
    }
}