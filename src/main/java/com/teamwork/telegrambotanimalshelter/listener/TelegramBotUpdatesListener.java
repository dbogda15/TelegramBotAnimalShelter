package com.teamwork.telegrambotanimalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;




@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

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
                if (message.chat().id() != null) {
                    switch (message.text()) {
                        case "/start":
                            sendMessage(chatId,
                                    "Здравствуйте, здесь вы сможете узнать о приюте,как его забрать и ухаживать за ним.");
                            break;
                        case "Приют для собак":
                            sendMessageShelter(chatId,
                                    "Это приют для собак. Здесь вы можете о часах работы и местонахождении приюта.");
                            break;
                        case "Приют для кошек":
                            sendMessageShelter(chatId,
                                    "Это приют для собак. Здесь вы можете о часах работы и местонахождении кошек.");
                            break;
                        case "Как взять животное из приюта":
                            sendMessageInfo(chatId,
                                    "Необходимо придти к нам в офис.");
                            break;
                        case "Прислать отчет о питомце":
                            sendMessageInfo(chatId,
                                    "Прикрепите все необходимые документы.");
                            break;
                        case "Позвать волонтера":
                            sendMessageInfo(chatId,
                                    "С вами свяжутся через некоторое время.");
                            return;

                    }
//               sendMessage(chatId,
//                            " Здравствуйте, здесь вы сможете узнать о приюте,как его забрать и ухаживать за ним. ");
//                }
//                if ("Приют для собак".equals(text)) {
//                    sendMessageShelter(chatId,
//                            "Это приют для собак. Здесь вы можете о часах работы и местонахождении приюта");
//                }
//                if ("Приют для кошек".equals(text)) {
//                    sendMessageShelter(chatId,
//                            "Это приют для кошек. Здесь вы можете о часах работы и местонахождении приюта");
//                }
//                if ("Как взять животное из приюта".equals(text)) {
//                    sendMessageInfo(chatId,
//                            "Необходимо придти к нам в офис.");
//                }
//                if ("Прислать отчет о питомце".equals(text)) {
//                    sendMessageInfo(chatId,
//                            "Прикрепите все необходимые документы.");
//                }
//                if ("Позвать волонтера".equals(text)) {
//                    sendMessageInfo(chatId,
//                            "С вами свяжутся через некоторое время.");
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {

        Keyboard replyKeyboardRemove = new ReplyKeyboardRemove();

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("/start"));
        replyKeyboardMarkup.oneTimeKeyboard(true);
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.selective(true);

        SendMessage sendMessage = new SendMessage(chatId, message)
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(false);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void sendMessageShelter(Long chatId, String message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Приют для собак"),
                new KeyboardButton("Приют для кошек"));
        replyKeyboardMarkup.oneTimeKeyboard(true);
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.selective(false);


        SendMessage sendMessageShelter = new SendMessage(chatId, message)
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBot.execute(sendMessageShelter);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void sendMessageInfo(Long chatId, String message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Как взять животное из приюта"),
                new KeyboardButton("Прислать отчет о питомце"),
                new KeyboardButton("Позвать волонтера"));
        replyKeyboardMarkup.oneTimeKeyboard(true);
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.selective(false);
        SendMessage sendMessageInfo = new SendMessage(chatId, message)
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);

        SendResponse sendResponse = telegramBot.execute(sendMessageInfo);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

}
