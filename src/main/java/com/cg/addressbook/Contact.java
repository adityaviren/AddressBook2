package com.cg.addressbook;

import java.util.Date;

import static java.lang.CharSequence.compare;

public class Contact {
    protected String f_name;
    protected String l_name;
    protected String address;
    protected String city;
    protected String state;
    protected String zip;
    protected String phone_number;
    protected String email;
    protected Date start;

    public String getFirst() {
        return f_name;
    }

    public void setFirst(String f_name) {
        this.f_name = f_name;
    }

    public String getLast() {
        return l_name;
    }

    public void setLast(String l_name) {
        this.l_name = l_name;
    }

    public String getName() {
        return f_name + l_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone_number;
    }

    public void setPhone(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate(){
        return start;
    }

    public void setDate(Date start){
        this.start=start;
    }

    Contact(){

    }

    Contact(String f_name,
            String l_name,
            String address,
            String city,
            String state,
            String zip,
            String phone_number,
            String email,Date start){
        this.f_name=f_name;
        this.l_name=l_name;
        this.address=address;
        this.city=city;
        this.state=state;
        this.zip=zip;
        this.phone_number=phone_number;
        this.email=email;
        this.start=start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact ab = (Contact) o;
        return compare(ab.getFirst(), getFirst()) == 0 && compare(ab.getLast(), getLast()) == 0;
    }

    public String toString() {
        return "Name :" + getFirst() + " " + getLast() + "\nAddress :" + getAddress() + " " + getCity() + " " + getState() + " " + getZip()
                + "\nPhone Number : " + getPhone() + "\nEmail id :  " + getEmail() + "\n" + start + "\n";
    }

}
