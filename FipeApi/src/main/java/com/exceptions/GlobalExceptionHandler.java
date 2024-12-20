package com.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof VeiculosNotFoundException) {
            VeiculosNotFoundException veiculosException = (VeiculosNotFoundException) exception;
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Veículos com a marca '" + veiculosException.getMarca() + "' não encontrados.", 404))
                    .build();
        } else if (exception instanceof JsonProcessingExceptionCustom) {
            JsonProcessingExceptionCustom jsonException = (JsonProcessingExceptionCustom) exception;
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorMessage("Erro no processamento de JSON: " + jsonException.getMessage(), 500))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorMessage("Erro desconhecido", 500))
                .build();
    }
}