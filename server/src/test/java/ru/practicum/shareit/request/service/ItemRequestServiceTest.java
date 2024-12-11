package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserForItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;
    ItemRequest itemRequest1;
    UserDto userDtoRequest;
    UserDto userDtoRequest1;
    User owner1;
    User requester;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    ItemRequestDto requestDto;
    ItemRequestResponseDto requestResponseDto;
    TypedQuery<ItemRequest> query;

    @BeforeEach
    void setStart() {
        now = LocalDateTime.now();
        nowPlus10min = now.plusMinutes(10);
        nowPlus10hours = now.plusHours(10);

        userDtoRequest = UserDto.builder()
                .name("name userDto1")
                .email("userDto1@mail.ru")
                .build();
        userDtoRequest1 = UserDto.builder()
                .name("name userDto2")
                .email("userDto2@mail.ru")
                .build();

        owner1 = User.builder()
                .id(userDtoRequest.getId())
                .name(userDtoRequest.getName())
                .email(userDtoRequest.getEmail())
                .build();

        requester = User.builder()
                .id(userDtoRequest1.getId())
                .name(userDtoRequest1.getName())
                .email(userDtoRequest1.getEmail())
                .build();

        itemRequest1 = ItemRequest.builder()
                .description("description for request 1")
                .requestor(requester)
                .created(now)
                .build();

        item1 = Item.builder()
                .name("name for item 1")
                .description("description for item 1")
                .owner(owner1)
                .available(true)
                .build();

        requestDto = ItemRequestDto.builder()
                .description(item1.getDescription())
                .build();

        requestResponseDto = ItemRequestResponseDto.builder()
                .description(item1.getDescription())
                .requestor(new UserForItemRequestDto(requester.getId(), requester.getName()))
                .created(now)
                .items(List.of())
                .build();
    }

    @SneakyThrows
    @Test
    void addItemRequestTest() {
        UserDto savedOwnerDto1 = userService.createUser(userDtoRequest);
        query = em.createQuery("Select ir from ItemRequest ir", ItemRequest.class);
        List<ItemRequest> requestsOfEmpty = query.getResultList();

        assertEquals(0, requestsOfEmpty.size());

        ItemRequestResponseDto saveItemRequest =
                itemRequestService.addItemRequest(savedOwnerDto1.getId(), requestDto);
        List<ItemRequest> afterSave = query.getResultList();

        assertNotNull(afterSave);
        assertEquals(1, afterSave.size());
        assertEquals(saveItemRequest.getId(), afterSave.getFirst().getId());
        assertEquals(saveItemRequest.getCreated(), afterSave.getFirst().getCreated());
        assertEquals(saveItemRequest.getDescription(), afterSave.getFirst().getDescription());
    }

    @Test
    void addItemRequestWhenRequesterIdIsNullReturnNotFoundRecordInBDTest() {
        long requesterId = 9991L;
        assertThrows(NotFoundException.class,
                () -> itemRequestService.addItemRequest(requesterId, requestDto));
    }

    @SneakyThrows
    @Test
    void getItemRequestsByUserIdTest() {
        UserDto savedUserDto = userService.createUser(userDtoRequest1);
        ItemRequestResponseDto savedItemRequest =
                itemRequestService.addItemRequest(savedUserDto.getId(), requestDto);

        query = em.createQuery("Select ir from ItemRequest ir", ItemRequest.class);

        List<ItemRequestResponseDto> itemsFromDb =
                itemRequestService.getItemRequestsByUserId(savedUserDto.getId());

        assertNotNull(itemsFromDb);
        assertEquals(1, itemsFromDb.size());
        assertEquals(savedItemRequest.getId(), itemsFromDb.getFirst().getId());
        assertEquals(savedItemRequest.getRequestor().getId(), itemsFromDb.getFirst().getRequestor().getId());
        assertEquals(savedItemRequest.getRequestor().getName(), itemsFromDb.getFirst().getRequestor().getName());
        assertEquals(savedItemRequest.getCreated(), itemsFromDb.getFirst().getCreated());
        assertEquals(requestResponseDto.getDescription(), itemsFromDb.getFirst().getDescription());
    }

    @SneakyThrows
    @Test
    void getAllItemRequestsTest() {
        UserDto saveRequesterDto = userService.createUser(userDtoRequest1);
        UserDto saveOwnerDto = userService.createUser(userDtoRequest);

        ItemRequestResponseDto savedItemRequest =
                itemRequestService.addItemRequest(saveRequesterDto.getId(), requestDto);

        query = em.createQuery("Select ir from ItemRequest ir where ir.requestor.id <> :userId", ItemRequest.class);
        List<ItemRequest> itemRequestList = query.setParameter("userId", saveOwnerDto.getId())
                .getResultList();

        List<ItemRequestResponseDto> emptyItemsFromDbForRequester =
                itemRequestService.getAllItemRequests(saveRequesterDto.getId(), 0, 5);

        assertEquals(0, emptyItemsFromDbForRequester.size());

        List<ItemRequestResponseDto> oneItemFromDbForOwner =
                itemRequestService.getAllItemRequests(saveOwnerDto.getId(), 0, 1);

        assertNotNull(oneItemFromDbForOwner);
        assertEquals(savedItemRequest.getId(), oneItemFromDbForOwner.getFirst().getId());
        assertEquals(savedItemRequest.getDescription(), oneItemFromDbForOwner.getFirst().getDescription());
        assertEquals(savedItemRequest.getRequestor().getId(), oneItemFromDbForOwner.getFirst().getRequestor().getId());
        assertEquals(savedItemRequest.getRequestor().getName(), oneItemFromDbForOwner.getFirst().getRequestor().getName());
        assertEquals(List.of(), oneItemFromDbForOwner.getFirst().getItems());
        assertEquals(savedItemRequest.getCreated(), oneItemFromDbForOwner.getFirst().getCreated());
    }

    @SneakyThrows
    @Test
    void getItemRequestTest() {
        UserDto savedRequesterDto = userService.createUser(userDtoRequest1);
        UserDto user = userService.createUser(UserDto.builder().name("user").email("user@mail.ru").build());

        ItemRequestResponseDto savedItRequest =
                itemRequestService.addItemRequest(savedRequesterDto.getId(), requestDto);

        ItemRequestResponseDto itRequestDtoFromDbObserver =
                itemRequestService.getItemRequest(savedItRequest.getId(), user.getId());

        assertNotNull(itRequestDtoFromDbObserver);
        assertEquals(savedItRequest.getId(), itRequestDtoFromDbObserver.getId());
        assertEquals(savedItRequest.getCreated(), itRequestDtoFromDbObserver.getCreated());
        assertEquals(savedItRequest.getDescription(), itRequestDtoFromDbObserver.getDescription());
        assertEquals(savedItRequest.getRequestor().getId(), itRequestDtoFromDbObserver.getRequestor().getId());
    }
}