package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class
UserServiceImplTest {
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    UserDto userDto1;
    User user1;
    UserDto userDto2;
    User user2;
    User userNull;
    UserDto userDtoNull;
    User userAllFieldsNull;

    @BeforeEach
    void setUp() {
    userService = new UserServiceImpl(userRepository);
        userDto1 = UserDto.builder()
                .name("userDto1")
                .email("userDto1@mail.ru")
                .build();
        user1 = User.builder().id(userDto1.getId()).name(userDto1.getName()).email(userDto1.getEmail()).build();

        userDto2 = UserDto.builder()
                .id(2L)
                .name("userDto2")
                .email("userDto2@mail.ru")
                .build();
        user2 = User.builder().id(userDto2.getId()).name(userDto2.getName()).email(userDto2.getEmail()).build();

        userAllFieldsNull = new User();

        userNull = null;
        userDtoNull = null;
    }

    @Test
    @Transactional
    void createUserWhenAllIsOkTest() {
        UserDto savedUser = userService.createUser(userDto1);

        assertNotNull(savedUser.getId());
        assertEquals(userDto1.getName(), savedUser.getName());
        assertEquals(userDto1.getEmail(), savedUser.getEmail());
    }

    @Test
    void getUserByIdWhenAllIsOkTest() {
        UserDto savedUser = userService.createUser(userDto1);

        User user = userService.getUserById(savedUser.getId());

        assertNotNull(user.getId());
        assertEquals(user.getName(), userDto1.getName());
        assertEquals(user.getEmail(), userDto1.getEmail());
    }

    @Test
    void getUserByIdWhenUserNotFoundInDbReturnTest() {
        UserDto savedUser = userService.createUser(userDto1);

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(9000L));
    }

    @SneakyThrows
    @Test
    void getAllUsersTest() {
        List<UserDto> userDtoList = List.of(userDto1, userDto2);

        userService.createUser(userDto1);
        userService.createUser(userDto2);


        Collection<User> result = userService.getAllUsers();

        assertEquals(userDtoList.size(), result.size());
        for (UserDto user : userDtoList) {
            assertThat(result, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(user.getName())),
                    hasProperty("email", equalTo(user.getEmail()))
            )));
        }
    }

    @SneakyThrows
    @Test
    void addUserTest() {
        userService.createUser(userDto1);

        Collection<User> users = userService.getAllUsers();
        Long id = users.stream()
                .filter(u -> u.getEmail().equals(userDto1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);

        User userDtoFromDb = userService.getUserById(id);

        assertEquals(1, users.size());
        assertEquals(userDto1.getName(), userDtoFromDb.getName());
        assertEquals(userDto1.getEmail(), userDtoFromDb.getEmail());
    }

    @SneakyThrows
    @Test
    void updateInStorageWhenAllIsOkAndNameIsNullReturnUpdatedUserTest() {
        UserDto createdUser = userService.createUser(userDto1);

        Collection<User> beforeUpdateUsers = userService.getAllUsers();
        Long id = beforeUpdateUsers.stream()
                .filter(u -> u.getEmail().equals(userDto1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, createdUser.getId());

        User userDtoFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userDtoFromDbBeforeUpdate.getName(), userDto1.getName());
        assertEquals(userDtoFromDbBeforeUpdate.getEmail(), userDto1.getEmail());

        userService.updateUser(userDto2, createdUser.getId());

        User userDtoFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userDtoFromDbBeforeUpdate.getId(), userDtoFromDbAfterUpdate.getId());
        assertEquals(userDtoFromDbAfterUpdate.getName(), userDto2.getName());
        assertEquals(userDtoFromDbAfterUpdate.getEmail(), userDto2.getEmail());
    }

    @Test
    void updateInStorageWhenAllIsOkAndEmailIsNullReturnUpdatedUserTest() {
        UserDto createdUser = userService.createUser(userDto1);

        Collection<User> beforeUpdateUsers = userService.getAllUsers();
        Long id = beforeUpdateUsers.stream()
                .filter(u -> u.getEmail().equals(userDto1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, createdUser.getId());

        User userDtoFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userDtoFromDbBeforeUpdate.getName(), userDto1.getName());
        assertEquals(userDtoFromDbBeforeUpdate.getEmail(), userDto1.getEmail());

        userService.updateUser(userDto2, createdUser.getId());

        User userDtoFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userDtoFromDbBeforeUpdate.getId(), userDtoFromDbAfterUpdate.getId());
        assertEquals(userDtoFromDbAfterUpdate.getName(), userDto2.getName());
        assertEquals(userDtoFromDbAfterUpdate.getEmail(), userDto2.getEmail());
    }

    @Test
    void updateInStorageWhenAllIsOkReturnUpdatedUserTest() {
        UserDto createdUser = userService.createUser(userDto1);

        Collection<User> beforeUpdateUsers = userService.getAllUsers();
        Long id = beforeUpdateUsers.stream()
                .filter(u -> u.getEmail().equals(userDto1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, createdUser.getId());

        User userDtoFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userDtoFromDbBeforeUpdate.getName(), userDto1.getName());
        assertEquals(userDtoFromDbBeforeUpdate.getEmail(), userDto1.getEmail());

        userService.updateUser(userDto2, createdUser.getId());

        User userDtoFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userDtoFromDbBeforeUpdate.getId(), userDtoFromDbAfterUpdate.getId());
        assertEquals(userDtoFromDbAfterUpdate.getName(), userDto2.getName());
        assertEquals(userDtoFromDbAfterUpdate.getEmail(), userDto2.getEmail());
    }

    @Test
    void updateInStorageWhenUserNotFoundReturnNotFoundRecordInBDTest() {
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                userService.updateUser(userDto1,55L));
        assertEquals("Пользователь с id: " +
                55 + " не найден", ex.getMessage());
    }

    @Test
    void removeFromStorageTest() {
        UserDto savedUser = userService.createUser(userDto1);
        Collection<User> beforeDelete = userService.getAllUsers();

        assertEquals(1, beforeDelete.size());

        userService.deleteUserById(savedUser.getId());
        Collection<User> afterDelete = userService.getAllUsers();

        assertEquals(0, afterDelete.size());
    }
}
