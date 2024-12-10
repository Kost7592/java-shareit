package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    UserRepository userRepositoryJpa;
    @Autowired
    CommentRepository commentRepository;
    User user;
    User user1;
    Item item3;
    Comment comment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user")
                .email("user@mail.ru")
                .build();
        userRepositoryJpa.save(user);
        user1 = User.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();
        userRepositoryJpa.save(user1);
        itemRepository.save(Item.builder()
                .name("item1")
                .description("item1 description")
                .available(true)
                .owner(user)
                .build());
        itemRepository.save(Item.builder()
                .name("item2")
                .description("item2 description")
                .available(true)
                .owner(user)
                .build());
        ItemRequest itemRequest = ItemRequest.builder()
                .created(LocalDateTime.now().minusDays(1))
                .requestor(user1)
                .description("test description")
                .build();
        itemRequestRepository.save(itemRequest);
        item3 = Item.builder()
                .name("item3")
                .description("item3 description")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();
        itemRepository.save(item3);
        itemRequest.setItems(List.of(item3));
        comment = Comment.builder()
                .text("coll")
                .author(itemRequest.getRequestor())
                .item(item3)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    @Test
    void findAllByOwnerOrderByIdTest() {
        List<Comment> commentList = commentRepository.findByItemIn(List.of(item3), Sort.by(DESC, "created"));

        assertNotNull(commentList);
        assertEquals(1, commentList.size());
        assertEquals(item3, commentList.getFirst().getItem());
        assertEquals(comment.getAuthor(), commentList.getFirst().getAuthor());
        assertEquals(comment.getText(), commentList.getFirst().getText());
        assertEquals(comment.getCreated(), commentList.getFirst().getCreated());
    }
}
