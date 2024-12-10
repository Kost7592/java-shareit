package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс ItemMapper для маппинга объектов Item на объекты ItemDto и обратно.
 */
@UtilityClass
public class ItemMapper {
    /**
     * Метод преобразует объект Item в объект ItemDto.
     *
     * @param item объект типа Item, который нужно преобразовать.
     * @return новый объект типа ItemDto, содержащий данные из переданного объекта Item.
     */
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .build();
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }

    /**
     * Метод преобразует объект ItemDto в объект Item.
     *
     * @param itemDto объект типа ItemDto, который нужно преобразовать.
     * @param user    объект владельца, используется при создании нового объекта Item.
     * @return новый объект типа Item, содержащий данные из переданного объекта ItemDto.
     */
    public static Item toItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        itemDto.setRequestId(itemDto.getRequestId());
        return item;
    }

    /**
     * Метод toItemForBookingMapper преобразует объект Item и связанные с ним данные в объект ItemBookingDto.
     *
     * @param item        — исходный объект Item, который будет использоваться для создания объекта ItemBookingDto.
     * @param lastBooking — информация о последнем бронировании этого товара.
     * @param nextBooking — информация о следующем бронировании этого товара.
     * @param comments    — список комментариев к товару.
     * @return новый объект ItemBookingDto, содержащий информацию о товаре, его доступности, а также данные о последних
     * двух бронированиях и комментариях.
     */
    public static ItemBookingDto toItemForBookingMapper(Item item, BookingItemDto lastBooking,
                                                        BookingItemDto nextBooking, List<CommentDtoResponse> comments) {
        return new ItemBookingDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments);
    }

    public List<ItemForItemRequestResponseDto> toItemForItemRequestsResponseDto(List<Item> item) {
        return item.stream().map(ItemMapper::toItemForItemRequestResponseDto).collect(Collectors.toList());
    }

    public ItemForItemRequestResponseDto toItemForItemRequestResponseDto(Item item) {
        ItemForItemRequestResponseDto itemForItemRequestResponseDto = ItemForItemRequestResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        if (item.getRequest() != null) {
            itemForItemRequestResponseDto.setRequestId(item.getRequest().getId());
        }
        return itemForItemRequestResponseDto;
    }
}
