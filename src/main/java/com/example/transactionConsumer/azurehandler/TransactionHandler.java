package com.example.transactionConsumer.azurehandler;

import com.example.transactionConsumer.domains.Order;
import com.example.transactionConsumer.util.ResponseDTO;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;

import java.util.Optional;

public class TransactionHandler extends AzureSpringBootRequestHandler<Object, ResponseDTO> {

    @FunctionName("transactionProcessor")
    public ResponseDTO run(
            @HttpTrigger(name = "transactionRequest", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<Order>> request,
            ExecutionContext context) {
        return handleRequest(request.getBody().get(), context);
    }

}