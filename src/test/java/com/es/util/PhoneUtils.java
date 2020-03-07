package com.es.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;


public class PhoneUtils {

    static PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public static Phonenumber.PhoneNumber parseMobile(String mobile) {
        try {
            return phoneUtil.parse(mobile, "CN");
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return null;
    }

    public static String formatPhone(String phone) {
        Phonenumber.PhoneNumber phoneNumber = parseMobile(phone);
        return formatPhone(phoneNumber);
    }

    public static String formatPhone(Phonenumber.PhoneNumber phoneNumber) {
        if (phoneNumber.getCountryCode() == 86) {
            return String.valueOf(phoneNumber.getNationalNumber());
        }
        return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }
}
