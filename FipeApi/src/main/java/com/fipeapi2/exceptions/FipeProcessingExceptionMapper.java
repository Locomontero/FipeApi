package com.fipeapi2.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.WebApplicationException;

@Provider
public class FipeProcessingExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        // Personaliza a resposta HTTP com código 400 (BAD_REQUEST) e a mensagem de erro
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Erro ao processar os dados do veículo: " + exception.getMessage())
                .build();
    }
}
