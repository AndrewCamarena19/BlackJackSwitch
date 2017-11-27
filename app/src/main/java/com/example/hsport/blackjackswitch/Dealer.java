package com.example.hsport.blackjackswitch;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Andy on 6/30/2017.
 */

public class Dealer implements Parcelable {
    private Hand hand;
    private boolean hasBlackJack;

    public Dealer() {
        hand = new Hand();
        hasBlackJack = false;
    }

    protected Dealer(Parcel in) {
        hand = in.readParcelable(Hand.class.getClassLoader());
        hasBlackJack = in.readByte() != 0;
    }

    public static final Creator<Dealer> CREATOR = new Creator<Dealer>() {
        @Override
        public Dealer createFromParcel(Parcel in) {
            return new Dealer(in);
        }

        @Override
        public Dealer[] newArray(int size) {
            return new Dealer[size];
        }
    };

    public void setBlackJack(boolean hasIt) {
        hasBlackJack = hasIt;
    }

    public boolean BlackJack() {
        return hasBlackJack;
    }

    public Hand getHand() {
        return hand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(hand, i);
        parcel.writeByte((byte) (hasBlackJack ? 1 : 0));
    }
}
