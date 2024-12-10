package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
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
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Класс ItemServiceImpl реализация интерфейса ItemService для работы с вещами (items).
 */
@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDto itemDtoRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " +
                userId + " не найден"));
        ItemRequest requester;
        Item item = ItemMapper.toItem(itemDtoRequest, user);
        if (itemDtoRequest.getRequestId() != null) {
            requester = itemRequestRepository.findById(itemDtoRequest.getRequestId()).orElseThrow(() ->
                    new NotFoundException("Запрос не найден"));
            item.setRequest(requester);
            requester.setItems(List.of(item));
        }
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto updatedItemDto) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id " +
                itemId + " не найдена"));
        long ownerId = oldItem.getOwner().getId();
        if (userId != ownerId) {
            throw new ValidationException("У пользователя нет доступа к вещи");
        }
        return ItemMapper.toItemDto(updateItemFields(oldItem, updatedItemDto));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemBookingDto getItemDto(Long ownerId, Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещь с id:" +
                id + " не найдена"));
        return fillBookingInfo(List.of(item), ownerId).get(0);
    }

    @Transactional
    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getOwnerAllItems(Long ownerId) {
        List<Item> ownerItems = itemRepository.findByOwnerId(ownerId);
        return ownerItems.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> searchedItems = itemRepository.getItemsBySearch(text);
        return searchedItems.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDtoResponse addComment(long itemId, long userId, CommentDtoRequest commentDtoRequest) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id " + itemId +
                " не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " +
                userId + " не найден"));
        Boolean checkValidate = bookingRepository.checkValidateBookingsFromItemAndStatus(itemId, userId,
                BookingStatus.APPROVED, LocalDateTime.now());
        if (!checkValidate) {
            throw new BadRequestException("Неверные параметры");
        }
        Comment comment = CommentMapper.toComment(commentDtoRequest, item, user);
        return CommentMapper.toCommentDtoResponse(commentRepository.save(comment));
    }

    /**
     * Метод updateItemFields обновляет поля вещи Item на основе данных из объекта ItemDto.
     *
     * @param item    — исходная вещь, который будет обновлён.
     * @param ItemDto — объект с новыми данными для обновления.
     * @return обновлённая вещь Item.
     */
    private Item updateItemFields(Item item, ItemDto ItemDto) {
        if (ItemDto.getName() != null) {
            item.setName(ItemDto.getName());
        }
        if (ItemDto.getDescription() != null) {
            item.setDescription(ItemDto.getDescription());
        }
        if (ItemDto.getAvailable() != null) {
            item.setAvailable(ItemDto.getAvailable());
        }
        return item;
    }

    /**
     * Метод fillBookingInfo заполняет информацию о бронированиях и комментариях для списка вещей.
     *
     * @param items  — список вещей, для которых будет заполнена информация.
     * @param userId — идентификатор пользователя, который выполняет запрос.
     * @return список объектов ItemBookingDto с информацией о бронировании и комментариях.
     */
    private List<ItemBookingDto> fillBookingInfo(List<Item> items, Long userId) {
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(
                        items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookings = bookingRepository.findByItemInAndStatus(
                        items, BookingStatus.APPROVED, Sort.by(DESC, "start"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        LocalDateTime now = LocalDateTime.now();
        return items.stream().map(item -> addBookingAndComment(item, userId, comments.getOrDefault(item, List.of()),
                        bookings.getOrDefault(item, List.of()), now))
                .collect(toList());
    }

    /**
     * Метод для добавления бронирования и комментария к вещи.
     *
     * @param item     — вещь, к которой добавляется бронирование и комментарий.
     * @param userId   — идентификатор пользователя, который выполняет бронирование.
     * @param comments — список комментариев, которые будут добавлены к бронированию.
     * @param bookings — список существующих бронирований вещи.
     * @param now      — текущая дата и время, используемые для создания записи о бронировании.
     * @return результат добавления бронирования и комментариев.
     */
    public ItemBookingDto addBookingAndComment(Item item, Long userId, List<Comment> comments,
                                               List<Booking> bookings, LocalDateTime now) {
        if (item.getOwner().getId().longValue() != userId.longValue()) {
            return ItemMapper.toItemForBookingMapper(item, null, null,
                    CommentMapper.commentDtoList(comments));
        }
        Booking lastBooking = bookings.stream()
                .filter(b -> !b.getStart().isAfter(now))
                .findFirst()
                .orElse(null);
        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .reduce((a, b) -> a.getStart().isBefore(b.getStart()) ? a : b)
                .orElse(null);
        BookingItemDto lastBookingDto = lastBooking != null ? BookingMapper.toItemBookingInfoDto(lastBooking) : null;
        BookingItemDto nextBookingDto = nextBooking != null ? BookingMapper.toItemBookingInfoDto(nextBooking) : null;
        return ItemMapper.toItemForBookingMapper(item, lastBookingDto, nextBookingDto,
                CommentMapper.commentDtoList(comments));
    }
}
