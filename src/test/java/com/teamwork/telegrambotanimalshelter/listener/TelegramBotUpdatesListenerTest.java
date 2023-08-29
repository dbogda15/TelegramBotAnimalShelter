package com.teamwork.telegrambotanimalshelter.listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
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
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.repository.OwnerRepository;
import com.teamwork.telegrambotanimalshelter.service.impl.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    TelegramBot telegramBot;
    @Mock
    SendResponse sendResponse;
    @Mock
    Update update;
    @Mock
    Message message;
    @Mock
    Owner owner;
    @Mock
    Animal animal;
    @Mock
    Report report;
    @Mock
    Chat chat;
    @Mock
    Shelter shelter;
    @Mock
    TrialPeriod trialPeriod;
    @Mock
    OwnerRepository ownerRepository;
    @Mock
    OwnerServiceImpl ownerService;
    @Mock
    ShelterServiceImpl shelterService;
    @Mock
    ReportServiceImpl reportService;
    @Mock
    TrialPeriodServiceImpl trialPeriodService;
    @InjectMocks
    TelegramBotUpdatesListener listener;
    @Captor
    ArgumentCaptor<SendMessage> captor;
    @Captor
    ArgumentCaptor<SendPhoto> photoCaptor;
    private final GetFileResponse getFileResponse = BotUtils.fromJson("""
            {
            "ok": true,
            "file": {
                "file_id": "file id"
                }
            }
            """, GetFileResponse.class);

    private final byte[] photo = Files.readAllBytes(
            Paths.get(Objects.requireNonNull(UpdatesListener.class.getResource("/images/cat.jpg")).toURI())
    );

    TelegramBotUpdatesListenerTest() throws IOException, URISyntaxException {
    }

    @Test
    void testStartCommand(){
        getCommand("/start");
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), Constants.WELCOME);
    }

    @Test
    void catShelterInfoCommandTest(){
        when(owner.getOwnerType()).thenReturn(AnimalType.CAT);
            getCommand(Keyboard.ABOUT_THE_SHELTER);
            var sentMessage = captor.getValue();
            assertEquals(sentMessage.getParameters().get("text"), Constants.CAT_SHELTER_INFO);
        }


    @Test
    void dogShelterInfoCommandTest(){
        when(owner.getOwnerType()).thenReturn(AnimalType.DOG);
        getCommand(Keyboard.ABOUT_THE_SHELTER);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), Constants.DOG_SHELTER_INFO
        );
    }

    @Test
    void catShelterGuards(){
        when(owner.getOwnerType()).thenReturn(AnimalType.CAT);
        getCommand(Keyboard.CONTACT_DETAILS_OF_THE_GUARD);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), Constants.GUARDS_INFO_CAT);
    }

    @Test
    void dogShelterGuards(){
        when(owner.getOwnerType()).thenReturn(AnimalType.DOG);
        getCommand(Keyboard.CONTACT_DETAILS_OF_THE_GUARD);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), Constants.GUARDS_INFO_DOG);
    }

    @Test
    void contactDetailsTest(){
        getCommand(Keyboard.CONTACT_DETAILS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Пожалуйста, введите свой номер телефона в формате +79171234123");
    }

    @Test
    void catShelterSchedule(){
        when(owner.getOwnerType()).thenReturn(AnimalType.CAT);
        when(shelterService.getByShelterType(AnimalType.CAT)).thenReturn(shelter);
        getCommand(Keyboard.WORK_SCHEDULE);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), shelter.getTimetable());
    }

    @Test
    void dogShelterSchedule(){
        when(owner.getOwnerType()).thenReturn(AnimalType.DOG);
        when(shelterService.getByShelterType(AnimalType.DOG)).thenReturn(shelter);
        getCommand(Keyboard.WORK_SCHEDULE);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), shelter.getTimetable());
    }

    @Test
    void callVolunteer(){
        getCommand(Keyboard.CALL_A_VOLUNTEER);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Мы направили ваш запрос волонтеру, скоро он с вами свяжется!");
    }

    @Test
    void rulesForDating(){
        getCommand(Keyboard.RULES_FOR_DATING_A_CAT);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.ANIMAL_DATING_RULES);
    }

    @Test
    void catCarriage(){
        getCommand(Keyboard.CAT_CARRIAGE);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.TRANSPORTATION_OF_THE_ANIMAL);
    }

    @Test
    void getDocuments(){
        getCommand(Keyboard.REQUIRED_DOCUMENTS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.REQUIRED_DOCUMENTS);
    }

    @Test
    void listOfReasonsForRejection(){
        getCommand(Keyboard.LIST_OF_REASONS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.LIST_OF_REASON_FOR_DENY);
    }
    @Test
    void puppySetup(){
        getCommand(Keyboard.PUPPY_SETUP);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY);
    }
    @Test
    void kittenSetup(){
        getCommand(Keyboard.KITTEN_SETUP);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY);
    }
    @Test
    void adultAnimalSetup(){
        getCommand(Keyboard.ADULT_DOG_SETUP);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL);
    }
    @Test
    void disabilityAnimalSetup(){
        getCommand(Keyboard.ARRANGEMENT_OF_DOG_WITH_DISABILITIES);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL);
    }

    @Test
    void dogHandlersAdvice(){
        getCommand(Keyboard.DOG_HANDLERS_ADVICE);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.DOG_HANDLERS_ADVICE);
    }

    @Test
    void provenDogHandlers(){
        getCommand(Keyboard.PROVEN_DOG_HANDLERS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.DOG_HANDLERS_CONTACTS);
    }

    @Test
    void safetyRulesGuide(){
        getCommand(Keyboard.TB_GUIDELINES);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"),Constants.TB_GUIDELINES);
    }

    @Test
    void getAnimal() {
        getCommand(Keyboard.GET_ANIMAL);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Введите номер животного в формате ID: 1");
    }
    @Test
    void sendReportExampleTest(){
        getCommand(Keyboard.SEND_REPORT_FORM);

        verify(telegramBot, times(1)).execute(photoCaptor.capture());
        reset(telegramBot);

        SendPhoto photo = photoCaptor.getValue();
        assertEquals(chat.id(), photo.getParameters().get("chat_id"));
        assertEquals("""
                    Рацион питания: ваш текст;
                    Общее самочувствие: ваш текст;
                    Изменение поведения: ваш текст
                    """, photo.getParameters().get("caption"));
    }

    @Test
    void sendReportToVolunteer() throws IOException {
        when(telegramBot.execute(any(GetFile.class))).thenReturn(getFileResponse);
        when(reportService.getById(any())).thenReturn(report);
        when(trialPeriodService.findById(any())).thenReturn(trialPeriod);
        when(telegramBot.getFileContent(any())).thenReturn(photo);

        listener.sendPhotoFromReportToVolunteer(report.getId(), 1L);

        verify(telegramBot, times(2)).execute(photoCaptor.capture());

        SendPhoto sendPhoto = photoCaptor.getValue();

        assertEquals(1L, sendPhoto.getParameters().get("chat_id"));
        assertEquals("Id владельца: " + owner.getId()  + "\n" +
                    "Id испытательного срока: " + trialPeriod.getId() + "\n" +
                    "Id отчёта:" + report.getId() + "\n" +
                    "Рацион питания: " + report.getFoodRation() + "\n" +
                    "Изменение поведения: " + report.getBehaviorChange() + "\n" +
                    "Состояние здоровья: " + report.getStateOfHealth(), sendPhoto.getParameters().get("caption"));

    }

    @Test
    void sendMessageStageTestCatShelter(){
        when(ownerService.getByChatId(any())).thenReturn(owner);
        owner.setOwnerType(AnimalType.CAT);
        getCommand(Keyboard.CAT_SHELTER);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Выберите приют:");
    }

    @Test
    void sendMessageStageTestDogShelter(){
        when(ownerService.getByChatId(any())).thenReturn(owner);
        owner.setOwnerType(AnimalType.DOG);
        getCommand(Keyboard.DOG_SHELTER);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Выберите приют:");
    }

    @Test
    void sendMenuCat(){
        when(owner.getOwnerType()).thenReturn(AnimalType.CAT);
        getCommand(Keyboard.INFORMATION_ABOUT_SHELTER);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Информация о кошачьем приюте");
    }

    @Test
    void sendMenuDog(){
        when(owner.getOwnerType()).thenReturn(AnimalType.DOG);
        getCommand(Keyboard.INFORMATION_ABOUT_SHELTER);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Информация о собачьем приюте");
    }

    @Test
    void getReportToVolunteer(){
        getCommand(Keyboard.SEND_REPORT_TO_VOLUNTEER);
        var sentMessage = captor.getValue();
        if (Objects.equals(chat.id(), Constants.VOLUNTEER_DOG_SHELTER) || Objects.equals(chat.id(), Constants.VOLUNTEER_CAT_SHELTER)) {
            assertEquals(sentMessage.getParameters().get("text"), "Введи номер отчета (только цифры, без дополнительных символов)");
        }
        else {
            assertEquals(sentMessage.getParameters().get("text"), "Здравствуйте! Данный функционал бота доступен только для волонтеров!");
        }
    }
    @Test
    void sendFAQMenuCat(){
        when(owner.getOwnerType()).thenReturn(AnimalType.CAT);
        getCommand(Keyboard.FAQ);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Все о кошках");
    }

    @Test
    void sendFAQMenuDog(){
        when(owner.getOwnerType()).thenReturn(AnimalType.DOG);
        getCommand(Keyboard.FAQ);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Все о собаках");
    }
    @Test
    void backToMenuCat(){
        getCommand(Keyboard.BACK_TO_ALL_ABOUT_CATS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Все о кошках");
    }

    @Test
    void backToMenuDog(){
        getCommand(Keyboard.BACK_TO_ALL_ABOUT_DOGS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Все о собаках");
    }

    @Test
    void recommendationsForCat(){
        getCommand(Keyboard.RECOMMENDATIONS_FOR_CATS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), Keyboard.RECOMMENDATIONS_FOR_CATS);
    }

    @Test
    void recommendationsForDog(){
        getCommand(Keyboard.RECOMMENDATIONS_FOR_DOGS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), Keyboard.RECOMMENDATIONS_FOR_DOGS);
    }

    @Test
    void mainMenuWhenOwnerTypeIsNull(){
        when(owner.getAnimals()).thenReturn(Collections.emptyList());
        owner.setOwnerType(null);
        ownerService.update(owner);
        getCommand(Keyboard.MAIN_MENU);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), Constants.WELCOME);
    }

    @Test
    void mainMenuWhenOwnerTypeIsCat(){
        when(ownerService.getByChatId(chat.id())).thenReturn(owner);
        when(owner.getAnimals()).thenReturn(List.of(animal));
        when(owner.getOwnerType()).thenReturn(AnimalType.CAT);
        owner.setOwnerType(AnimalType.CAT);
        ownerService.update(owner);
        getCommandMainMenu(Keyboard.MAIN_MENU);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), """
                                                На данный момент вы уже являетесь клиентом приюта для котов и не можете выйти в главное меню.
                                                Если есть вопросы, обратитесь к волонтёру.
                                                """);
    }
    @Test
    void mainMenuWhenOwnerTypeIsDog(){
        when(ownerService.getByChatId(chat.id())).thenReturn(owner);
        when(owner.getAnimals()).thenReturn(List.of(animal));
        when(owner.getOwnerType()).thenReturn(AnimalType.DOG);
        owner.setOwnerType(AnimalType.DOG);
        ownerService.update(owner);
        getCommandMainMenu(Keyboard.MAIN_MENU);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), """
                                                На данный момент вы уже являетесь клиентом приюта для собак и не можете выйти в главное меню.
                                                Если есть вопросы, обратитесь к волонтёру.
                                                """);
    }
                     
    @Test
    void sendMenuCatList() {
        when(shelterService.getByShelterType(AnimalType.CAT)).thenReturn(shelter);
        shelter.setAnimalList(List.of(animal));
        getCommand(Keyboard.LIST_OF_CATS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Кошек нет");
    }

    @Test
    void sendMenuDogList() {
        when(shelterService.getByShelterType(AnimalType.DOG)).thenReturn(shelter);
        shelter.setAnimalList(List.of(animal));
        getCommand(Keyboard.LIST_OF_DOGS);
        var sentMessage = captor.getValue();
        assertEquals(sentMessage.getParameters().get("text"), "Собак нет");
    }
                     
    private void getCommand(String command){
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(command);
        when(message.chat()).thenReturn(chat);
        when(ownerRepository.getOwnerByChatId(chat.id())).thenReturn(owner);

        if (ownerRepository.existsOwnerByChatId(chat.id())){
            ownerService.create(new Owner(chat.id(), chat.firstName(), new ArrayList<>()));
        }

        listener.process(List.of(update));
        verify(telegramBot, times(1)).execute(captor.capture());
    }

    private void getCommandMainMenu(String command){
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(command);
        when(message.chat()).thenReturn(chat);
        when(ownerRepository.getOwnerByChatId(chat.id())).thenReturn(owner);

        if (ownerRepository.existsOwnerByChatId(chat.id())){
            ownerService.create(new Owner(chat.id(), chat.firstName(), new ArrayList<>()));
        }

        listener.process(List.of(update));
        verify(telegramBot, times(2)).execute(captor.capture());
    }

}