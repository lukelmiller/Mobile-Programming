package com.csce4623.ahnelson.restclientexample;

public class User {
    protected class Address{
        protected class Geo{
            private String lat;
            private String lng;
            public String getLat() {
                return lat;
            }
            public String getLng() {
                return lng;
            }
        }

        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        public String getStreet() {
            return street;
        }
        public String getSuite() {
            return suite;
        }
        public String getCity() {
            return city;
        }
        public String getZipcode() {
            return zipcode;
        }
        public Geo getGeo() {
            return geo;
        }
    }

    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
