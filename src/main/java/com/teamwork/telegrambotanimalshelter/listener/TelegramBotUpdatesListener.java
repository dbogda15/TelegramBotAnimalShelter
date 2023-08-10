package com.teamwork.telegrambotanimalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.teamwork.telegrambotanimalshelter.constant.Constants;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.replymarkup.ReplyMarkUp;
import com.teamwork.telegrambotanimalshelter.repository.OwnerRepository;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import com.teamwork.telegrambotanimalshelter.constant.Keyboard;
import com.teamwork.telegrambotanimalshelter.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;



@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final OwnerService ownerService;
    private final OwnerRepository ownerRepository;
    private final AnimalService animalService;
    private final ShelterService shelterService;
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
            list.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                logger.info("Processing update: {}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                Chat chat = message.chat();
                String text = message.text();
                if(!ownerRepository.existsOwnerByChatId(chatId)){
                    ownerService.create(new Owner(chatId, chat.firstName()));
                }
                Owner owner = ownerRepository.getOwnerByChatId(chatId);
                if (message.chat().id() != null) {
                    switch (text) {
                        case "/start", "К выбору приютов" -> {
                            logger.info("Запустили бота/выбрали приют - ID:{}", chatId);
                            replyMarkup.sendMessageStart(chatId);
                        }
                        case Keyboard.CAT_SHELTER -> sendMenuStage(AnimalType.CAT, chatId);
                        case Keyboard.DOG_SHELTER -> sendMenuStage(AnimalType.DOG, chatId);
                        case Keyboard.INFORMATION_ABOUT_SHELTER-> {
                            AnimalType type = owner.getOwnerType();
                            if (type.equals(AnimalType.CAT)){
                                replyMarkup.sendMenuCat(chatId);
                            }
                            if (type.equals(AnimalType.DOG)){
                                replyMarkup.sendMenuDog(chatId);
                            }
                        }
                        case Keyboard.MAIN_MENU -> {
                            owner.setOwnerType(null);
                            ownerService.update(owner);
                            replyMarkup.sendMessageStart(chatId);
                        }
                        case Keyboard.CONTACT_DETAILS_OF_THE_GUARD -> {
                            if (owner.getOwnerType().equals(AnimalType.DOG)){
                                sendMessage(chatId, shelterService.getByShelterType(AnimalType.DOG).getSecurity());
                            }
                            if (owner.getOwnerType().equals(AnimalType.CAT)){
                                sendMessage(chatId, shelterService.getByShelterType(AnimalType.CAT).getSecurity());
                            }
                        }
                        case Keyboard.CONTACT_DETAILS -> {
                            sendMessage(chatId, "Пожалуйста, введите свой номер телефона в формате +79171234123");
                        }
                        case Keyboard.ABOUT_THE_SHELTER -> {
                            AnimalType type = owner.getOwnerType();
                            if(type.equals(AnimalType.DOG)){
                                sendMessage(chatId, Constants.DOG_SHELTER_INFO);
                            }
                            if(type.equals(AnimalType.CAT)){
                                sendMessage(chatId, Constants.CAT_SHELTER_INFO);
                            }
                        }
                        case Keyboard.WORK_SCHEDULE -> {
                            AnimalType type = owner.getOwnerType();
                            if (type.equals(AnimalType.DOG)){
                                sendMessage(chatId, shelterService.getByShelterType(type).getTimetable());
                            }
                            if (type.equals(AnimalType.CAT)){
                                sendMessage(chatId, shelterService.getByShelterType(type).getTimetable());
                            }
                        }



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
    private void sendMenuStage(AnimalType shelterType, Long chatId) {
        logger.info("Вызвано меню выбора приюта - ID:{}", chatId);
        ReplyMarkUp replyMarkup = new ReplyMarkUp(telegramBot);
        Owner owner = ownerService.getByChatId(chatId);
        owner.setOwnerType(shelterType);
        ownerService.update(owner);
        replyMarkup.sendMessageStage(chatId);
    }


}
