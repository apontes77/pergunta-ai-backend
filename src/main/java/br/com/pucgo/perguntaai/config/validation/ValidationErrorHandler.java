package br.com.pucgo.perguntaai.config.validation;

import br.com.pucgo.perguntaai.exceptions.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationErrorHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<FormErrorDto> handle(MethodArgumentNotValidException exception) {
        List<FormErrorDto> formErrorDtos = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            FormErrorDto error = new FormErrorDto(e.getField(), message);
            formErrorDtos.add(error);
        });
        return formErrorDtos;
    }

    @ExceptionHandler(UserRegistrationException.class)
    public FormErrorDto handleUserRegistrationException(UserRegistrationException ex) {
        return new FormErrorDto("Algum problema na inserção de novo usuário", ex.getMessage());
    }
}
