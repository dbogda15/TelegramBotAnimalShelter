package com.teamwork.telegrambotanimalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private TelegramBot telegramBot;

    private final OwnerService ownerService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, OwnerService ownerService) {
        this.telegramBot = telegramBot;
        this.ownerService = ownerService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод для обработки Updates
     * @param list
     * @return
     */
    @Override
    public int process(List<Update> list) {
        try {
            list.forEach(update -> {
                logger.info("Processing update: {}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();
                if ("/start".equals(text)) {
                    sendMessage(chatId,
                            " Здравствуйте, здесь вы сможете узнать о приюте,как его забрать и ухаживать за ним. ");
                }
                if ("/dogShelterInfo".equals(text)) {
                    sendMessage(chatId,
                            "Это приют для собак. Здесь вы можете о часах работы и местонахождении приюта");
                }
                if ("/catShelterInfo".equals(text)) {
                    sendMessage(chatId,
                            "Это приют для кошек. Здесь вы можете о часах работы и местонахождении приюта");
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId,message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
}
