package com.cg.addressbook;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressBookMainTest {

    @Test
    public void givenClass_shouldReturnConnection() {
        ConnectionCreate jdbc = new ConnectionCreate();
        Connection con = jdbc.makeConnection();
        Assert.assertNotNull(con);
    }

    @Test
    public void givenConnection_shouldReadFromTable() throws SQLException {
        AddressBook addressBook = new AddressBook();
        Assert.assertNotNull(addressBook.readDB());
    }

    @Test
    public void givenName_shouldReturnIfSync() throws SQLException {
        AddressBook addressBook = new AddressBook();
        addressBook.readDB();
        Assert.assertTrue(addressBook.checkSync("chetan sharma"));
    }

    @Test
    public void givenStartAndEndDate_shouldReturnNumberOfPeople() throws SQLException {
        AddressBook addressBook = new AddressBook();
        addressBook.readDB();
        Assert.assertEquals(2,addressBook.findDoj("2018-01-01","2019-01-01"));
    }

    @Test
    public void givenStateAndCity_shouldReturnNumberOfPeople() throws SQLException {
        AddressBook addressBook = new AddressBook();
        addressBook.readDB();
        Assert.assertEquals(2,addressBook.retrieveByState("chandigarh"));
        Assert.assertEquals(1,addressBook.retrieveByCity("panchkula"));
    }

    @Test
    public void givenContact_shouldInsertInDB() throws SQLException {
        AddressBook addressBook = new AddressBook();
        addressBook.readDB();
        int size = addressBook.sizeOfAddressBook();
        Contact contact = new Contact("aditya","viren",
                "mohali","mohali","punjab","160000",
                "98989898","adi@gmail.com",new Date(1000000L));
        addressBook.insert(contact);
        addressBook.readDB();
        Assert.assertEquals(4,size+1);

    }

    @Test
    public void givenAddressBook_shouldInsertAll() throws IOException, CsvValidationException, SQLException {
        AddressBook addressBook = new AddressBook();
        addressBook.readDB();
        int size = addressBook.sizeOfAddressBook();
        Contact contact1 = new Contact("aditya","viren",
                "mohali","mohali","punjab","160000",
                "98989898","adi@gmail.com",new Date(1000000L));
        Contact contact2 = new Contact("aditya","vir",
                "mohal","mohal","punja","16000",
                "9898989","adi@gmail.co",new Date(1000000L));
        Contact contact3 = new Contact("aditya","sharma",
                "moha","moha","pun","1600",
                "989898","adi@gmail.c",new Date(1000000L));
        ArrayList<Contact> arrayList = new ArrayList<>();
        arrayList.add(contact1);
        arrayList.add(contact2);
        arrayList.add(contact3);
        addressBook.insertMultiple(arrayList);
        addressBook.readDB();
        Assert.assertEquals(16,size+3);
    }
}
