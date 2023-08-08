package com.teamwork.telegrambotanimalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.replymarkup.ReplyMarkUp;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import com.teamwork.telegrambotanimalshelter.constant.Keyboard;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.teamwork.telegrambotanimalshelter.constant.Constants.SHELTER_INFO;


@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private Owner owner;

    private OwnerService ownerService;
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
        ReplyMarkUp replyMarkup = new ReplyMarkUp(telegramBot);
        try {
            list.forEach(update -> {
                logger.info("Processing update: {}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();
                if (message.chat().id() != null) {
                    switch (text) {
                        case "/start", "К выбору приютов" -> {
                            logger.info("Запустили бота/выбрали приют - ID:{}", chatId);
                            replyMarkup.sendMessageStart(chatId);
                        }
                        case Keyboard.CAT_SHELTER, Keyboard.DOG_SHELTER -> replyMarkup.sendMessageStage(chatId);
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, message));
        if (!sendResponse.isOk()) {
            logger.error(sendResponse.description());
        }
    }


}
