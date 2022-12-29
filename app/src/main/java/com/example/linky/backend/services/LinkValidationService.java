package com.example.linky.backend.services;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a service that, based on the platform name, validates if a link is correct. For example,
 * any Facebook link must start with "https://www.facebook.com/" in order to be valid
 */
public class LinkValidationService {
    private static LinkValidationService instance = null;
    private final Map<String, String> regexes;    // platform -> regex

    //https://www.linkedin.com/in/mircea-feder-b460151aa/

    private LinkValidationService() {
        String HTTPS = "https://";
        String WWW = "www\\.";

        regexes = new HashMap<>();
        regexes.put("BeReal", String.format("%sbere.al/.+", HTTPS));
        regexes.put("Discord", ".+#\\d{4}");
        regexes.put("Facebook", String.format("%s(%s)?facebook\\.com/.+", HTTPS, WWW));
        regexes.put("GitHub", String.format("%s(%s)?github\\.com/.+", HTTPS, WWW));
        regexes.put("Gmail", ".+@gmail\\.com");
        regexes.put("Instagram", String.format("%s(%s)?instagram\\.com/.+", HTTPS, WWW));
        regexes.put("LinkedIn", String.format("%s(%s)?linkedin\\.com/in/.+", HTTPS, WWW));
        regexes.put("OnlyFans", String.format("%s(%s)?onlyfans\\.com/.+", HTTPS, WWW));
        regexes.put("Pinterest", String.format("%s.+pinterest\\.com/.+", HTTPS));
        regexes.put("Quora", String.format("%s(%s)?quora\\.com/profile/.+", HTTPS, WWW));
        regexes.put("Reddit", String.format("%s(%s)?reddit\\.com/user/.+", HTTPS, WWW));
        regexes.put(
                "Snapchat",
                String.format("%s(%s)?snapchat\\.com/add/.+\\?share_id=.+", HTTPS, WWW)
        );
        regexes.put("TikTok", String.format("%s(%s)?tiktok\\.com/@.+", HTTPS, WWW));
        regexes.put("Twitter", String.format("%s(%s)?twitter\\.com/.+", HTTPS, WWW));
        regexes.put("Yahoo", ".+@yahoo\\.com");
    }

    public static LinkValidationService getInstance() {
        if (instance == null)
            instance = new LinkValidationService();
        return instance;
    }

    public boolean checkLink(String platformName, String link) {
        if (!regexes.containsKey(platformName))
            return false;
        return link.matches(regexes.get(platformName));
    }
}
