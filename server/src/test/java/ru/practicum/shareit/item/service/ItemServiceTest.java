package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;

    private final User owner = User.builder()
            .id(1L)
            .name("User")
            .email("User@gmail.com")
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .description("All needed thing")
            .name("1st Item")
            .available(true)
            .owner(owner)
            .build();

    @Test
    void addItemValidAddTest() {
        Long ownerId = 1L;
        ItemDto itemDto = new ItemDto(null,
                "1st Item",
                "All needed thing",
                true,
                1L,
                null);
        when(userRepository.existsById(ownerId)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto result = itemService.createItem(ownerId, itemDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1st Item", result.getName());
        assertEquals("All needed thing", result.getDescription());
        assertTrue(result.getAvailable());
        assertNull(result.getRequestId());
    }

    @Test
    public void getAllItemsWithBlankTextShouldReturnEmptyListTest() {
        String text = "";
        Pageable page = PageRequest.of(0, 10);
        Page<Item> actualResult = itemRepository.findByNameOrDescription(text, page);

        assertNull(actualResult);
    }

    @Test
    void findItemByIdTest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Item result = itemRepository.findById(1L).orElse(null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1st Item", result.getName());
        assertEquals("All needed thing", result.getDescription());
        assertEquals(owner, result.getOwner());
        assertTrue(result.getAvailable());
        assertNull(result.getRequest());
    }

    @Test
    public void updateItemValidTest() {
        Long ownerId = 1L;
        Long itemId = 2L;
        Long itemRequestId = 3L;
        ItemDto itemDto = new ItemDto(1L,
                "test",
                "description",
                true,
                1L,
                3L);
        ItemDto updateItemDto = new ItemDto(1L,
                "test_update",
                "description_update",
                true,
                1L,
                3L);
        ItemRequest itemRequest = ItemRequest.builder()
                .id(3L)
                .description("description")
                .created(LocalDateTime.now())
                .build();

        User user = new User();
        user.setId(ownerId);
        Item oldItem = new Item();
        oldItem.setId(itemId);
        oldItem.setOwner(user);
        oldItem.setName("old name");
        oldItem.setDescription("old description");
        oldItem.setAvailable(false);
        itemRequest.setRequestor(user);
        oldItem.setRequest(itemRequest);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(3L)).thenReturn(Optional.of(ItemRequest.builder().build()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(oldItem));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArguments()[0]);

        ItemDto result = itemService.updateItem(ownerId, itemId, itemDto);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("test", result.getName());
        assertEquals("description", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(3L, result.getRequestId());

        ItemDto resultUpdated = itemService.updateItem(ownerId, itemId, updateItemDto);

        assertNotNull(resultUpdated);
        assertEquals(itemId, result.getId());
        assertEquals("test_update", resultUpdated.getName());
        assertEquals("description_update", resultUpdated.getDescription());
        assertTrue(resultUpdated.getAvailable());
        assertEquals(3L, resultUpdated.getRequestId());
    }

    @Test
    public void getItemTest() {
        Long itemId = 1L;
        Long userId = 2L;
        User owner = new User(1L, "Owner", "Owner@mail.ru");

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(new Item(itemId, "Item",
                        "Description",
                        true,
                        owner,
                        null)));
        when(commentRepository.findByItemIn(anyList(),
                any(Sort.class))).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItemInAndStatus(anyList(),
                eq(BookingStatus.APPROVED), any(Sort.class))).thenReturn(Collections.emptyList());

        ItemBookingDto result = itemService.getItemDto(userId, itemId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("Item", result.getName());
        assertEquals("Description", result.getDescription());
        assertTrue(result.getAvailable());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    public void updateInvalidOwnerIdThrowsExceptionTest() {
        Long ownerId = 1L;
        Long itemId = 2L;
        ItemDto itemDto = new ItemDto(1L,
                "test1",
                "description1",
                true,
                1L,
                null);
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> itemService.updateItem(ownerId, itemId, itemDto));
        assertEquals("Вещь с id 2 не найдена", ex.getMessage());
    }

    @Test
    public void failAddItemInvalidParamsTest() {
        User owner = new User(1L, "test@gmail.com", "Tester");

        ItemDto newItem = new ItemDto(null,
                null,
                null,
                null,
                null,
                null);
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.createItem(owner.getId(), newItem));

        ItemDto newItemWithoutName = new ItemDto(null,
                null,
                null,
                true,
                null,
                null);

        assertThrows(NotFoundException.class, () -> itemService.createItem(owner.getId(), newItemWithoutName));
        assertNotNull(exception);

        ItemDto newItemWithoutDescription = new ItemDto(null,
                "testName",
                null,
                true,
                null,
                null);
        assertThrows(NotFoundException.class, () -> itemService.createItem(owner.getId(), newItemWithoutDescription));
    }

    @Test
    public void addItemWithoutOwnerIdTest() {
        ItemDto itemDto = new ItemDto(null,
                "Item1",
                "new item1",
                true,
                null,
                null);

        assertThrows(NotFoundException.class, () -> {
            itemService.createItem(9L, itemDto);
        });
    }

    @Test
    public void shouldMapToCommentDtoListTest() {
        User owner = new User(1L,
                "Owner",
                "Owner@gmail.com");
        Item item = new Item(1L,
                "Item",
                "Item description",
                true,
                owner,
                null);

        User author = new User(3L,
                "test@gmail.com",
                "Tester");
        Comment comment1 = new Comment(1L, "text1", item, author, LocalDateTime.now());
        Comment comment2 = new Comment(1L, "text2", item, author, LocalDateTime.now());
        List<Comment> commentList = List.of(comment1, comment2);
        List<CommentDtoResponse> commentDto = CommentMapper.commentDtoList(commentList);

        assertNotNull(commentDto);
        assertEquals(commentDto.get(0).getText(), comment1.getText());
        assertEquals(commentDto.get(1).getText(), comment2.getText());
    }

    @Test
    public void addBookingAndCommentTest() {
        User owner = new User(1L,
                "Test",
                "test@gmail.com");
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .owner(owner)
                .build();
        List<Comment> comments = List.of(
                new Comment(1L, "Coll", item, owner, LocalDateTime.now()),
                new Comment(2L, "Fine", item, owner, LocalDateTime.now())
        );
        List<Booking> bookings = List.of(
                new Booking(1L,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        item,
                        owner,
                        BookingStatus.APPROVED),
                new Booking(2L,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(2),
                        item,
                        owner,
                        BookingStatus.APPROVED)
        );
        LocalDateTime now = LocalDateTime.now();

        ItemBookingDto result = itemService.addBookingAndComment(item, 1L, comments, bookings, now);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertNull(result.getAvailable());
        assertNotNull(result.getLastBooking());
        assertNotNull(result.getNextBooking());
        assertNotNull(result.getComments());
        assertEquals(comments.size(), result.getComments().size());
        assertEquals(comments.get(0).getId(), result.getComments().get(0).getId());
        assertEquals(comments.get(1).getId(), result.getComments().get(1).getId());
    }

    @Test
    public void getItemInvalidThrowsExceptionTest() {
        Long itemId = 1L;
        Long userId = 2L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> itemService.getItemDto(itemId, userId));
        assertEquals("Вещь с id:2 не найдена", ex.getMessage());
    }

    @Test
    public void toItemDtoListEmptyListTest() {
        List<Item> itemList = Collections.emptyList();
        List<ItemForItemRequestResponseDto> expectedResult = Collections.emptyList();
        List<ItemForItemRequestResponseDto> actualResult = ItemMapper.toItemForItemRequestsResponseDto(itemList);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void searchItemsByTextWhenTextIsBlankTest() {
        List<ItemDto> itemDtoList = itemService.getItemsBySearch("");

        assertEquals(List.of(), itemDtoList);
    }

    @Test
    public void addCommentAuthorNullThrowExceptionTest() {
        long authorId = 5L;
        long itemId = 3L;
        CommentDtoRequest commentDto = new CommentDtoRequest("Test comment");

        NotFoundException ex = assertThrows(NotFoundException.class, () -> itemService.addComment(itemId, authorId, commentDto));
        assertEquals("Вещь с id 3 не найдена", ex.getMessage());
    }

    @Test
    public void addCommentItemNotFoundThrowsExceptionTest() {
        Long authorId = 1L;
        long itemId = 1L;
        CommentDtoRequest commentDto = new CommentDtoRequest();
        User user = new User();
        user.setId(authorId);
        when(userRepository.findById(authorId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            itemService.addComment(itemId, authorId, commentDto);
        });
        assertEquals("Вещь с id 1 не найдена", ex.getMessage());
    }

    @Test
    void updateItemIfNotValidOwnerTest() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(1L,
                "test_update",
                "description_update",
                true,
                1L,
                3L);
        itemDto.setName("Some name");
        assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(itemId, userId, itemDto);
        }, "Вещь с ID 1 не зарегистрирован!");
    }

    @Test
    public void addCommentSuccessTest() {
        long itemId = 1L;
        long userId = 2L;
        String text = "Test comment";
        CommentDtoRequest request = new CommentDtoRequest(text);

        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(bookingRepository.checkValidateBookingsFromItemAndStatus(
                anyLong(), anyLong(), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArguments()[0]);

        CommentDtoResponse response = itemService.addComment(itemId, userId, request);

        assertNotNull(response);
        assertEquals(request.getText(), response.getText());

    }
}
