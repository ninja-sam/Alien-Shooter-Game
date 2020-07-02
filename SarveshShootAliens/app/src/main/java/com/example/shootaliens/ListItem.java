package com.example.shootaliens;

public class ListItem {

    private String mName;
    private int mScore;

    public ListItem(String name, int score){
        mName = name;
        mScore = score;
    }

    public String getName() {
        return mName;
    }

    public String getScore()
    {
        return Integer.toString(mScore);
    }

}
