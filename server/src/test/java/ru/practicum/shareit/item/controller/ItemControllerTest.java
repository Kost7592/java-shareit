package ru.practicum.shareit.item.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    ItemDto itemDto;
    ItemDtoRequest itemDtoRequest;
    ItemDtoResponse itemDtoResponse;
    ItemRequest itemRequest;
    Item item;
    ItemSearchOfTextDto itemSearchOfTextDto;
    ItemBookingDto itemWithBookingAndCommentsDto;
    User owner;
    User booker;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("owner")
                .email("owner@mall.ru")
                .build();

        booker = User.builder()
                .id(101L)
                .name("booker")
                .email("booker@email.ru")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Item")
                .requestor(booker)
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .available(true)
                .owner(owner)
                .request(itemRequest)
                .build();

        itemRequest.setItems(List.of(item));

        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        itemDtoRequest = ItemDtoRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        itemDtoResponse = ItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        itemWithBookingAndCommentsDto = ItemBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(null)
                .nextBooking(null)
                .comments(List.of())
                .build();

        itemSearchOfTextDto = ItemSearchOfTextDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        assertEquals(item.getId(), itemDtoRequest.getId());
        assertEquals(item.getName(), itemDtoRequest.getName());
        assertEquals(item.getDescription(), itemDtoRequest.getDescription());
        assertEquals(item.getAvailable(), itemDtoRequest.getAvailable());
    }

    @Test
    void testGetItemByIdForOwnerTest() throws Exception {
        when(itemService.getItemDto(anyLong(), anyLong()))
                .thenReturn(itemWithBookingAndCommentsDto);

        mockMvc.perform(get("/items/{id}", itemDtoRequest.getId())
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingAndCommentsDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemWithBookingAndCommentsDto.getDescription()), String.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingAndCommentsDto.getName()), String.class));
    }

    @Test
    void testGetAllTest() throws Exception {
        when(itemService.getOwnerAllItems(anyLong()))
                .thenReturn(List.<ItemDto>of(itemDto));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithBookingAndCommentsDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemWithBookingAndCommentsDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].name", is(itemWithBookingAndCommentsDto.getName()), String.class));
    }

    @Test
    void testSearchItemsByTextTest() throws Exception {
        String text = "one item";
        given(itemService.getItemsBySearch(text))
                .willReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemSearchOfTextDto))));

        when(itemService.getItemsBySearch("items not found"))
                .thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", "items not found")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    void testAddTest() throws Exception {
        when(itemService.createItem(anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class));
    }

    @Test
    void testUpdateWhenAllAreOkAndReturnUpdatedItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemDtoRequest.getId())
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class));
    }

    @Test
    void addCommentToItemTest() throws Exception {
        CommentDtoRequest commentDto = CommentDtoRequest.builder()
                .text("comment 1")
                .build();
        CommentDtoResponse commentDtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .text("comment 1")
                .authorName("name user")
                .created(LocalDateTime.now().minusSeconds(5))
                .build();
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDtoRequest.class)))
                .thenReturn(commentDtoResponse);

        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(commentDtoResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDtoResponse.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentDtoResponse.getAuthorName()), String.class));
    }
}
