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
import com.teamwork.telegrambotanimalshelter.model.Report;
import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.replymarkup.ReplyMarkUp;
import com.teamwork.telegrambotanimalshelter.repository.OwnerRepository;
import com.teamwork.telegrambotanimalshelter.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;


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

                if(!ownerRepository.existsOwnerByChatId(chatId)){
                    ownerService.create(new Owner(chatId, chat.firstName(), new ArrayList<>()));
                }

                Pattern pattern = Pattern.compile("(\\+7\\d+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    getContact(chatId, text);
                    return;
                }

                Pattern pattern2 = Pattern.compile("(ID:)\\s(\\d+)");
                Matcher matcher2 = pattern2.matcher(text);
                if(matcher2.find()){
                    Long animalId = parseLong(matcher2.group(2));
                    setOwner(animalId, chatId);
                    sendMessage(chatId, "Поздравляем! Вы стали временным владельцем животного! Не забывайте о ежедневных отчетах!");
                }

                Pattern pattern1 = Pattern.compile("(Номер отчета:)\\s(\\d+)");
                Matcher matcher1 = pattern1.matcher(text);
                if (matcher1.find()) {
                    Long reportId = parseLong(matcher1.group(2));
                    Long trialPeriodId = reportService.getById(reportId).getTrialPeriodId();
                    AnimalType animalType = trialPeriodService.findById(trialPeriodId).getAnimalType();

                    switch (animalType){
                        case CAT -> sendPhotoFromReportToVolunteer(reportId, Constants.VOLUNTEER_CAT_SHELTER);

                        case DOG -> sendPhotoFromReportToVolunteer(reportId, Constants.VOLUNTEER_DOG_SHELTER);
                    }
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
                            AnimalType type = owner.getOwnerType();
                            if (type.equals(AnimalType.CAT)){
                                replyMarkup.sendMenuCat(chatId);
                            }
                            if (type.equals(AnimalType.DOG)){
                                replyMarkup.sendMenuDog(chatId);
                            }
                        }
                        case Keyboard.MAIN_MENU -> {
                            if(owner.getAnimals().isEmpty()) {
                                owner.setOwnerType(null);
                                ownerService.update(owner);
                                replyMarkup.sendMessageStart(chatId);
                            } else {
                                AnimalType type = owner.getOwnerType();
                                switch (type) {
                                    case CAT -> {
                                        sendMenuStage(AnimalType.CAT, chatId);
                                        sendMessage(chatId, """
                                                На данный момент вы уже являетесь клиентом приюта для котов и не можете выйти в главное меню.
                                                Если есть вопросы, обратитесь к волонтёру.
                                                """);
                                    }
                                    case DOG -> {
                                        sendMenuStage(AnimalType.DOG, chatId);
                                        sendMessage(chatId, """
                                                На данный момент вы уже являетесь клиентом приюта для собак и не можете выйти в главное меню.
                                                Если есть вопросы, обратитесь к волонтёру.
                                                """);
                                    }
                                }
                            }
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
                        case Keyboard.SEND_REPORT_FORM -> {
                            AnimalType type = owner.getOwnerType();
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
                            logger.info("Позвали волонтёра - ID: {}", chatId);
                            sendMessage(chatId, "Мы направили ваш запрос волонтеру, скоро он с вами свяжется!");
                            AnimalType type = owner.getOwnerType();
                            switch (type) {
                                case CAT -> sendMessage(Constants.VOLUNTEER_CAT_SHELTER, "Напиши этому человеку: @" + username);
                                case DOG -> sendMessage(Constants.VOLUNTEER_DOG_SHELTER, "Напиши этому человеку: @" + username);
                                default -> sendMessage(chatId, "К сожалению, мы не можем с вами связаться, напишите волонтеру самостоятельно. Спасибо! "
                                            + Constants.VOLUNTEER_INVITE);
                            }
                        }
                        case Keyboard.FAQ -> {
                            AnimalType type = owner.getOwnerType();
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
                        case Keyboard.PROVEN_DOG_HANDLERS -> {
                            logger.info("Проверенные кинологи для обращения - ID:{}", chatId);
                            sendMessage(chatId, Constants.DOG_HANDLERS_CONTACTS);
                        }
                        case Keyboard.SEND_REPORT_TO_VOLUNTEER -> {
                            if (Objects.equals(chatId, Constants.VOLUNTEER_DOG_SHELTER) || Objects.equals(chatId, Constants.VOLUNTEER_CAT_SHELTER)){
                                logger.info("Запрос на получение фотоотчёта - ID:{}", chatId);
                                sendMessage(chatId, "Введи номер отчета (только цифры, без дополнительных символов)");
                            }
                            else {
                                logger.info("Запрос на получение фотоотчёта сторонним пользователем- ID:{}", chatId);
                                sendMessage(chatId, "Здравствуйте! Данный функционал бота доступен только для волонтеров!");
                            }
                        }
                        case Keyboard.TB_GUIDELINES -> {
                            logger.info("Рекомендации по ТБ - ID:{}", chatId);
                            sendMessage(chatId, Constants.TB_GUIDELINES);
                        }
                        case Keyboard.GET_ANIMAL -> {
                            logger.info("Забирают животное - ID:{}", chatId);
                            sendMessage(chatId, "Введите номер животного в формате ID: 1");
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

    public void getReport(Message message) {
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
        Pattern pattern = Pattern.compile("^(\\+\\d{11})$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            Owner byId = ownerService.getByChatId(chatId);
            byId.setPhone(matcher.group(1));
            ownerService.update(byId);
            sendMessage(chatId, "Телефон принят");
        } else {
            sendMessage(chatId, "Пожалуйста, введите корректный номер!");
        }
        logger.info("Прилетел телефон - ID:{} тел:{} ", chatId, text);
    }

    public void sendPhotoFromReportToVolunteer(Long reportId, Long volunteerChatId) {
        GetFile getFile = new GetFile(reportService.getById(reportId).getPhotoId());
        GetFileResponse getFileResponse = telegramBot.execute(getFile);
        Report report = reportService.getById(reportId);
        TrialPeriod trialPeriod = trialPeriodService.findById(report.getTrialPeriodId());
        if (getFileResponse.isOk()) {
            try {
                byte[] image = telegramBot.getFileContent(getFileResponse.file());
                SendPhoto sendPhoto = new SendPhoto(volunteerChatId, image);
                sendPhoto.caption("Id владельца: " + trialPeriod.getOwnerId() + "\n" +
                        "Id испытательного срока: " + trialPeriod.getId() + "\n" +
                        "Id отчёта:" + reportId + "\n" +
                        "Рацион питания: " + report.getFoodRation() + "\n" +
                        "Изменение поведения: " + report.getBehaviorChange() + "\n" +
                        "Состояние здоровья: " + report.getStateOfHealth());
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
                    Изменение поведения: ваш текст
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
    private void setOwner(Long animalId, Long chatId){
        Owner owner = ownerService.getByChatId(chatId);
        animalService.setOwner(animalId, owner);
    }

    @Scheduled(cron = "@daily")
    private void sendNotification() {
        for (Owner owner : ownerService.getAll()) {
            for (TrialPeriod trialPeriod : trialPeriodService.findAllByOwnerId(owner.getId())) {
                if ((trialPeriod.getReports().size() < 60 && !trialPeriod.getLastDateOfReport().isEqual(trialPeriod.getDateOfTheEnd())) &&
                        trialPeriod.getLastDateOfReport().isBefore(LocalDate.now().minusDays(2))) {
                    sendMessage(owner.getChatId(), "Вы не отправляли отчёты уже более двух дней. " +
                            "Пожалуйста, отправьте отчёт или выйдите на связь с волонтёрами.");
                    if (owner.getOwnerType().equals(AnimalType.CAT)) {
                        sendMessage(Constants.VOLUNTEER_CAT_SHELTER, "Напиши этому человеку c ID = " + owner.getChatId()
                                + ". Он не отправлял отчет о животном более двух дней");
                    } else if (owner.getOwnerType().equals(AnimalType.DOG))
                        sendMessage(Constants.VOLUNTEER_DOG_SHELTER, "Напиши этому человеку c ID = " + owner.getChatId()
                                + ". Он не отправлял отчет о животном более двух дней");
                }
            }
        }
    }

    @Scheduled(cron = "@daily")
    public void sendTrialPeriodStatus() {
        for (Owner owner : ownerService.getAll()) {
            for (TrialPeriod trialPeriod : trialPeriodService.findAllByOwnerId(owner.getId())) {
                if (trialPeriod.getPeriodType().equals(TrialPeriodType.FAILED)) {
                    sendMessage(owner.getChatId(),
                            """ 
                            Здравстуйте!
                            К сожалению, вы не выполнили обязательное условие по уходу за животным и мы вынуждены забрать животное обратно в приют.
                            Пожалуйста, свяжитесь с волонтёром, если вам нужна будет помощь!
                            """);

                } else if (trialPeriod.getPeriodType().equals(TrialPeriodType.EXTENDED)) {
                    sendMessage(owner.getChatId(),
                            """
                            Вам продлили испытательный срок.
                            Для подробной информации, пожалуйста, свяжитесь с волонтёром.
                            """);
                } else if (trialPeriod.getPeriodType().equals(TrialPeriodType.FINISHED)) {
                    sendMessage(owner.getChatId(),
                            """
                            Поздравляем!
                            Вы прошли испытательный срок!
                            Теперь вы - настоящий родитель своего любимого питомца!
                            Будьте счастливы!
                            Если остались вопросы, вы всегда можете обратиться к нашим волонтёрам!
                            """);
                }
            }
        }
    }
}
