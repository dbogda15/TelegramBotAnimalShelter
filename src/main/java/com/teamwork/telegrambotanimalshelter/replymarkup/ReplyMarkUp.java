package com.teamwork.telegrambotanimalshelter.replymarkup;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.teamwork.telegrambotanimalshelter.constant.Constants;
import com.teamwork.telegrambotanimalshelter.constant.Keyboard;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReplyMarkUp {
    private final TelegramBot telegramBot;

    public void sendMessageStart(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.DOG_SHELTER),
                new KeyboardButton(Keyboard.CAT_SHELTER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER),
                new KeyboardButton(Keyboard.SEND_REPORT_FORM));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, Constants.WELCOME);
    }

    public void sendMessageStage(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.INFORMATION_ABOUT_SHELTER),
                new KeyboardButton(Keyboard.SEND_REPORT_FORM));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите:");
    }

    public void sendMenuCat(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.WORK_SCHEDULE),
                new KeyboardButton(Keyboard.LIST_OF_CATS),
                new KeyboardButton(Keyboard.ABOUT_THE_SHELTER));
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.TB_GUIDELINES),
                new KeyboardButton(Keyboard.CONTACT_DETAILS),
                new KeyboardButton(Keyboard.CONTACT_DETAILS_OF_THE_GUARD));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о кошачьем приюте");
    }

    public void sendMenuDog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.WORK_SCHEDULE),
                new KeyboardButton(Keyboard.LIST_OF_DOGS),
                new KeyboardButton(Keyboard.ABOUT_THE_SHELTER));
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.TB_GUIDELINES),
                new KeyboardButton(Keyboard.CONTACT_DETAILS),
                new KeyboardButton(Keyboard.CONTACT_DETAILS_OF_THE_GUARD));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о собачьем приюте");
    }

    public void returnResponseReplyKeyboardMarkup(ReplyKeyboardMarkup replyKeyboardMarkup, Long chatId, String text) {
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.oneTimeKeyboard(false);
        replyKeyboardMarkup.selective(false);

        SendMessage request = new SendMessage(chatId, text)
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);
        telegramBot.execute(request);
    }
}
