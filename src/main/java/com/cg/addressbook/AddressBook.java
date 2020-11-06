package com.cg.addressbook;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class AddressBook extends Contact{
    Map<String, ArrayList<Contact>> city_wise_map = new HashMap<>();
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
        insert(contact);

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

    public int sizeOfAddressBook(){
        return address_book.size();
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

    public ArrayList<Contact> readDB() throws SQLException {
        ConnectionCreate c = new ConnectionCreate();
        Statement stmt = c.makeConnection().createStatement();
        String sql = "select * from address_book_table;";
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
            contact.setDate(rs.getDate(9));
            address_book.add(contact);
        }
        return address_book;
    }

    public boolean checkSync(String name) throws SQLException {
        String[] names = name.split(" ");
        if(names.length==2) {
            ConnectionCreate c = new ConnectionCreate();
            Connection con = c.makeConnection();
            PreparedStatement prep = con.prepareStatement("select phone from address_book_table where first_name = ? and last_name = ?");
            prep.setString(1, names[0]);
            prep.setString(2, names[1]);
            ResultSet rs = prep.executeQuery();
            rs.next();
            String local_phone = rs.getString(1);
            for (Contact contact : address_book) {
                if (contact.getFirst().equals(names[0]) && contact.getLast().equals(names[1])) {
                    if (contact.getPhone().equals(local_phone)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else
            return false;
    }

    public int findDoj(String start,String end)
    {
        int count=0;
        String sql="select * from address_book_table where DOJ between ? and ?;";
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
        String sql = "select * from address_book_table where state=?";
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
        String sql = "select * from address_book_table where city=?";
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
        String sql="insert into address_book_table (first_name,last_name,address,city,state,zip,phone,email) values (?,?,?,?,?,?,?,?);";
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

    public void insertMultiple(ArrayList<Contact> addressBook) {
        for(Contact c:addressBook) {
            Runnable task = () -> {
                insert(c);
            };
            Thread thread = new Thread(task);
            thread.start();
            try {
                thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
