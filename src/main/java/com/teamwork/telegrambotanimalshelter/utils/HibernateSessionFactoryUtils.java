package com.teamwork.telegrambotanimalshelter.utils;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import org.hibernate.SessionFactory;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration conf = new Configuration().configure("hibernate.cfg.xml");
            conf.addAnnotatedClass(Owner.class).addAnnotatedClass(Animal.class);
            sessionFactory = conf.buildSessionFactory(
                    new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build()
            );
        }
        return sessionFactory;
    }
}
