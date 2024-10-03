package ru.andryss.killers.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.andryss.killers.exception.InternalServerErrorException;
import ru.andryss.killers.model.ErrorObject;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@ResponseBody
@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class,
            MethodArgumentTypeMismatchException.class,
    })
    @ResponseStatus(BAD_REQUEST)
    ErrorObject handleBadRequest(Exception e) {
        return new ErrorObject().message(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    ErrorObject handleUnsupportedMediaType(Exception e) {
        return new ErrorObject().message(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorObject handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Object[] messageArguments = e.getDetailMessageArguments();
        if (messageArguments == null) {
            return new ErrorObject().message(e.getMessage());
        }
        return new ErrorObject().message((String) e.getDetailMessageArguments()[1]);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorObject handleNotFound(Exception e) {
        return new ErrorObject().message(e.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ErrorObject handleInternalServerError(Exception e) {
        return new ErrorObject().message(e.getMessage());
    }
}
