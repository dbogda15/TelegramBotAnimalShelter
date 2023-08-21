package com.teamwork.telegrambotanimalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.teamwork.telegrambotanimalshelter.constant.Constants;
import com.teamwork.telegrambotanimalshelter.constant.Keyboard;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.replymarkup.ReplyMarkUp;
import com.teamwork.telegrambotanimalshelter.repository.AnimalRepository;
import com.teamwork.telegrambotanimalshelter.repository.OwnerRepository;
import com.teamwork.telegrambotanimalshelter.repository.ShelterRepository;
import com.teamwork.telegrambotanimalshelter.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final OwnerService ownerService;
    private final OwnerRepository ownerRepository;
    private final ShelterService shelterService;
    private final ReportService reportService;
    private final TrialPeriodService trialPeriodService;
    private final AnimalService animalService;
    private final ShelterRepository shelterRepository;
    private final AnimalRepository animalRepository;

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
                String username = message.chat().username();

                Owner owner = ownerRepository.getOwnerByChatId(chatId);
                if (message.photo() != null) {
                    getReport(message);
                    return;
                }
                AnimalType type = owner.getOwnerType();

                if(!ownerRepository.existsOwnerByChatId(chatId)){
                    ownerService.create(new Owner(chatId, chat.firstName()));
                }

                Pattern pattern = Pattern.compile("(\\d+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    getContact(chatId, text);
                    return;
                }

                if (message.chat().id() != null) {
                    switch (text) {
                        case "/start", "К выбору приютов" -> {
                            logger.info("Запустили бота/выбрали приют - ID:{}", chatId);
                            replyMarkup.sendMessageStart(chatId);
                        }
                        case Keyboard.CAT_SHELTER -> sendMenuStage(AnimalType.CAT, chatId);
                        case Keyboard.DOG_SHELTER -> sendMenuStage(AnimalType.DOG, chatId);
                        case Keyboard.INFORMATION_ABOUT_SHELTER-> {
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
                                sendMessage(chatId, Constants.GUARDS_INFO_DOG);
                            }
                            if (owner.getOwnerType().equals(AnimalType.CAT)){
                                sendMessage(chatId, Constants.GUARDS_INFO_CAT);
                            }
                        }
                        case Keyboard.CONTACT_DETAILS -> {
                            sendMessage(chatId, "Пожалуйста, введите свой номер телефона в формате 89171234123");
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
                            if (type.equals(AnimalType.DOG)){
                                sendMessage(chatId, shelterService.getByShelterType(type).getTimetable());
                            }
                            if (type.equals(AnimalType.CAT)){
                                sendMessage(chatId, shelterService.getByShelterType(type).getTimetable());
                            }
                        }
                        case Keyboard.SEND_REPORT_FORM -> {
                            logger.info("Отправили форму отчета - ID:{}", chatId);
                            sendReportExample(chatId, type);
                        }
                        case Keyboard.LIST_OF_CATS -> {
                            logger.info("Список кошек - ID:{}", chatId);
                            List<Animal> catList = shelterService.getByShelterType(AnimalType.CAT)
                                    .getAnimalList()
                                    .stream()
                                    .filter(animal -> animal.getOwner() == null)
                                    .toList();
                            if (catList.isEmpty()) {
                                sendMessage(chatId,"Кошек нет");
                                return;
                            }
                            sendMessage(chatId,getStringFromList(catList));
                        }
                        case Keyboard.LIST_OF_DOGS -> {
                            logger.info("Список собак - ID:{}", chatId);
                            List<Animal> dogList = shelterService.getByShelterType(AnimalType.DOG)
                                    .getAnimalList()
                                    .stream()
                                    .filter(animal -> animal.getOwner() == null)
                                    .toList();
                            if (dogList.isEmpty()) {
                                sendMessage(chatId,"Собак нет");
                                return;
                            }
                            sendMessage(chatId,getStringFromList(dogList));
                        }
                        case Keyboard.CALL_A_VOLUNTEER -> {
                            AnimalType type = owner.getOwnerType();
                            logger.info("Позвали волонтёра - ID: {}", chatId);
                            sendMessage(chatId, "Мы направили ваш запрос волонтеру, скоро он с вами свяжется!");

                            switch (type) {
                                case CAT -> sendMessage(Constants.VOLUNTEER_1_ID, "Напиши этому человеку: @" + username);
                                case DOG -> sendMessage(Constants.VOLUNTEER_2_ID, "Напиши этому человеку: @" + username);
                                default -> sendMessage(chatId, "К сожалению, мы не можем с вами связаться, напишите волонтеру самостоятельно. Спасибо! "
                                            + Constants.VOLUNTEER_INVITE);
                            }
                        }
                        case Keyboard.FAQ -> {
                            logger.info("Часто задаваемые вопросы - ID:{}", chatId);
                            if (type.equals(AnimalType.CAT)) {
                                replyMarkup.menuCat(chatId);
                            } else if(type.equals(AnimalType.DOG)) {
                                replyMarkup.menuDog(chatId);
                            }
                        }
                        case Keyboard.BACK_TO_ALL_ABOUT_CATS -> {
                            logger.info("Все о кошках - ID:{}", chatId);
                            replyMarkup.menuCat(chatId);
                        }
                        case Keyboard.BACK_TO_ALL_ABOUT_DOGS -> {
                            logger.info("Все о собаках - ID:{}", chatId);
                            replyMarkup.menuDog(chatId);
                        }
                        case Keyboard.RULES_FOR_DATING_A_CAT, Keyboard.RULES_FOR_DATING_A_DOG -> {
                            logger.info("Правила знакомства - ID:{}", chatId);
                            sendMessage(chatId, Constants.ANIMAL_DATING_RULES);
                        }
                        case Keyboard.CAT_CARRIAGE, Keyboard.DOG_CARRIAGE -> {
                            logger.info("Перевозка - ID:{}", chatId);
                            sendMessage(chatId, Constants.TRANSPORTATION_OF_THE_ANIMAL);
                        }
                        case Keyboard.REQUIRED_DOCUMENTS -> {
                            logger.info("Необходимые документы - ID:{}", chatId);
                            sendMessage(chatId, Constants.REQUIRED_DOCUMENTS);
                        }
                        case Keyboard.LIST_OF_REASONS -> {
                            logger.info("Список причин для отказа выдачи питомца - ID:{}", chatId);
                            sendMessage(chatId, Constants.LIST_OF_REASON_FOR_DENY);
                        }
                        case Keyboard.RECOMMENDATIONS_FOR_DOGS -> {
                            logger.info("Рекомендации для собак - ID:{}", chatId);
                            replyMarkup.rulesForDogs(chatId);
                        }
                        case Keyboard.RECOMMENDATIONS_FOR_CATS -> {
                            logger.info("Рекомендации для кошек - ID:{}", chatId);
                            replyMarkup.rulesForCats(chatId);
                        }
                        case Keyboard.PUPPY_SETUP, Keyboard.KITTEN_SETUP -> {
                            logger.info("Обустройство щенка/котенка - ID:{}", chatId);
                            sendMessage(chatId, Constants.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY);
                        }
                        case Keyboard.ADULT_DOG_SETUP, Keyboard.ADULT_CAT_SETUP -> {
                            logger.info("Обустройство взрослого животного - ID:{}", chatId);
                            sendMessage(chatId, Constants.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL);
                        }
                        case Keyboard.ARRANGEMENT_OF_DOG_WITH_DISABILITIES,
                                Keyboard.ARRANGEMENT_OF_CAT_WITH_DISABILITIES -> {
                            logger.info("Обустройство животного с ограниченными возможностями - ID:{}", chatId);
                            sendMessage(chatId, Constants.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL);
                        }
                        case Keyboard.DOG_HANDLERS_ADVICE -> {
                            logger.info("Советы кинолога - ID:{}", chatId);
                            sendMessage(chatId, Constants.DOG_HANDLERS_ADVICE);
                        }
                        case Keyboard.PROVEN_CYNOLOGISTS -> {
                            logger.info("Проверенные кинологи для обращения - ID:{}", chatId);
                            sendMessage(chatId, Constants.DOG_HANDLERS_CONTACTS);
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

    private void getReport(Message message) {
        PhotoSize photoSize = message.photo()[message.photo().length - 1];
        String caption = message.caption();
        Long chatId = message.chat().id();
        Owner owner = ownerRepository.getOwnerByChatId(chatId);
        try {
            reportService.createFromTelegram(photoSize.fileId(), caption, owner.getId());
            sendMessage(chatId, "Ваш отчёт принят.");
        } catch (Exception e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    private void getContact(Long chatId, String text) {
        Pattern pattern = Pattern.compile("^(\\d{11})$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            Owner byId = ownerService.getByChatId(chatId);
            byId.setPhone(matcher.group(1));
            ownerService.update(byId);
            sendMessage(chatId, "Телефон принят");
        } else {
            sendMessage(chatId, "Неверно ввел");
        }
        logger.info("Прилетел телефон - ID:{} тел:{} ", chatId, text);
    }

    private void sendPhoto(Long chatId) {
        Owner owner = ownerRepository.getOwnerByChatId(chatId);
        GetFile getFile = new GetFile(reportService.getById(chatId).getPhotoId());
        GetFileResponse getFileResponse = telegramBot.execute(getFile);
        TrialPeriod trialPeriod = trialPeriodService.findById(reportService.getById(chatId).getTrialPeriodId());
        if (getFileResponse.isOk()) {
            try {
                byte[] image = telegramBot.getFileContent(getFileResponse.file());
                SendPhoto sendPhoto = new SendPhoto(chatId, image);
                sendPhoto.caption("Id владельца: " + trialPeriod.getOwnerId() + "\n" +
                        "Id испытательного срока: " + trialPeriod.getId() + "\n" +
                        "Id отчёта:" + chatId);
                telegramBot.execute(sendPhoto);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void sendReportExample(Long chatId, AnimalType animalType) {
        String resource;
        if(animalType == AnimalType.CAT){
            resource = "/images/cat.jpg";
        } else
            resource = "/images/dog.jpg";

        try {
            byte[] photo = Files.readAllBytes(
                    Paths.get(Objects.requireNonNull(UpdatesListener.class.getResource(resource)).toURI())
            );
            SendPhoto sendPhoto = new SendPhoto(chatId, photo);
            sendPhoto.caption("""
                    Рацион питания: ваш текст;
                    Общее самочувствие: ваш текст;
                    Изменение поведения: ваш текст;
                    """);
            telegramBot.execute(sendPhoto);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private String getStringFromList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(o -> sb.append(o)
                .append("\n")
                .append("============").append("\n"));
        return sb.toString();
    }
}
