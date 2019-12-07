package com.example.knowyourgov;

import java.io.Serializable;
import java.util.ArrayList;

public class Official implements Serializable {
    String title;
    String party;
    String name;
    String phones;
    String website;
    ArrayList<String> address;
    String gplus;
    String twitter;
    String youtube;
    String facebook;
    String email;
    String photoURL;

    Official(String _name, String _title, String _party){
        name = _name;
        title = _title;
        party = _party;
    }

    void setPhones (String numbers){
        phones = numbers;
    }
    void setWebsite (String site){
        website = site;
    }
    void setAddress(ArrayList<String> _address){
        address = _address;
    }
    void setgplus (String _gplus){
        gplus = _gplus;
    }
    void setTwitter(String _twitter){
        twitter = _twitter;
    }
    void setYoutube(String _youtube){
        youtube = _youtube;
    }
    void setFacebook(String _facebook){
        facebook = _facebook;
    }
    void setEmail(String _email){
        email = _email;
    }
    void setPhotoURL(String _photoURL){
        photoURL = _photoURL;
    }
}
