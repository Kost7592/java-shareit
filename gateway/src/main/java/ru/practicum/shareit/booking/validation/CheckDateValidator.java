package ru.practicum.shareit.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.time.LocalDateTime;

/**
 *  Класс реализует интерфейс ConstraintValidator для проверки валидности объекта типа BookingDtoRequest.
 */
public class CheckDateValidator implements ConstraintValidator<StartValid, BookingDtoRequest> {

    /**
     * Метод, для того чтобы задать дополнительные параметры валидации.
     * @param constraintAnnotation
     */
    @Override
    public void initialize(StartValid constraintAnnotation) {
    }

    /**
     * Метод проверяет валидность объекта типа BookingDtoRequest.
     * @param bookingDtoRequest объект, который нужно проверить на валидность;
     * @param constraintValidatorContext контекст валидации.
     * @return boolean.
     */
    @Override
    public boolean isValid(BookingDtoRequest bookingDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDtoRequest.getStart();
        LocalDateTime end = bookingDtoRequest.getEnd();
        Long itemId = bookingDtoRequest.getItemId();
        if (start == null || end == null || itemId == null) {
            return false;
        }
        if (!end.isAfter(start)) {
            return false;
        }
        return true;
    }
}
