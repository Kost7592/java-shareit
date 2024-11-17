package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentMapper для маппинга объектов Comment на объекты CommentDtoResponse, CommentDtoRequest и обратно.
 */
@UtilityClass
public class CommentMapper {
    /**
     * Метод toComment преобразует объект ItemDto в объект Item.
     *
     * @param commentDtoRequest объект типа commentDtoRequest, который нужно преобразовать в объект Comment;
     * @param item              объект комментируемой вещи;
     * @param author            объект автора комментария;
     * @return новый объект типа Item, содержащий данные из переданного объекта ItemDto.
     */
    public Comment toComment(CommentDtoRequest commentDtoRequest, Item item, User author) {
        Comment comment = Comment.builder()
                .text(commentDtoRequest.getText())
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
        return comment;
    }

    /**
     * Метод commentDtoList преобразует список объектов Comment в список CommentDtoResponse.
     *
     * @param commentList — исходный список комментариев, который будет преобразован;
     * @return список CommentDtoResponse, содержащий информацию о комментариях.
     */
    public List<CommentDtoResponse> commentDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(CommentMapper::toCommentDtoResponse)
                .collect(Collectors.toList());
    }

    /**
     * Метод toCommentDtoResponse преобразует объект Comment в CommentDtoResponse.
     *
     * @param comment — исходный комментарий, который будет преобразован.
     * @return новый объект CommentDtoResponse, содержащий информацию о комментарии.
     */
    public CommentDtoResponse toCommentDtoResponse(Comment comment) {
        return new CommentDtoResponse(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
    }
}
