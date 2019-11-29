package com.example.knowyourgov;

import java.util.ArrayList;

public class Office {
    private String name;
    private ArrayList<Integer> officialIndices;

    Office(String _name, ArrayList<Integer> indices){
        name = _name;
        officialIndices = indices;
    }
}
