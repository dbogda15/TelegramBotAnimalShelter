### Telegram bot abilities:
- to answer popular questions from people about what you need to know and do to pick up an animal 🐱 🐶 from a shelter;
- to receive daily reports 📄 on how the animal adapts to the new environment;
- to pass difficult questions to a volunteer 🙋🏻

Для запуска программы поменяйте следующие параметры:
- в классе *Constants*:
    - VOLUNTEER_1_ID - чат ID волонтера приюта для кошек 
    - VOLUNTEER_2_ID - чат ID волонтера приюта для собак
    - VOLUNTEER_INVITE - вместо USERNAME вписать имя пользователя универсального волонтера
- в *hibernate.cfg.xml* подставить свои значения в следующин свойства:
    - connection.url - путь до базы данных
    - connection.username - имя пользователя от БД
    - connection.password - пароль от БД
- изменить значение свойств в файле application.properties:
    - spring.datasource.url - путь до базы данных
    - spring.datasource.username - имя пользователя от БД
    - spring.datasource.password - пароль от БД