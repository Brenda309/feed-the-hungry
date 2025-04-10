package com.brenda.FeedTheHungry.mail;

public interface EmailSender {
    void send(String to, String email) throws IllegalAccessException;
    void send(String to, String email, String subject) throws IllegalAccessException;
}