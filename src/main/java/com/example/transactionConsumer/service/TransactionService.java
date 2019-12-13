package com.example.transactionConsumer.service;

import com.example.transactionConsumer.domains.Order;
import com.example.transactionConsumer.util.ResponseDTO;
import com.google.gson.Gson;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class TransactionService {
    static final Gson GSON = new Gson();

    public ResponseDTO process(Order order) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Order is confirmed and Your Order Confirmation email sent to " + order.getEmail());
        responseDTO.setData(order);
        return responseDTO;
    }

//    public static void main(String[] args) {
//        new TransactionService().registerMessage(new Order());
//    }

    public ResponseDTO registerMessage(Order order) {
        try {
            SubscriptionClient subscription1Client = new SubscriptionClient(new ConnectionStringBuilder("<Put your connectipn String>", "<Put your Entry Path>"), ReceiveMode.PEEKLOCK);
            registerMessageHandlerOnClient(subscription1Client);
        } catch (Exception ex) {
        }
        return new ResponseDTO();
    }

    public void registerMessageHandlerOnClient(SubscriptionClient receiveClient) throws Exception {

        // register the RegisterMessageHandler callback
        IMessageHandler messageHandler = new IMessageHandler() {
            // callback invoked when the message handler loop has obtained a message
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
                // receives message is passed to callback
                if (message.getLabel() != null &&
                        message.getContentType() != null &&
                        message.getLabel().contentEquals("Scientist") &&
                        message.getContentType().contentEquals("application/json")) {

                    byte[] body = message.getBody();
                    Order scientist = GSON.fromJson(new String(body, UTF_8), Order.class);
                    System.out.printf("*****************************start****************");
                    System.out.printf(scientist.toString());
                    System.out.printf("*****************************end****************");

                }
                return receiveClient.completeAsync(message.getLockToken());
            }

            public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
                System.out.printf(exceptionPhase + "-" + throwable.getMessage());
            }
        };


        receiveClient.registerMessageHandler(
                messageHandler,
                // callback invoked when the message handler has an exception to report
                // 1 concurrent call, messages are auto-completed, auto-renew duration
                new MessageHandlerOptions(1, false, Duration.ofMinutes(1)));

    }
}
