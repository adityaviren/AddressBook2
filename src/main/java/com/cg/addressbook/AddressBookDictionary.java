package com.cg.addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressBookDictionary extends AddressBook {

    Map<String, AddressBook> address_book_dictionary = new HashMap<>();

    public void addAddressBook(String name,AddressBook addressbook) {
        address_book_dictionary.put(name,addressbook);
    }

    public boolean isPresentAddressBook(String name) {
        return address_book_dictionary.containsKey(name);
    }

    public AddressBook returnAddressBook(String name) {
        return address_book_dictionary.get(name);
    }

    public int countByCity(String city) {
        return address_book_dictionary.values().stream().mapToInt(addressBook -> addressBook.countByCity(city)).sum();
    }
    public int countByState(String state) {
        return address_book_dictionary.values().stream().mapToInt(addressBook -> addressBook.countByState(state)).sum();
    }
    public void viewByCity(String city) {
        address_book_dictionary.values().stream().forEach(addressBook -> addressBook.viewByCity(city));
    }
    public void viewByState(String state) {
        address_book_dictionary.values().stream().forEach(addressBook -> addressBook.viewByState(state));
    }
    public ArrayList<Contact> returnByCity(String city) {
        ArrayList<Contact> cityContact = new ArrayList<>();
        address_book_dictionary.values().stream().forEach(c->cityContact.addAll(c.viewPersonByCity(city)));
        return cityContact;
    }
    public ArrayList<Contact> returnByState(String state) {
        ArrayList<Contact> stateContact = new ArrayList<>();
        address_book_dictionary.values().stream().forEach(c->stateContact.addAll(c.viewPersonByState(state)));
        return  stateContact;
    }

}
