package com.kyle.admin.Users;
import com.kyle.admin.models.Country;

import java.util.ArrayList;

public class User {
    public String userId,brief,country,city,name,profileImage,likes,love,userCountryCode;
    public int matchRatio;
    public String age;
    boolean blocked,hideAge,hideLocation,heIsUsingMatchPlus,heIsVIP;
    public ArrayList<String> images;
    public String interests;
    public String fakeName;
    String fakeCountry;
    String gems;
    ArrayList<Country> countriesList;
  public boolean suspended;
    public User(boolean suspended,String gems,String userId, ArrayList<Country> countriesList, String fakeCountry, String brief, String country, String city, String name, String profileImage, String likes, String love, String userCountryCode, int matchRatio, String age, boolean blocked, boolean hideAge, boolean hideLocation, boolean heIsUsingMatchPlus, boolean heIsVIP, ArrayList<String> images, String interests, String fakeName) {
        this.userId = userId;
        this.brief = brief;
        this.country = country;
        this.fakeCountry=fakeCountry;
        this.city = city;
        this.gems=gems;
        this.name = name;
        this.profileImage = profileImage;
        this.likes = likes;
        this.love = love;
        this.userCountryCode = userCountryCode;
        this.matchRatio = matchRatio;
        this.age = age;
        this.countriesList=countriesList;
        this.blocked = blocked;
        this.hideAge = hideAge;
        this.hideLocation = hideLocation;
        this.heIsUsingMatchPlus = heIsUsingMatchPlus;
        this.heIsVIP = heIsVIP;
        this.images = images;
        this.interests = interests;
        this.fakeName = fakeName;
        this.suspended=suspended;
    }
}