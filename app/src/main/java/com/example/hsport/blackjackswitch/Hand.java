package com.example.hsport.blackjackswitch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Andy on 6/30/2017.
 */
public class Hand implements Parcelable {

    private ArrayList<Card> hand;
    private Integer Value;
    private Integer Aces;
    private Integer Handnum = 0;
    private Double Bet;
    private boolean isHard;
    private boolean busted;
    private boolean isDone = false;
    private boolean hasBlackJack = false;
    private boolean wasSwitched = false;

    public Integer getHandSize() {
        return hand.size();
    }

    public boolean isWasSwitched() {
        return wasSwitched;
    }

    public void setWasSwitched(boolean wasSwitched) {
        this.wasSwitched = wasSwitched;
    }

    public boolean isHasBlackJack() {
        return hasBlackJack;
    }

    public void setHasBlackJack(boolean hasBlackJack) {
        this.hasBlackJack = hasBlackJack;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setBusted(boolean curr) {
        busted = curr;
    }

    public boolean isBusted() {
        return busted;
    }

    public void setHard(boolean hard) {
        isHard = hard;
    }

    public boolean isHard() {
        return isHard;
    }

    //public void decrementHandNum() {
    //    Handnum--;
   // }

    public void setHandnum(Integer num) {
        Handnum = num;
    }

    //public void incrementHandNum() {
     //   Handnum++;
   // }

    public Integer getHandNum() {
        return Handnum;
    }

    public Integer getAces() {
        return Aces;
    }

    public Hand() {
        hand = new ArrayList();
        Value = 0;
        Aces = 0;
        isHard = true;
        busted = false;
        //Insurance = 0.0;
        Bet = 0.0;
    }

    public Hand(Double bet) {
        hand = new ArrayList();
        Value = 0;
        Aces = 0;
        Bet = bet;
        isHard = true;
        busted = false;
        //Insurance = 0.0;
    }

    public Hand(Double bet, Card split) {
        hand = new ArrayList();
        Value = 0;
        Bet = bet;
        isHard = true;
        busted = false;
        //Insurance = 0.0;
        Aces = 0;
        AddCard(split);
    }

    public void DoubleBet() {
        Bet = Bet * 2;
    }

    public Double InsuranceValue() {
        return Bet / 2;
    }

    public Integer getValue() {
        return Value;
    }

    public Double getBet() {
        return Bet;
    }

    public Card getUpCard() {
        return hand.get(0);
    }

    public boolean canSplit() {
        return hand.get(0).getRank().equals(hand.get(1).getRank());
    }

    public void removeCard() {
        switch (hand.get(0).getRank()) {
            case "J":
            case "T":
            case "Q":
            case "K":
                Value -= 10;
                break;
            case "A":
                Value -= 1;
                break;
            default:
                Value -= Integer.parseInt(hand.get(0).getRank());
                break;
        }
        hand.remove(0);
    }

    public StringBuilder getCards() {
        StringBuilder sb = new StringBuilder();
        for (Card x : hand) {
            sb.append(x.getRank() + " ");
        }
        return sb;
    }

    public void AddCard(Card card) {
        hand.add(card);
        switch (card.getRank()) {
            case "J":
            case "T":
            case "Q":
            case "K":
                Value += 10;
                break;
            case "A":
                Value += 11;
                Aces++;
                break;
            default:
                Value += Integer.parseInt(card.getRank());
                break;
        }
        if (Value > 21 && Aces > 0) {
            Aces--;
            Value -= 10;
        }
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    protected Hand(Parcel in) {
        hand = in.createTypedArrayList(Card.CREATOR);
        isHard = in.readByte() != 0;
        busted = in.readByte() != 0;
        isDone = in.readByte() != 0;
        hasBlackJack = in.readByte() != 0;
        wasSwitched = in.readByte() != 0;
    }

    public static final Creator<Hand> CREATOR = new Creator<Hand>() {
        @Override
        public Hand createFromParcel(Parcel in) {
            return new Hand(in);
        }

        @Override
        public Hand[] newArray(int size) {
            return new Hand[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(hand);
        parcel.writeByte((byte) (isHard ? 1 : 0));
        parcel.writeByte((byte) (busted ? 1 : 0));
        parcel.writeByte((byte) (isDone ? 1 : 0));
        parcel.writeByte((byte) (hasBlackJack ? 1 : 0));
        parcel.writeByte((byte) (wasSwitched ? 1 : 0));
    }
}
