package com.demo.hotel_management.viber;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.service.VacationService;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.Request;
import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.MessageDestination;
import com.viber.bot.api.ViberBot;
import com.viber.bot.message.TextMessage;
import com.viber.bot.profile.BotProfile;
import com.viber.bot.profile.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
public class SpringBot  implements ApplicationListener<ApplicationReadyEvent>{

    @Autowired
    private VacationService vacationService;

    @Autowired
    private ViberBot bot;

    @Autowired
    private ViberSignatureValidator signatureValidator;

    @Value("${application.viber-bot.webhook-url}")
    private String webhookUrl;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent appReadyEvent) {
        try {
            bot.setWebhook(webhookUrl).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        bot.onMessageReceived((event, message, response) -> response.send(message)); // echos everything back
//        bot.onConversationStarted(event -> Futures.immediateFuture(Optional.of( // send 'Hi UserName' when conversation is started
//                new TextMessage("Hi " + event.getUser().getName()))));
    }

    @PostMapping(value = "/viber", produces = "application/json")
    public String incoming(@RequestBody String json,
                           @RequestHeader("X-Viber-Content-Signature") String serverSideSignature)
            throws ExecutionException, InterruptedException, IOException {
        Preconditions.checkState(signatureValidator.isSignatureValid(serverSideSignature, json), "invalid signature");
        @Nullable InputStream response = bot.incoming(Request.fromJsonString(json)).get();
        return response != null ? CharStreams.toString(new InputStreamReader(response, Charsets.UTF_16)) : null;
    }

    @Scheduled(fixedDelay = 3000)
    public void todaysVacationNotification() {

        List<VacationDto> todaysVacationList = vacationService.getVacationsForTomorrow();
//        bot.sendMessage(new MessageDestination(UserProfile));
    }

    @Configuration
    public class BotConfiguration {

        @Value("${application.viber-bot.auth-token}")
        private String authToken;

        @Value("${application.viber-bot.name}")
        private String name;

        @Value("${application.viber-bot.avatar:@null}")
        private String avatar;

        @Bean
        ViberBot viberBot() {
            return new ViberBot(new BotProfile(name, avatar), authToken);
        }

        @Bean
        ViberSignatureValidator signatureValidator() {
            return new ViberSignatureValidator(authToken);
        }
    }
}