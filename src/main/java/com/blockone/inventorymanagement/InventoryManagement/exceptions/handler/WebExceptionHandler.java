package com.blockone.inventorymanagement.InventoryManagement.exceptions.handler;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.BadParametersException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.DBException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.EntityNotFoundException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DBException.class)
    public void dbException(RuntimeException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public void entityNotFoundException(RuntimeException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(MalformedRequestException.class)
    public void malformedInputException(RuntimeException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(BadParametersException.class)
    public void badParameterException(RuntimeException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
    }


}
