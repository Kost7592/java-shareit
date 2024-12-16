package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ItemServiceImplTest {
    LocalDateTime now = LocalDateTime.now();
    private final ItemService itemService;
    private final UserService userService;
    private final ItemServiceImpl itemServiceImpl;

    @Test
    void getItemWithBookingAndCommentTest() {
        User owner2 = User.builder()
                .id(2L)
                .name("owner1")
                .email("owner1@gmail.com")
                .build();

        User userForTest2 = User.builder()
                .id(1L)
                .name("userForTest2")
                .email("userForTest2@gmail.com")
                .build();

        Item item1 = Item.builder()
                .id(1L)
                .name("item1")
                .description("item1 description")
                .owner(owner2).build();

        Booking bookingFromBd = Booking.builder()
                .id(1L)
                .item(item1)
                .booker(userForTest2)
                .start(now.minusDays(10))
                .end(now.minusDays(5))
                .build();

        Item itemFromBd = Item.builder()
                .id(1L)
                .name("itemFromBd")
                .description("itemFromBd description")
                .owner(owner2)
                .available(true)
                .build();

        CommentDtoResponse commentDto = CommentDtoResponse.builder()
                .id(1L)
                .text("comment 1")
                .authorName("userForTest2")
                .created(now.minusDays(5))
                .build();

        Comment outputComment = Comment.builder()
                .id(1L)
                .author(userForTest2)
                .text("comment 1")
                .item(itemFromBd)
                .build();

        UserRepository userRepositoryJpa2 = mock(UserRepository.class);
        ItemRepository itemRepositoryJpa2 = mock(ItemRepository.class);
        CommentRepository commentRepository2 = mock(CommentRepository.class);

        when(userRepositoryJpa2.findById(any())).thenReturn(Optional.of(userForTest2));
        when(itemRepositoryJpa2.findById(any())).thenReturn(Optional.of(itemFromBd));
        when(commentRepository2.save(any())).thenReturn(outputComment);
    }

    @Test
    public void addItemTest() {
        UserDto userDto = new UserDto(1L,
                "name",
                "mail@gmail.com"
        );
        userService.createUser(userDto);

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@gmail.com")
                .build();

        UserDto userDtoResponse = UserMapper.toUserDto(user);

        ItemDto itemDtoRequest1 = ItemDto.builder()
                .id(1L)
                .name("name for item 1")
                .description("description for item 1")
                .available(true)
                .ownerId(user.getId())
                .build();
        Item item1 = Item.builder()
                .id(1L)
                .name("name for item 1")
                .description("description for item 1")
                .owner(user)
                .available(true)
                .build();
        ItemDto itemDto = ItemMapper.toItemDto(item1);

        assertNotNull(itemDto);
        assertEquals(itemDto.getId(), 1L);
        assertEquals(itemDto.getDescription(), itemDtoRequest1.getDescription());
        assertEquals(itemDto.getName(), itemDtoRequest1.getName());
        assertEquals(itemDto.getAvailable(), itemDtoRequest1.getAvailable());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> itemService.updateItem(user.getId(),
                itemDtoRequest1.getId(), itemDtoRequest1));
        assertEquals("Вещь с id 1 не найдена", ex.getMessage());
    }

    @Test
    public void itemDeleteTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .available(true)
                .ownerId(1L)
                .build();
        UserDto user = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();
        userService.createUser(user);
        itemService.createItem(user.getId(), itemDto);
        assertNotNull(itemService.getOwnerAllItems(1L));
        itemService.deleteItem(1L);
        assertEquals(0, itemService.getOwnerAllItems(1L).size());
    }

    @Test
    public void updateItemNameTest() {
        UserDto user = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .available(true)
                .ownerId(1L)
                .build();
        ItemDto itemForUpdateDto = ItemDto.builder()
                .id(1L)
                .name("updatedItem")
                .build();
        userService.createUser(user);
        itemService.createItem(1L, itemDto);
        itemService.updateItem(1L, 1L, itemForUpdateDto);
        assertEquals(itemForUpdateDto.getName(), itemService.getItemDto(1L, 1L).getName());
    }

    @Test
    public void updateItemDescriptionTest() {
        UserDto user = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .available(true)
                .ownerId(1L)
                .build();
        ItemDto itemForUpdateDto = ItemDto.builder()
                .id(1L)
                .description("updatedItem Description")
                .build();
        userService.createUser(user);
        itemService.createItem(1L, itemDto);
        itemService.updateItem(1L, 1L, itemForUpdateDto);
        assertEquals(itemForUpdateDto.getDescription(), itemService.getItemDto(1L, 1L).getDescription());
    }

    @Test
    public void updateItemAvailableTest() {
        UserDto user = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .available(true)
                .ownerId(1L)
                .build();
        ItemDto itemForUpdateDto = ItemDto.builder()
                .id(1L)
                .available(false)
                .build();
        userService.createUser(user);
        itemService.createItem(1L, itemDto);
        itemService.updateItem(1L, 1L, itemForUpdateDto);
        assertEquals(itemForUpdateDto.getAvailable(), itemService.getItemDto(1L, 1L).getAvailable());
    }

    @Test
    public void updateItemByNotOwner() {
        UserDto user = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .available(true)
                .ownerId(1L)
                .build();
        ItemDto itemForUpdateDto = ItemDto.builder()
                .id(1L)
                .available(false)
                .build();
        userService.createUser(user);
        itemService.createItem(1L, itemDto);
        assertThrows(ValidationException.class,
                () -> itemService.updateItem(999L, 1L, itemForUpdateDto));;
    }

    @Test
    public void addCommentAuthorNullThrowExceptionTest() {
        long authorId = 5L;
        long itemId = 3L;
        CommentDtoRequest commentDto = new CommentDtoRequest("Test comment");

        NotFoundException ex = assertThrows(NotFoundException.class, () -> itemService.addComment(itemId, authorId, commentDto));
        assertEquals("Вещь с id 3 не найдена", ex.getMessage());
    }
}
