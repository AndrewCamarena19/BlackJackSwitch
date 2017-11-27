package com.example.hsport.blackjackswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity {
    Button HitBtn;
    Button StayBtn;
    Button DoubleBtn;
    Button SwitchBtn;
    Button SplitBtn;
    Button BetBtn;
    Button Yes;
    Button No;
    ToggleButton TGCount;
    ArrayList<TextView> Hands = new ArrayList();
    ArrayList<TextView> HandsValue = new ArrayList();
    ArrayList<ImageView> DealerImages = new ArrayList();
    ArrayList<FrameLayout> HandImages = new ArrayList();
    TextView DealerHand;
    TextView Money;
    TextView Insurance;
    TextView CountText;
    TextView DealerValue;
    EditText ToBet;
    Game game;
    Boolean NotBusted = false;
    Integer currentSplit = 1;
    Double DefaultMonies;
    Double bet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        if (savedInstanceState == null) {
            DefaultMonies = getIntent().getDoubleExtra("StartingStack", 1000.00);
            game = new Game(3, DefaultMonies, getBaseContext());
        }
        else
            game = savedInstanceState.getParcelable("Game");
        InitScreen();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("Game", game);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //TODO bust then blackjack not picking up second hand blackjack
    //TODO bust on a hand then push 22 not treating bust as loss first
    //TODO new background on play activity
    //TODO Rules Activity
    //TODO Splash background
    //TODO Number of Decks add intent
    public void AddCardDisplay(ImageView card, String toAdd) {
        card.setVisibility(View.VISIBLE);
        int id = getResources().getIdentifier(toAdd, "drawable", getPackageName());
        card.setImageResource(id);

    }

    private void ResetCards() {
        for (int i = 0; i < HandImages.size(); i++) {
            for (int k = 0; k < 6; k++) {
                HandImages.get(i).getChildAt(k).setVisibility(View.INVISIBLE);
            }
        }
        for (int i = 0; i < 6; i++)
            DealerImages.get(i).setVisibility(View.INVISIBLE);

    }

    private void InitScreen() {
        //create a count label for current count game.getcount
        HitBtn = (Button) findViewById(R.id.BtnHit);
        BetBtn = (Button) findViewById(R.id.BtnBet);
        StayBtn = (Button) findViewById(R.id.BtnStay);
        DoubleBtn = (Button) findViewById(R.id.BtnDouble);
        SwitchBtn = (Button) findViewById(R.id.BtnSwitch);
        SplitBtn = (Button) findViewById(R.id.BtnSplit);
        Yes = (Button) findViewById(R.id.BtnYes);
        No = (Button) findViewById(R.id.BtnNo);
        TGCount = (ToggleButton) findViewById(R.id.CurrentCountTG);
        Insurance = (TextView) findViewById(R.id.TVInsurance);

        GameBtnsOff();
        Yes.setVisibility(View.GONE);
        No.setVisibility(View.GONE);
        Insurance.setVisibility(View.GONE);


        ToBet = (EditText) findViewById(R.id.ToBet);
        DealerHand = (TextView) findViewById(R.id.DealerHand);
        CountText = (TextView) findViewById(R.id.CurrentCountLabel);
        Money = (TextView) findViewById(R.id.Money);
        DealerValue = (TextView) findViewById(R.id.DealerValue);
        Money.setText("Current Money: $ " + game.getPlayer().getMoney().toString());

        HandsValue.add((TextView) findViewById(R.id.Hand1Value));
        HandsValue.add((TextView) findViewById(R.id.Hand2Value));
        HandsValue.add((TextView) findViewById(R.id.SplitHand1Value));
        HandsValue.add((TextView) findViewById(R.id.SplitHand2Value));
        HandsValue.add((TextView) findViewById(R.id.SplitHand3Value));

        DealerImages.add((ImageView) findViewById(R.id.DealerCard1));
        DealerImages.add((ImageView) findViewById(R.id.DealerCard2));
        DealerImages.add((ImageView) findViewById(R.id.DealerCard3));
        DealerImages.add((ImageView) findViewById(R.id.DealerCard4));
        DealerImages.add((ImageView) findViewById(R.id.DealerCard5));
        DealerImages.add((ImageView) findViewById(R.id.DealerCard6));

        HandImages.add((FrameLayout) findViewById(R.id.Hand1Cards));
        HandImages.add((FrameLayout) findViewById(R.id.Hand2Cards));
        HandImages.add((FrameLayout) findViewById(R.id.Hand3Cards));
        HandImages.add((FrameLayout) findViewById(R.id.Hand4Cards));
        HandImages.add((FrameLayout) findViewById(R.id.Hand5Cards));


        Hands.add((TextView) findViewById(R.id.Hand1));
        Hands.add((TextView) findViewById(R.id.Hand2));
        Hands.add((TextView) findViewById(R.id.SplitHand1));
        Hands.add((TextView) findViewById(R.id.SplitHand2));
        Hands.add((TextView) findViewById(R.id.SplitHand3));

        for (int i = 2; i < Hands.size(); i++) {
            Hands.get(i).setVisibility(View.INVISIBLE);
            HandsValue.get(i).setVisibility(View.INVISIBLE);
        }

        TGCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CountText.setText("Count: " + game.getCount());
                if (b)
                    CountText.setVisibility(View.VISIBLE);
                else
                    CountText.setVisibility(View.INVISIBLE);

            }
        });


        HitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hand h = game.getPlayer().getHands().get(game.getCurrHand());
                game.Hit(h, "Player");
                HandsValue.get(h.getHandNum()).setText(game.CheckHand(h, "Player"));
                Hands.get(h.getHandNum()).setText(h.getCards());
                AddCardDisplay((ImageView) HandImages.get(h.getHandNum()).getChildAt(h.getHandSize() - 1), h.getHand().get(h.getHandSize() - 1).toString());
                DealerCheck();
                SwitchBtn.setEnabled(false);

            }
        });

        SplitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPlayer().getHands().size() < 5) {
                    Hand h = game.getPlayer().getHands().get(game.getCurrHand());
                    if (h.canSplit() && game.getPlayer().getMoney() > h.getBet()) {
                        SwitchBtn.setEnabled(false);
                        game.Split(h);
                        HandsValue.get(h.getHandNum()).setText(game.CheckHand(h, "Player"));
                        Hands.get(h.getHandNum()).setText(h.getCards());
                        AddCardDisplay((ImageView) HandImages.get(h.getHandNum()).getChildAt(0), h.getHand().get(0).toString());
                        AddCardDisplay((ImageView) HandImages.get(h.getHandNum()).getChildAt(1), h.getHand().get(1).toString());

                        Hands.get(++currentSplit).setVisibility(View.VISIBLE);
                        HandsValue.get(currentSplit).setVisibility(View.VISIBLE);

                        h = game.getPlayer().getHands().get(currentSplit);
                        HandImages.get(h.getHandNum()).setVisibility(View.VISIBLE);
                        AddCardDisplay((ImageView) HandImages.get(h.getHandNum()).getChildAt(0), h.getHand().get(0).toString());
                        AddCardDisplay((ImageView) HandImages.get(h.getHandNum()).getChildAt(1), h.getHand().get(1).toString());
                        HandsValue.get(currentSplit).setText(game.CheckHand(h, "Player"));
                        Hands.get(currentSplit).setText(h.getCards());

                        Money.setText("Current Money: $ " + game.getPlayer().getMoney());
                        DealerCheck();
                    } else
                        game.MakeToast("Cannot Split when cards are different or insufficient funds");
                } else
                    game.MakeToast("Cannot have more than 5 hands");
            }
        });

        SwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.Switch(game.getPlayer().getHands());
                Hand h1 = game.getPlayer().getHands().get(game.getCurrHand());
                Hand h2 = game.getPlayer().getHands().get(game.getCurrHand() + 1);
                HandsValue.get(h1.getHandNum()).setText(game.CheckHand(h1, "Player"));
                HandsValue.get(h2.getHandNum()).setText(game.CheckHand(h2, "Player"));
                AddCardDisplay((ImageView) HandImages.get(h1.getHandNum()).getChildAt(0), h1.getHand().get(0).toString());
                AddCardDisplay((ImageView) HandImages.get(h1.getHandNum()).getChildAt(1), h1.getHand().get(1).toString());
                AddCardDisplay((ImageView) HandImages.get(h2.getHandNum()).getChildAt(0), h2.getHand().get(0).toString());
                AddCardDisplay((ImageView) HandImages.get(h2.getHandNum()).getChildAt(1), h2.getHand().get(1).toString());
                Hands.get(h1.getHandNum()).setText(h1.getCards());
                Hands.get(h2.getHandNum()).setText(h2.getCards());
                SwitchBtn.setEnabled(false);
            }
        });

        DoubleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hand h = game.getPlayer().getHands().get(game.getCurrHand());
                if (h.getHandSize() <= 2) {
                    game.Double(h, "Player");
                    HandsValue.get(h.getHandNum()).setText(game.CheckHand(h, "Player"));
                    Hands.get(h.getHandNum()).setText(h.getCards());
                    AddCardDisplay((ImageView) HandImages.get(h.getHandNum()).getChildAt(h.getHandSize() - 1), h.getHand().get(h.getHandSize() - 1).toString());
                    DealerCheck();
                    SwitchBtn.setEnabled(false);
                    Money.setText("Current Money: $ " + game.getPlayer().getMoney());
                } else
                    game.MakeToast("Cannot Double after a hit");
            }
        });

        StayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hand h1 = game.getPlayer().getHands().get(game.getCurrHand());
                game.Stay(h1);
                DealerCheck();
                SwitchBtn.setEnabled(false);
            }
        });

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPlayer().getMoney() < game.getPlayer().getHands().get(0).getBet() / 2) {
                    game.MakeToast("Not enough money to get insurance. Insurance is half your bet");
                } else {
                    game.MakeToast("Taking insurance for " + game.getPlayer().getHands().get(0).InsuranceValue() + " on both hands");
                    game.getPlayer().setMoney(game.getPlayer().getMoney() - (game.getPlayer().getHands().get(0).InsuranceValue() * 2));
                    game.getPlayer().setHasInsurance(true);
                    Money.setText("Current Money: $ " + game.getPlayer().getMoney().toString());
                }
                InsuranceAction();

            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.MakeToast("No insurance taken");
                game.getPlayer().setHasInsurance(false);
                InsuranceAction();
            }
        });

        BetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bet = 0.0;
                try {
                    bet = Double.parseDouble((ToBet.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    game.MakeToast("Enter a bet value in the bottom right corner");
                }
                if (bet != 0.0) {
                    if (game.getPlayer().getMoney() < 2) {
                        game.MakeToast("Not enough money, reloading to starting stack");
                        game.getPlayer().setMoney(DefaultMonies);
                    } else if (game.getPlayer().getMoney() < (bet * 2)) {
                        game.MakeToast("Not enough money to place 2 bets of that size");
                    } else {
                        GameBtnsOn();
                        Toast.makeText(getBaseContext(), "Making Bets", Toast.LENGTH_SHORT).show();
                        for (int i = 2; i < Hands.size(); i++) {
                            Hands.get(i).setVisibility(View.INVISIBLE);
                            HandsValue.get(i).setVisibility(View.INVISIBLE);
                        }
                        StartGame();
                    }
                }
            }
        });
    }

    public void DealerCheck() {
        if (game.getCurrHand() == game.getPlayer().getHands().size()) {
            for (Hand h : game.getPlayer().getHands()) {
                if (!NotBusted)
                    NotBusted = (!h.isBusted() && !h.isHasBlackJack());
            }
            if (NotBusted) {
                DealerHand.setText(game.getDealer().getHand().getCards());
                game.DealerPlay();
                DealerValue.setText(game.CheckHand(game.getDealer().getHand(), "Dealer"));
                DealerHand.setText(game.getDealer().getHand().getCards());
                AddCardDisplay(DealerImages.get(1), game.getDealer().getHand().getHand().get(0).toString());
                AddCardDisplay(DealerImages.get(0), game.getDealer().getHand().getHand().get(1).toString());
                for (int i = 2; i < game.getDealer().getHand().getHand().size(); i++) {
                    AddCardDisplay(DealerImages.get(i), game.getDealer().getHand().getHand().get(i).toString());

                }
                if (game.getDealer().getHand().getValue() == 22) {
                    game.MakeToast("All bets push on dealer having 22");
                }
                for (Hand h : game.getPlayer().getHands()) {
                    if (game.getDealer().getHand().getValue() == 22 && !h.isHasBlackJack()) {
                        game.getPlayer().changeMoney(h.getBet());
                    } else if ((h.getValue() > game.getDealer().getHand().getValue() && !h.isBusted()) || (game.getDealer().getHand().isBusted() && !h.isBusted() || h.isHasBlackJack())) {
                        Hands.get(h.getHandNum()).setText("Win: " + h.getBet());
                        game.getPlayer().changeMoney(h.getBet() * 2);
                    } else if (h.getValue() < game.getDealer().getHand().getValue() || h.isBusted()) {
                        Hands.get(h.getHandNum()).setText("Lose: " + h.getBet());
                    } else {
                        Hands.get(h.getHandNum()).setText("Push");
                        game.getPlayer().changeMoney(h.getBet());
                    }
                }
            }
            GameBtnsOff();
            Money.setText("Current Money: $ " + game.getPlayer().getMoney().toString());
        }
    }

    public void StartGame() {
        currentSplit = 1;
        ResetCards();
        if (bet > 0) {
            if (game.GameInit(bet)) {
                Money.setText("Current Money: $ " + game.getPlayer().getMoney());
                for (int i = 0; i < game.getPlayer().getHands().size(); i++) {
                    Hands.get(i).setText(game.getPlayer().getHands().get(i).getCards());
                    HandsValue.get(i).setText(game.CheckHand(game.getPlayer().getHands().get(i), "Player"));
                }
                for (int i = 0; i < 2; i++) {
                    for (int k = 0; k < 2; k++)
                        AddCardDisplay((ImageView) HandImages.get(i).getChildAt(k), game.getPlayer().getHands().get(i).getHand().get(k).toString());
                }
                DealerHand.setText(game.getDealer().getHand().getUpCard().toString());
                DealerValue.setText(game.getDealer().getHand().getUpCard().getRank());
                AddCardDisplay(DealerImages.get(1), game.getDealer().getHand().getHand().get(0).toString());
                AddCardDisplay(DealerImages.get(0), "back");
                CheckBlackJack();
                Hand h = game.getPlayer().getHands().get(0);
                if (h.isHasBlackJack()) {
                    game.getPlayer().changeMoney(h.getBet() * 2);
                    game.Stay(h);
                    DealerCheck();
                }
                CountText.setText("Count: " + game.getCount());
            }
        }
    }

    public void CheckBlackJack() {
        if (game.getDealer().getHand().getValue() == 21 && !game.getDealer().getHand().getUpCard().getRank().equals("A")) {
            game.MakeToast("Dealer has Blackjack!");
            game.getDealer().setBlackJack(true);
            AddCardDisplay(DealerImages.get(1), game.getDealer().getHand().getHand().get(0).toString());
            AddCardDisplay(DealerImages.get(0), game.getDealer().getHand().getHand().get(1).toString());
            GameBtnsOff();
        }
        if (game.getDealer().getHand().getUpCard().getRank().equals("A")) {
            AskInsurance();
        }
    }

    private void GameBtnsOn() {
        HitBtn.setEnabled(true);
        StayBtn.setEnabled(true);
        DoubleBtn.setEnabled(true);
        SwitchBtn.setEnabled(true);
        SplitBtn.setEnabled(true);
        BetBtn.setEnabled(false);
    }

    private void GameBtnsOff() {
        HitBtn.setEnabled(false);
        StayBtn.setEnabled(false);
        DoubleBtn.setEnabled(false);
        SwitchBtn.setEnabled(false);
        SplitBtn.setEnabled(false);
        BetBtn.setEnabled(true);
    }

    public void AskInsurance() {
        //Add two pop up buttons for insurance
        //Yes or No button press
        //insurance = cin.nextLine();
        Yes.setVisibility(View.VISIBLE);
        No.setVisibility(View.VISIBLE);
        Insurance.setVisibility(View.VISIBLE);
        HitBtn.setEnabled(false);
        StayBtn.setEnabled(false);
        DoubleBtn.setEnabled(false);
        SwitchBtn.setEnabled(false);
        SplitBtn.setEnabled(false);
        BetBtn.setEnabled(false);
    }

    public void InsuranceAction() {
        if (game.getDealer().getHand().getValue() == 21) {
            game.getDealer().setBlackJack(true);
            game.MakeToast("Dealer has a blackjack");
            AddCardDisplay(DealerImages.get(1), game.getDealer().getHand().getHand().get(0).toString());
            AddCardDisplay(DealerImages.get(0), game.getDealer().getHand().getHand().get(1).toString());
            if (game.getPlayer().isHasInsurance()) {
                game.getPlayer().changeMoney(game.getPlayer().getHands().get(0).InsuranceValue() * 6);
                game.MakeToast("Player wins: " + game.getPlayer().getHands().get(0).InsuranceValue() * 6 + " from insurance bet.");
            }
            GameBtnsOff();

        } else {
            game.MakeToast("Dealer does not have BlackJack");
            GameBtnsOn();
        }
        game.getPlayer().setHasInsurance(false);
        Yes.setVisibility(View.GONE);
        No.setVisibility(View.GONE);
        Insurance.setVisibility(View.GONE);
        Money.setText("Current Money : $ " + game.getPlayer().getMoney());

    }
}
