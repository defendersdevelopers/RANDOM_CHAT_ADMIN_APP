package com.kyle.admin.models;

public class Country {
    public String CountryCode;
    public String CountryName;
    public String CountryFlag;

    public Country(String countryCode, String countryName, String countryFlag) {
        CountryCode = countryCode;
        CountryName = countryName;
        CountryFlag = countryFlag;
    }
}
