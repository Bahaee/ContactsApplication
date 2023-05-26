package com.example.contactsapp;

import java.io.Serializable;

public class Contact implements Serializable {
    String firstNameContact, lastNameContact, phoneNumberContact,emailContact, professionContact, imgUrlContact;
    boolean isFavorite = false;

    public Contact(String firstNameContact, String lastNameContact, String phoneNumberContact, String emailContact, String professionContact, String imgUrlContact) {
        this.firstNameContact = firstNameContact;
        this.lastNameContact = lastNameContact;
        this.phoneNumberContact = phoneNumberContact;
        this.emailContact = emailContact;
        this.professionContact = professionContact;
        this.imgUrlContact = imgUrlContact;
    }

    public Contact(String firstNameContact, String lastNameContact, String phoneNumberContact, String emailContact, String professionContact) {
        this.firstNameContact = firstNameContact;
        this.lastNameContact = lastNameContact;
        this.phoneNumberContact = phoneNumberContact;
        this.emailContact = emailContact;
        this.professionContact = professionContact;
    }

    public Contact(String firstNameContact, String lastNameContact, String phoneNumberContact, String emailContact) {
        this.firstNameContact = firstNameContact;
        this.lastNameContact = lastNameContact;
        this.phoneNumberContact = phoneNumberContact;
        this.emailContact = emailContact;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getFirstNameContact() {
        return firstNameContact;
    }

    public String getLastNameContact() {
        return lastNameContact;
    }

    public String getPhoneNumberContact() {
        return phoneNumberContact;
    }

    public String getProfessionContact() {
        return professionContact;
    }

    public String getImgUrlContact() {
        return imgUrlContact;
    }

    public void setFirstNameContact(String firstNameContact) {
        this.firstNameContact = firstNameContact;
    }

    public void setLastNameContact(String lastNameContact) {
        this.lastNameContact = lastNameContact;
    }

    public void setPhoneNumberContact(String phoneNumberContact) {
        this.phoneNumberContact = phoneNumberContact;
    }

    public void setProfessionContact(String professionContact) {
        this.professionContact = professionContact;
    }

    public void setImgUrlContact(String imgUrlContact) {
        this.imgUrlContact = imgUrlContact;
    }
}
