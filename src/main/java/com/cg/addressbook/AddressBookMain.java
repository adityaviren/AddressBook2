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

    public static void main(String[] args) throws IOException, SQLException {
        AddressBookDictionary abd = new AddressBookDictionary();
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Address Book System");
        boolean loop1 = true, loop2 ,add_avail;
        int choice1, choice2;
        AddressBook book;
        String name,address_book_name,first_name,last_name,city,state;
        while (loop1) {
            System.out.println("""
					Enter 1 to add new Address Book
					Enter 2 to modify an Address Book
					Enter 3 to return all people of a city
					Enter 4 to return all people of a state
					Enter 5 to view all people of a city
					Enter 6 to view all people of a state
					Enter 7 to get number of people in the city
					Enter 8 to get number of people in the state
					Enter 0 to exit""");
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
                            System.out.println("""
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
								Enter 0 to exit""");
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

class AddressBook extends Contact {
    Map<String,ArrayList<Contact>> city_wise_map = new HashMap<>();
    Map<String,ArrayList<Contact>> state_wise_map = new HashMap<>();
    private ArrayList<Contact> address_book = new ArrayList<>();
    private String name;

    AddressBook() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean nameExists(Contact c) {
        return address_book.stream().anyMatch(contact -> contact.equals(c));
    }

    public void addDetails(Contact contact) {
        address_book.add(contact);

        ArrayList<Contact> cityContact = city_wise_map.get(contact.getCity());
        if(cityContact==null){
            ArrayList<Contact> firstInsertion = new ArrayList<>();
            firstInsertion.add(contact);
            city_wise_map.put(contact.getCity(),firstInsertion);
        }
        else {
            cityContact.add(contact);
            city_wise_map.put(contact.getCity(), cityContact);
        }

        ArrayList<Contact> stateContact = state_wise_map.get(contact.getState());
        if(cityContact==null){
            ArrayList<Contact> firstInsertion = new ArrayList<>();
            firstInsertion.add(contact);
            state_wise_map.put(contact.getState(),firstInsertion);
        }
        else {
            stateContact.add(contact);
            state_wise_map.put(contact.getState(), stateContact);
        }
    }

    public void sortByName() {
        List<Contact> list=address_book.stream().sorted(Comparator.comparing(Contact::getName)).collect(Collectors.toList());
        address_book = new ArrayList<>(list);
        address_book.stream().forEach(System.out::println);
    }

    public void sortByCity() {
        List<Contact> list=address_book.stream().sorted(Comparator.comparing(Contact::getCity)).collect(Collectors.toList());
        address_book = new ArrayList<>(list);
    }

    public void sortByState() {
        List<Contact> list=address_book.stream().sorted(Comparator.comparing(Contact::getState)).collect(Collectors.toList());
        address_book = new ArrayList<>(list);
    }

    public void sortByZip() {
        List<Contact> list=address_book.stream().sorted(Comparator.comparing(Contact::getZip)).collect(Collectors.toList());
        address_book = new ArrayList<>(list);
    }

    public void viewAllContacts() {
        address_book.stream().forEach(System.out::println);
    }

    public int countByCity(String city){
        return city_wise_map.get(city).size();
    }

    public int countByState(String state){
        return state_wise_map.get(state).size();
    }

    public ArrayList<Contact> viewPersonByCity(String city) {
        return city_wise_map.get(city);
    }

    public ArrayList<Contact> viewPersonByState(String state) {
        return state_wise_map.get(state);
    }

    public void viewByCity(String city) {
        city_wise_map.values().stream().forEach(contacts -> System.out.println(contacts));
    }

    public void viewByState(String state){
        state_wise_map.values().stream().forEach(contacts -> System.out.println(contacts));
    }

    public void deleteContact() {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter first name");
        String first_name = sc.nextLine();
        System.out.println("Enter last name");
        String last_name = sc.nextLine();
        boolean check = false;
        for (Contact c : address_book) {
            if (c.getFirst().equalsIgnoreCase(first_name) && c.getLast().equalsIgnoreCase(last_name)) {
                address_book.remove(c);
                check = true;
                break;
            }
        }
        if (!check)
            System.out.println("Contact not found");
        else
            System.out.println("Contact deleted");
    }

    public void editContact() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter first name");
        String first_name = sc.nextLine();
        System.out.println("Enter last name");
        String last_name = sc.nextLine();
        boolean check = false;
        for (Contact c : address_book) {

            if (c.getFirst().equalsIgnoreCase(first_name) && c.getLast().equalsIgnoreCase(last_name)) {
                c.setFirst(first_name);
                c.setLast(last_name);
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
                check = true;
                break;
            }
        }
        if (!check)
            System.out.println("Contact not found");
        else
            System.out.println("Contact edited");
    }

    public void readFromFile() throws FileNotFoundException {
        File f=new File("F:\\Local contacts.txt");
        Scanner myFile = new Scanner(f);
        while(myFile.hasNextLine()){
            try
            {
                Contact c= new Contact();
                String data=myFile.nextLine();
                String[] str=data.split(" ");
                c.setFirst(str[0]);
                c.setLast(str[1]);
                c.setAddress(str[2]);
                c.setCity(str[3]);
                c.setState(str[4]);
                c.setZip(str[5]);
                c.setPhone(str[6]);
                c.setEmail(str[7]);
                addDetails(c);
            }catch (Exception e){
                System.out.println("Invalid contact");
            }
        }

    }

    public void writeInFile() {
        try {
            FileWriter fileWriter = new FileWriter("F:\\Address Book.txt",true);
            for (Contact c:address_book){
                fileWriter.write(c.getFirst()+" "+c.getLast()+" "
                        +c.getAddress()+" "+c.getCity()+" "+c.getState()+" "+c.getState()+" "
                        +c.getPhone()+" "+c.getEmail()+"\n");
            }
            fileWriter.close();
        }
        catch (IOException e){
            System.out.println("File not exists.");
        }
    }


    public void writeCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        String CSV_write_file = "F:\\AddressBookCSVwrite.txt";
        Writer writer = Files.newBufferedWriter(Paths.get(CSV_write_file));

        StatefulBeanToCsv<Contact> beanToCsv=new StatefulBeanToCsvBuilder(writer).
                                                 withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
        beanToCsv.write(address_book);
        writer.close();

    }

    public void readCSV() throws IOException, CsvValidationException {
        String CSV_read_file = "F:\\AddressBookCSVread.txt";
        Reader reader = Files.newBufferedReader(Paths.get(CSV_read_file));

        CSVReader csvReader = new CSVReader(reader);

        String[] nextRecord;
        nextRecord = csvReader.readNext();
        while((nextRecord = csvReader.readNext())!=null) {
        Contact c = new Contact();
            c.setAddress(nextRecord[0]);
            c.setCity(nextRecord[1]);
            c.setEmail(nextRecord[2]);
            c.setFirst(nextRecord[3]);
            c.setLast(nextRecord[4]);
            c.setPhone(nextRecord[5]);
            c.setState(nextRecord[6]);
            c.setZip(nextRecord[7]);
            address_book.add(c);
        }
    }

    public void writeJSON() throws IOException {
        String JSON_write_file = "F:\\Directory\\AddressBookJSONwrite.txt";
        Gson gson = new Gson();
        String json = gson.toJson(address_book);
        FileWriter Writer = new FileWriter(JSON_write_file);
        Writer.write(json);
        Writer.close();
    }

    public void readJSON() throws FileNotFoundException {
        String JSON_read_file = "F:\\Directory\\AddressBookJSONread.txt";
        Gson gson = new Gson();
        BufferedReader br= new BufferedReader(new FileReader(JSON_read_file));
        Contact[] usrObj= gson.fromJson(br, Contact[].class);
        List<Contact> contactList = Arrays.asList(usrObj);
        for (Contact c : contactList){
            address_book.add(c);
        }
    }

    public void readDB() throws SQLException {
        ConnectionCreate c = new ConnectionCreate();
        Statement stmt = c.makeConnection().createStatement();
        String sql = "select * from addressbook;";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()){
            Contact contact = new Contact();
            contact.setFirst(rs.getString(1));
            contact.setLast(rs.getString(2));
            contact.setAddress(rs.getString(3));
            contact.setCity(rs.getString(4));
            contact.setState(rs.getString(5));
            contact.setZip(rs.getString(6));
            contact.setPhone(rs.getString(7));
            contact.setEmail(rs.getString(8));
            address_book.add(contact);
        }
    }

    public boolean checkSync(String name) throws SQLException {
        ConnectionCreate c = new ConnectionCreate();
        Connection con = c.makeConnection();
        PreparedStatement prep = con.prepareStatement("select phone from addressbook where name = ?");
        prep.setString(1,name);
        ResultSet rs = prep.executeQuery();
        String local_phone = rs.getString(1);
        for(Contact contact : address_book){
            if(contact.getFirst().equals(name)){
                if(contact.getPhone().equals(local_phone)){
                    return true;
                }
            }
        }
        return false;
    }

    public int findDoj(String start,String end)
    {
        int count=0;
        String sql="select * from address where DOJ between ? and ?;";
        try
        {
            ConnectionCreate c = new ConnectionCreate();
            Connection con = c.makeConnection();
            PreparedStatement statement=con.prepareStatement(sql);
            statement.setString(1,start);
            statement.setString(2,end);
            ResultSet r=statement.executeQuery();
            while(r.next())
            {
                count++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int retrieveByState(String state)
    {
        int count = 0;
        String sql = "select * from address where state=?";
        try {
            ConnectionCreate c = new ConnectionCreate();
            Connection con = c.makeConnection();
            PreparedStatement statement=con.prepareStatement(sql);
            statement.setString(1, state);
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                count++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int retrieveByCity(String city)
    {
        int count = 0;
        String sql = "select * from address where vity=?";
        try {
            ConnectionCreate c = new ConnectionCreate();
            Connection con = c.makeConnection();
            PreparedStatement statement=con.prepareStatement(sql);
            statement.setString(1, city);
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                count++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public void insert(Contact c)
    {
        String sql="insert into contact (first_name,last_name,address,city,state,zip,phone,email) values (?,?,?,?,?,?,?,?);";
        try {
            ConnectionCreate conc = new ConnectionCreate();
            Connection con = conc.makeConnection();
            PreparedStatement statement=con.prepareStatement(sql);
            statement.setString(1, c.getFirst());
            statement.setString(2,c.getLast());
            statement.setString(3,c.getAddress());
            statement.setString(4,c.getCity());
            statement.setString(5,c.getState());
            statement.setString(6,c.getZip());
            statement.setString(7,c.getPhone());
            statement.setString(8,c.getEmail());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}

class AddressBookDictionary extends AddressBook {

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
