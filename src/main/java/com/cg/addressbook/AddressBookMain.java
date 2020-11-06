package com.cg.addressbook;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

import javax.print.DocFlavor;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.CharSequence.compare;


public class AddressBookMain {
    public static Contact getContact(String first, String last) {
        Scanner sc = new Scanner(System.in);
        Contact c = new Contact();
        c.setFirst(first);
        c.setLast(last);
        System.out.println("Enter Address");
        c.setAddress(sc.nextLine());
        System.out.println("Enter City");
        c.setCity(sc.nextLine());
        System.out.println("Enter State");
        c.setState(sc.nextLine());
        System.out.println("Enter ZIP");
        c.setZip(sc.nextLine());
        System.out.println("Enter Phone Number");
        c.setPhone(sc.nextLine());
        System.out.println("Enter Email Id");
        c.setEmail(sc.nextLine());
        return c;

    }

    public static AddressBook getAddressBook(String name) {
        AddressBook ab = new AddressBook();
        ab.setName(name);
        return ab;
    }

    public static void main(String[] args) throws IOException, SQLException, CsvValidationException {
        AddressBookDictionary abd = new AddressBookDictionary();
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Address Book System");
        boolean loop1 = true, loop2 ,add_avail;
        int choice1, choice2;
        AddressBook book;
        String name,address_book_name,first_name,last_name,city,state;
        while (loop1) {
            /*System.out.println("""
					Enter 1 to add new Address Book
					Enter 2 to modify an Address Book
					Enter 3 to return all people of a city
					Enter 4 to return all people of a state
					Enter 5 to view all people of a city
					Enter 6 to view all people of a state
					Enter 7 to get number of people in the city
					Enter 8 to get number of people in the state
					Enter 0 to exit""");*/
            choice1 = Integer.parseInt(sc.nextLine());
            switch (choice1) {
                case 1:
                    System.out.println("Enter the name of Address Book");
                    address_book_name=sc.nextLine();
                    abd.addAddressBook(address_book_name,getAddressBook(address_book_name));
                    break;
                case 2:
                    System.out.println("Enter the name of Address Book");
                    name = sc.nextLine();
                    add_avail = abd.isPresentAddressBook(name);
                    if (!add_avail)
                        System.out.println("Address Book not found");
                    else {

                        book = abd.returnAddressBook(name);
                        loop2=true;
                        while (loop2) {
                            /*System.out.println("""
								Enter 1 to add contact
								Enter 2 to view all contacts
								Enter 3 to edit a contact
								Enter 4 to delete a contact
								Enter 5 to sort the address book by name
								Enter 6 to sort the address book by city
								Enter 7 to sort the address book by state
								Enter 8 to sort the address book by zip
								Enter 9 to read from a file
								Enter 10 to write to a file
								Enter 11 to write in a CSV file
								Enter 12 to read from a CSV file
								Enter 13 to write in a JSON file
								Enter 14 to read from JSON file
								Enter 15 to read from DB
								Enter 16 to check if program and DB are in sync
								Enter 17 to return all people in the given dates
								Enter 18 to return number of people in the city
								Enter 19 to return number of people in the state
								Enter 20 to insert into DB
								Enter 21 to insert multiple in DB
								Enter 0 to exit""");*/
                            choice2 = Integer.parseInt(sc.nextLine());
                            switch (choice2) {
                                case 1:
                                    Contact contact= new Contact();
                                    System.out.println("Enter first name");
                                    first_name=sc.nextLine();
                                    contact.setFirst(first_name);
                                    System.out.println("Enter last name");
                                    last_name=sc.nextLine();
                                    contact.setLast(last_name);
                                    if(book.nameExists(contact))
                                        System.out.println("Contact already exists");
                                    else
                                        book.addDetails(getContact(first_name, last_name));
                                    break;
                                case 2:
                                    book.viewAllContacts();
                                    break;
                                case 3:
                                    book.editContact();
                                    break;
                                case 4:
                                    book.deleteContact();
                                    break;
                                case 5:
                                    book.sortByName();
                                    break;
                                case 6:
                                    book.sortByCity();
                                    break;
                                case 7:
                                    book.sortByState();
                                    break;
                                case 8:
                                    book.sortByZip();
                                    break;
                                case 9:
                                    try{
                                        book.readFromFile();
                                    }catch (FileNotFoundException e){
                                        System.out.println("File not found");
                                    }
                                    break;
                                case 10:
                                    book.writeInFile();
                                    break;
                                case 11:
                                    try {
                                        book.writeCSV();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (CsvDataTypeMismatchException e) {
                                        e.printStackTrace();
                                    } catch (CsvRequiredFieldEmptyException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 12:
                                    try {
                                        book.readCSV();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (CsvValidationException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 13:
                                    book.writeJSON();
                                    break;
                                case 14:
                                    book.readJSON();
                                    break;
                                case 15:
                                    book.readDB();
                                    break;
                                case 16:
                                    book.checkSync(sc.nextLine());
                                    break;
                                case 17:
                                    book.findDoj(sc.nextLine(),sc.nextLine());
                                    break;
                                case 18:
                                    System.out.println(book.retrieveByCity(sc.nextLine()));
                                    break;
                                case 19:
                                    System.out.println(book.retrieveByState(sc.nextLine()));
                                    break;
                                case 20:
                                    book.insert(getContact(sc.nextLine(),sc.nextLine()));
                                    break;
                                case 21:
                                    book.readCSV();
                                    book.insertMultiple(book.viewPersonByCity(sc.nextLine()));
                                    break;

                                default:
                                    loop2=false;
                                    break;
                            }
                        }

                    }
                    break;
                case 3 :
                    System.out.println("Enter the name of city");
                    city= sc.nextLine();
                    ArrayList<Contact> cityContact = abd.returnByCity(city);
                    break;
                case 4 :
                    System.out.println("Enter the name of state");
                    state= sc.nextLine();
                    ArrayList<Contact> stateContact = abd.returnByState(state);
                    break;
                case 5 :
                    System.out.println("Enter the name of city");
                    city= sc.nextLine();
                    abd.viewByCity(city);
                    break;
                case 6 :
                    System.out.println("Enter the name of State");
                    state= sc.nextLine();
                    abd.viewByState(state);
                    break;
                case 7:
                    System.out.println("Enter the name of city");
                    city= sc.nextLine();
                    abd.countByCity(city);
                    break;
                case 8:
                    System.out.println("Enter the name of State");
                    state= sc.nextLine();
                    abd.countByState(state);
                    break;

                default:
                    loop1 = false;
                    break;

            }
        }
    }
}


