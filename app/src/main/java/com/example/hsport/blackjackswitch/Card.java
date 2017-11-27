package com.example.hsport.blackjackswitch;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Andy on 6/30/2017.
 */

public class Card implements Parcelable{
    private String Rank;
    private String Suit;
    private String Color;

    public Card(String rank, String suit) {
        Rank = rank;
        Suit = suit;
        if (suit.equals("Clubs") || suit.equals("Spades"))
            Color = "Black";
        else
            Color = "Red";
    }

    @Override
    public String toString() {
        return Suit.toLowerCase().charAt(0) + Rank.toLowerCase();
    }

    public String getRank() {
        return Rank;
    }

    public String getSuit() {
        return Suit;
    }

    public String getColor() {
        return Color;
    }

    protected Card(Parcel in) {
        Rank = in.readString();
        Suit = in.readString();
        Color = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Rank);
        parcel.writeString(Suit);
        parcel.writeString(Color);
    }
}
