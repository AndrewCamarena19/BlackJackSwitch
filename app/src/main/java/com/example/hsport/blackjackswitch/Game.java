package com.example.hsport.blackjackswitch;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Andy on 6/30/2017.
 */

public class Game implements Parcelable{
    private Context context;
    private Player player;
    private Dealer dealer;
    private Integer numDecks;
    private Integer currHand = 0;
    private Integer Cardnum = 0;
    private Integer TotalHands = 0;
    private Integer Count = 0;
    //private boolean isDoneGame = false;
    private boolean isSplit = false;
    private ArrayList<String> Suits = new ArrayList(Arrays.asList("Clubs", "Diamonds", "Hearts", "Spades"));
    private ArrayList<String> Ranks = new ArrayList(Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"));
    private ArrayList<Card> Shoe = new ArrayList();

    public void MakeToast(String msg) {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean GameInit(Double bet) {
        //Set all prints to toasts
        //all calls to scanner set to buttons

        if (player.getMoney() >= bet * 2 && bet > 0) {
            player.setHands(new ArrayList());
            dealer = new Dealer();
            player.getHands().add(new Hand(bet));
            player.getHands().add(new Hand(bet));
            player.setMoney(player.getMoney() - (bet * 2));

            if (Cardnum >= (Shoe.size() * 5) / 6) {
                Collections.shuffle(Shoe);
                Cardnum = 0;
                Count = 0;
                MakeToast("Shuffling Shoe!");
            }
            TotalHands = 0;
            currHand = 0;
            player.getHands().get(0).setHandnum(TotalHands++);
            Count(Shoe.get(Cardnum));//0
            player.getHands().get(0).AddCard(Shoe.get(Cardnum++)); //0->1
            Count(Shoe.get(Cardnum));//1
            player.getHands().get(0).AddCard(Shoe.get(Cardnum++));//3->4
            Count(Shoe.get(Cardnum));//2
            player.getHands().get(1).setHandnum(TotalHands++);
            player.getHands().get(1).AddCard(Shoe.get(Cardnum++));//1->2
            Count(Shoe.get(Cardnum));//3
            player.getHands().get(1).AddCard(Shoe.get(Cardnum++));
            Count(Shoe.get(Cardnum));//3
            dealer.getHand().AddCard(Shoe.get(Cardnum++));//2->3
            dealer.getHand().AddCard(Shoe.get(Cardnum));
            dealer.getHand().setHandnum(0);
            return true;
        } else {
            MakeToast("Enter a bet amount of max half your stack or greater than 1");
            return false;
        }

    }

   // public boolean getSplit() {
    //    return isSplit;
   // }

    //public void makeSplit(boolean split) {
    //    isSplit = split;
   // }

    public void IncrementHand() {
        currHand++;
    }

    public Integer getCurrHand() {
        return currHand;
    }

    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    //public boolean isGameDone() {
     //   return isDoneGame;
    //}

    public Game(Integer num, Double MoneyStart, Context mContext) {
        context = mContext;
        numDecks = num;
        Init();
        player = new Player();
        player.setMoney(MoneyStart);

    }

    private void Init() {
        for (int i = 0; i < numDecks; i++) {
            for (String suit : Suits)
                for (String rank : Ranks)
                    Shoe.add(new Card(rank, suit));
        }
        Collections.shuffle(Shoe);
    }

    public void Hit(Hand hand, String person) {
        Card curr = Shoe.get(++Cardnum);
        //Remove printline change to animation to card being pulled and added
        //System.out.println("Card pulled: " + Shoe.get(Cardnum) + " on hand: " + hand.getHandNum());
        hand.AddCard(curr);
        Count(curr);
        //remove current count keep on side hide able label
        //System.out.println("Current count:  " + Count);
        if (hand.getValue() > 21) {
            hand.setBusted(true);
            Stay(hand);
        } else if (hand.getValue() == 21) {
            Stay(hand);
        }
    }

    public void Split(Hand hand) {
        if (hand.canSplit() && player.getMoney() >= hand.getBet()) {
            isSplit = hand.canSplit();
            MakeToast("Splitting " + hand.getUpCard().getRank() + "'s");
            Hand h = new Hand(hand.getBet(), hand.getUpCard());
            player.changeMoney(h.getBet() * -1);
            player.getHands().add(h);
            player.getHands().get(player.getHands().size()-1).setHandnum(TotalHands++);
            hand.removeCard();
            Hit(hand, "Player");
            Hit(h, "Player");
        } else {
            MakeToast("Hand must have same cards to split and enough money to cover both bets");
        }
    }

    public void Double(Hand hand, String person) {
        if (player.getMoney() >= hand.getBet()) {
            player.changeMoney(hand.getBet() * -1);
            hand.DoubleBet();
            MakeToast("Player doubling bet and taking last card");
            Card curr = Shoe.get(++Cardnum);
            hand.AddCard(curr);
            Count(curr);
            if(hand.getValue() > 21)
                hand.setBusted(true);
            Stay(hand);
            if (hand.getAces() > 0) {
                hand.setHard(false);
            } else {
                hand.setHard(true);
            }
        } else {
            MakeToast("Not enough Money to Double Down");
        }
    }

    public void Switch(ArrayList<Hand> hands) {
        if (!hands.get(0).isDone() && !hands.get(0).isWasSwitched() && !hands.get(1).isWasSwitched() && !hands.get(1).isHasBlackJack()) {
            Card swt = hands.get(0).getUpCard();
            Card swt2 = hands.get(1).getUpCard();
            MakeToast("Switching " + swt.toString() + " with " + swt2.toString());
            hands.get(0).removeCard();
            hands.get(1).removeCard();

            hands.get(0).AddCard(swt2);
            hands.get(1).AddCard(swt);

            hands.get(0).setWasSwitched(true);
            hands.get(1).setWasSwitched(true);

        } else {
            MakeToast("Cannot switch already switched hands or after first hand action");
        }
    }

    public void Stay(Hand hand) {
        if(!hand.isBusted() && !hand.isHasBlackJack()) {
            MakeToast("Player Stays on: " + hand.getValue());
        }
        else if(hand.getValue() > 21)
            MakeToast("Player Busts with: " + hand.getValue());
        hand.setDone(true);
        IncrementHand();
    }

/*    public void doAction(String action, Hand hand) {
        //change from string action to button presses
        switch (action.toLowerCase()) {
            case "hit":
                Hit(hand, "Player");
                break;
            case "split":
                Split(hand);
                break;
            case "double":
                Double(hand, "Player");
                break;
            case "switch":
                Switch(player.getHands());
                break;
            case "stay":
                Stay(hand);
                break;
            default:
                System.out.println("Select a correct action");
        }
    }*/

    public String CheckHand(Hand h, String person) {
        if (h.getHandSize() < 2) {
            Hit(h, person);
        }
        if (h.getValue() == 21 && !isSplit && h.getHandSize() == 2 && !h.isWasSwitched()) {
            MakeToast(person + " has BlackJack on hand " + (h.getHandNum() + 1));
            h.setHasBlackJack(true);
            h.setDone(true);
            return "Winner";
        } else {
            if (h.getAces() > 0) {
                //this will be on the label below the hand
                h.setHard(false);
                return (": Soft " + h.getValue());
            } else {
                h.setHard(true);
                return (": Hard " + h.getValue());
            }
        }
    }

    public void DealerPlay() {
        while (!dealer.getHand().isDone()) {
            CheckHand(dealer.getHand(), "Dealer");
            while (dealer.getHand().getValue() < 17 || (!dealer.getHand().isHard() && dealer.getHand().getValue() == 17)) {
                CheckHand(dealer.getHand(), "Dealer");
                Card curr = Shoe.get(++Cardnum);
                dealer.getHand().AddCard(curr);
                Count(curr);
            }
            if(dealer.getHand().getValue() > 22)
                dealer.getHand().setBusted(true);
            dealer.getHand().setDone(true);
        }
    }

    private void Count(Card card) {
        switch (card.getRank()) {
            case "J":
            case "T":
            case "Q":
            case "K":
            case "A":
                Count -= 1;
                break;
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
                Count += 1;
                break;
            default:
                break;
        }
    }

    public Integer getCount() {
        return Count;
    }


    protected Game(Parcel in) {
        player = in.readParcelable(Player.class.getClassLoader());
        dealer = in.readParcelable(Dealer.class.getClassLoader());
        isSplit = in.readByte() != 0;
        Suits = in.createStringArrayList();
        Ranks = in.createStringArrayList();
        Shoe = in.createTypedArrayList(Card.CREATOR);
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(player, i);
        parcel.writeParcelable(dealer, i);
        parcel.writeByte((byte) (isSplit ? 1 : 0));
        parcel.writeStringList(Suits);
        parcel.writeStringList(Ranks);
        parcel.writeTypedList(Shoe);
    }
}
