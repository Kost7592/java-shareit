package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.validation.Update;
import ru.practicum.shareit.validation.Create;

import java.util.Objects;

/**
 *  Класс представляет собой DTO (объект передачи данных) для пользователя (users) в систему
 *  Он содержит следующие поля:
 *  - id - идентификатор пользователя.
 *  - name - имя пользователя
 *  - email - почтовый адрес пользователя
 */
@Data
@NonNull
@AllArgsConstructor
@Builder
public class UserDtoRequest {
    private final Long id;
    @NotBlank(groups = {Create.class})
    private final String name;
    @NotEmpty(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private final String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoRequest userDtoRequest = (UserDtoRequest) o;
        return Objects.equals(name, userDtoRequest.name) && Objects.equals(email, userDtoRequest.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
