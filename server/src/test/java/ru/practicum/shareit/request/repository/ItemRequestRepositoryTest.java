package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    UserRepository userRepository;
    User user;
    User user1;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();
        user1 = User.builder()
                .name("user2")
                .email("user2@mail.ru")
                .build();
        userRepository.save(user);
        userRepository.save(user1);
        itemRepository.save(Item.builder()
                .name("item1")
                .description("item 1")
                .available(true)
                .owner(user)
                .build());
        itemRepository.save(Item.builder()
                .name("Item2")
                .description("Item2 description")
                .available(true)
                .owner(user)
                .build());
        itemRequestRepository.save(ItemRequest.builder()
                .description("Item2 description")
                .requestor(user1)
                .created(LocalDateTime.now())
                .build());
    }

    @Test
    void findAllByNotRequesterIdTest() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequestorIdIsNot(user.getId(), PageRequest.of(0, 2)).getContent();

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
    }

    @Test
    void findAllTest() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAll(PageRequest.of(0, 2)).getContent();

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
    }

    @Test
    public void findItemRequestsByUserIdTest() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findByRequestorId(user1.getId());

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
    }
}
