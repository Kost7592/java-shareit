package ru.practicum.shareit.request.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequestResponseDto addItemRequest(long userId, ItemRequestDto itemRequestDto) {
        User user = checkUser(userId);
        ItemRequest itemRequest = ItemRequestDtoMapper.toItemRequest(itemRequestDto, user);
        ItemRequest addRequests = requestRepository.save(itemRequest);
        List<Item> items = itemRepository.findAllByRequestId(addRequests.getId());
        addRequests.setItems(items);
        return ItemRequestDtoMapper.toItemRequestResponseDto(addRequests);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestResponseDto> getItemRequestsByUserId(long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequests = requestRepository.findByRequestorId(userId);
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequest.setItems(items);
        }
        return ItemRequestDtoMapper.toItemRequestsResponseDto(itemRequests);
    }

    @Override
    public List<ItemRequestResponseDto> getAllItemRequests(long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " +
                    userId + " не зарегистрирован!");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from / size, size, sort);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequestorIdIsNot(userId, pageable).getContent();
        addItems(itemRequests);
        return ItemRequestDtoMapper.toItemRequestsResponseDto(itemRequests);
    }

    @Override
    public ItemRequestResponseDto getItemRequest(long requestId, long userId) {
        checkUser(userId);
        ItemRequest itemRequest = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос c ID " + requestId + " не найден"));
        List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
        itemRequest.setItems(items);
        return ItemRequestDtoMapper.toItemRequestResponseDto(itemRequest);
    }

    /**
     * Метод addItems принимает список itemRequests типа List<ItemRequest> и выполняет следующие действия:
     * 1. Создаёт список requestId, содержащий идентификаторы запросов из списка itemRequests.
     * 2. Создаёт пустую карту itemsByRequest типа Map<Long, List<Item>>, где ключ — это идентификатор запроса (Long),
     * а значение — список элементов (List<Item>) для этого запроса.
     * 3. Проходит по каждому элементу item типа Item из репозитория itemRepository, который соответствует одному из
     * идентификаторов запросов в списке requestId. Для каждого такого элемента item метод computeIfAbsent добавляет
     * новый список ArrayList<> в качестве значения на карте itemsByRequest, если ключа с таким идентификатором ещё нет.
     * В противном случае метод просто возвращает существующее значение. Затем элемент item добавляется в этот список.
     * 4. Перебирает каждый элемент itemRequest из списка itemRequests и устанавливает его свойство items, равное
     * значению из карты itemsByRequest для соответствующего идентификатора запроса или пустому списку
     * Collections.emptyList(), если такого ключа на карте нет.
     */
    private void addItems(List<ItemRequest> itemRequests) {
        List<Long> requestId = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        Map<Long, List<Item>> itemsByRequest = new HashMap<>();
        for (Item item : itemRepository.findByRequestIdIn(requestId)) {
            itemsByRequest.computeIfAbsent(item.getRequest().getId(), k -> new ArrayList<>()).add(item);
        }
        itemRequests.forEach(itemRequest -> {
            List<Item> items = itemsByRequest.getOrDefault(itemRequest.getId(), Collections.emptyList());
            itemRequest.setItems(items);
        });
    }

    /**
     * Метод проверяет наличие пользователя по переданному идентификатору (userId) в репозитории userRepository.
     *
     * @param userId идентификатор проверяемого пользователя
     */
    private User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " +
                        userId + " не найден"));
    }
}
