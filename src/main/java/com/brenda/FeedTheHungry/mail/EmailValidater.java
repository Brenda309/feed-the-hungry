package com.brenda.FeedTheHungry.mail;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;


@Service
public class EmailValidater implements Predicate<String> {
    @Override
    public boolean test(String s) {
        //TODO : regex for validating email
        return true;
    }
}