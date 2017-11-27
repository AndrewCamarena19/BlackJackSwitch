package com.example.hsport.blackjackswitch;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Andy on 6/30/2017.
 */
public class Player implements Parcelable {

    private boolean hasInsurance = false;
    private ArrayList<Hand> hands;
    private Double Money;

    public boolean isHasInsurance() {
        return hasInsurance;
    }

    public Player() {
        hasInsurance = false;
        Money = 0.0;
        hands = new ArrayList();
    }

    public void setHasInsurance(boolean hasInsurance) {
        this.hasInsurance = hasInsurance;
    }

    public ArrayList<Hand> getHands() {
        return hands;
    }

    public void setHands(ArrayList<Hand> hands) {
        this.hands = hands;
    }

    public Double getMoney() {
        return Money;
    }

    public void setMoney(Double money) {
        Money = money;
    }

    public void changeMoney(Double change) {
        Money += change;
    }

    protected Player(Parcel in) {
        hasInsurance = in.readByte() != 0;
        hands = in.createTypedArrayList(Hand.CREATOR);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (hasInsurance ? 1 : 0));
        parcel.writeTypedList(hands);
    }
}
