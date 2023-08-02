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
                if ("Приют для собак".equals(text)) {
                    sendMessage(chatId,
                            "Это приют для собак. Здесь вы можете о часах работы и местонахождении приюта");
                }
                if ("Приют для кошек".equals(text)) {
                    sendMessage(chatId,
                            "Это приют для кошек. Здесь вы можете о часах работы и местонахождении приюта");
                }
                if ("Как взять животное из приюта".equals(text)) {
                    sendMessage(chatId,
                            "Необходимо придти к нам в офис.");
                }
                if ("Прислать отчет о питомце".equals(text)) {
                    sendMessage(chatId,
                            "Прикрепите все необходимые документы.");
                }
                if ("Позвать волонтера".equals(text)) {
                    sendMessage(chatId,
                            "С вами свяжутся через некоторое время.");
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"Приют для собак", "Приют для кошек"},
                new String[]{"Как взять животное из приюта", "Прислать отчет о питомце", "Позвать волонтера"})
                .oneTimeKeyboard(false)
                .resizeKeyboard(true)
                .selective(true);
        /*ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("/start"));
        replyKeyboardMarkup.oneTimeKeyboard(false);
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.selective(false);
        ReplyKeyboardMarkup replyKeyboardMarkup1 = new ReplyKeyboardMarkup(
                new KeyboardButton("Приют для собак"),
                new KeyboardButton("Приют для кошек"));
        replyKeyboardMarkup1.oneTimeKeyboard(false);
        replyKeyboardMarkup1.resizeKeyboard(true);
        replyKeyboardMarkup1.selective(false);
        ReplyKeyboardMarkup replyKeyboardMarkup2 = new ReplyKeyboardMarkup(
                new KeyboardButton("Как взять животное из приюта"),
                new KeyboardButton("Прислать отчет о питомце"),
                new KeyboardButton("Позвать волонтера"));
        Keyboard replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardMarkup2.oneTimeKeyboard(false);
        replyKeyboardMarkup2.resizeKeyboard(true);
        replyKeyboardMarkup2.selective(false);*/
        SendMessage sendMessage = new SendMessage(chatId, message)
                /*.replyMarkup(replyKeyboardMarkup)
                .replyMarkup(replyKeyboardRemove)
                .replyMarkup(replyKeyboardMarkup1)
                .replyMarkup(replyKeyboardMarkup2)*/
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }


}
