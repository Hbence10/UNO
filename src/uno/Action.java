package uno;

import java.util.*;

public class Action {
    private ArrayList<Card> deck; //Huzopakli
    private ArrayList<Card> onTable; //Az asztalon levo kartyak

    public Action(ArrayList<Card> deck, ArrayList<Card> onTable) {
        this.deck = deck;
        this.onTable = onTable;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public ArrayList<Card> getOnTable() {
        return onTable;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public void setOnTable(ArrayList<Card> onTable) {
        this.onTable = onTable;
    }

    public void checkAction(Player ongoingPlayerPlayer, Player otherPlayerPlayer) { //Adott kort 
        ArrayList<Integer> goodCard = new ArrayList<Integer>(); //A lerakhato kartyak 

        writeInfo(ongoingPlayerPlayer, otherPlayerPlayer); //Kezdeti informaciok kiiratasa

        for (int i = 0; i < ongoingPlayerPlayer.getHand().size(); i++) { //A kezben levo kartyak vizsgalasa, lerakhato egy kartya ha:
            if (ongoingPlayerPlayer.getHand().get(i).getColor().equals("black")) { //ha a kezben van fekete szinu kartya azaz szinvaltos vagy huz fel negyet lap
                goodCard.add(i); //ezt a kartyat hozzaadom a megfelelo kartyak listajahoz
            } else if (ongoingPlayerPlayer.getHand().get(i).getColor().equals(this.onTable.get(this.onTable.size() - 1).getColor()) || ongoingPlayerPlayer.getHand().get(i).getNumber().equals(this.onTable.get(this.onTable.size() - 1).getNumber())) { //ha a kezunkben van ugyanolyan szamu vagy szinu lap mint ami az asztal tetejen van
                goodCard.add(i); //ezt a kartyat hozzaadom a megfelelo kartyak listajahoz
            }
        }

        if (goodCard.size() == 0) { //Ha nem tudok huzni lapot akkor huznom kell
            drag(ongoingPlayerPlayer); //meghivom a huzas methodot
        } else { //Ha meg tudok rakni
            deposit(ongoingPlayerPlayer, goodCard, otherPlayerPlayer); //meghivom a rakas methodot
        }
    }

    public void drag(Player player) { //huzas
        if (this.deck.isEmpty()) {
            reloadDeck();
        }
        player.getHand().add(this.deck.get(this.deck.size() - 1)); //a player kezeben levo lapjaihoz hozzaadjuk a huzo pakli tetejen levo lapot

        System.out.println(player.getName() + " retrieved: " + this.deck.get(this.deck.size() - 1).getCardFullName()); //kiprinteljuk az akciot

        this.deck.remove(this.deck.size() - 1); //a huzopaklibol eltavolitjuk a kihuzott lapot
    }

    public void deposit(Player player, ArrayList<Integer> goodCard, Player otherPlayer) { //rakas
        Card choosenCard = player.getHand().get(goodCard.get(new Random().nextInt(0, goodCard.size()))); //a jo kartyak kozul kivalasztunk random egyer

        this.onTable.add(choosenCard); // "lerakjuk"
        player.getHand().remove(choosenCard); //eltavolitjuk a kezunkbol

        if (choosenCard.getSpecialCard()) { //megviszgaljuk hogy a kartya specialis kartya-e
            specialCard(choosenCard, otherPlayer, player, (player.getName() + " deposit: " + choosenCard.getCardFullName())); // ha igen akkor meghivjuk a specialCard methodot
        } else { // kulonben
            System.out.println(player.getName() + " deposit:" + choosenCard.getCardFullName()); //kiirjuk az akciot
            sayUno(player); //megvizsgalom a sayUno methoddal hogy kell-e mondani unot
        }
    }

    public void sayUno(Player player) { //UNO mondasa
        if (player.getHand().size() == 1) { //ha a jatekosnak egy kartyaja maradt a kezeben
            System.out.println(player.getName() + ": UNO!"); //akkor mondja hogy UNO
        }
    }

    public void handout(Player player) {//kezdo kartya kiosztasa
        for (int i = 0; i < 7; i++) { //het kartya kiosztasa az adott jatekosnak
            int choosenCardIndex = new Random().nextInt(0, this.deck.size());
            player.getHand().add(this.deck.get(choosenCardIndex)); //odaadjuk a jatekosnak
            this.deck.remove(this.deck.get(choosenCardIndex)); //es eltavolitjuk a huzopaklibol
        }
    }

    public boolean checkWin(Player player) { //nyerest vizsgalja
        return player.getHand().isEmpty(); //returnoli hogy nulla van-e a kezunkben
    }

    public void reloadDeck() {  //pakli ujratoltese
        ArrayList<Card> cards = new ArrayList<Card>(); //megfelelo kartyak

        for (int i = this.onTable.size() - 2; i > 1; i--) {
            if (!this.onTable.get(i).getNumber().equals("-")) {  //megnezzuk hogy az adott kartya nem csak szin kartya
                cards.add(this.onTable.get(i)); //ha az akkor a megfelelo kartyakhoz hozzaadjuk
                this.onTable.remove(i); //es kitoroljuk az asztalrol
            } else {
                this.onTable.remove(i); // is kitoroljuk az asztalrol
            }
        }

        Collections.shuffle(cards); //megkeverjuk a paklit

        this.deck = cards; //lementjuk az "uj" paklit

        System.out.println("The deck has been shuffled due to emptiness"); //kiirjuk a valtozast
    }

    public void specialCard(Card specialCard, Player otherPlayer, Player ongoingPlayer, String msg) { //specialis kartyak lekezelese
        switch (specialCard.getNumber()) {
            case "Draw 2":  //huz fel ketto kartya
                System.out.println(msg); //kiiratjuk az akciot
                sayUno(ongoingPlayer); //megvizsgalom a sayUno methoddal hogy kell-e mondani unot
                for (int i = 0; i < 2; i++) {
                    drag(otherPlayer); //felhuzatunk a masik jatekossal 2 kartyat a huzas method segitsegevel
                }

                checkAction(ongoingPlayer, otherPlayer);  // es igy megint a rako jatekos jon
                break;

            case "Wild and Draw 4": //huzz fel negyet es valasz szint kartya
                System.out.println(msg);
                if (!ongoingPlayer.getHand().isEmpty()) { //megvizsgaljuk, hogy nem ez az utolso kartyank, ha nem ez akkor 
                    String choosedColor = chooseGoodColor(ongoingPlayer.getHand()); //kivalasztjuk azt a szint amelyet szeretnenk

                    System.out.println(ongoingPlayer.getName() + " wants " + choosedColor + " color \n"); //kiirjuk az akciot
                    this.onTable.add(new Card(choosedColor, "-", false)); // hozaadunk lenyegeben egy szin kartyar
                    for (int i = 0; i < 4; i++) { //negy kartyat
                        drag(otherPlayer); //huzatunk a masik jatekossal
                    }
                    sayUno(ongoingPlayer); //megvizsgalom a sayUno methoddal hogy kell-e mondani unot
                    checkAction(ongoingPlayer, otherPlayer); // es ujra a jelenlegi jatekos jon
                }
                break;

            case "Wild": //sima szinvalasztas
                System.out.println(msg); //kiirjuk az akciot
                if (!ongoingPlayer.getHand().isEmpty()) {  //ha nem ez az utolso kartyank
                    String choosedColor = chooseGoodColor(ongoingPlayer.getHand()); //kivalasztjuk azt a szint amelyet szeretnenk

                    System.out.println(ongoingPlayer.getName() + " wants " + choosedColor + " color"); //kiirjuk az akciot
                    this.onTable.add(new Card(choosedColor, "-", false));   // hozaadunk lenyegeben egy szin kartyar
                }
                sayUno(ongoingPlayer); //megvizsgalom a sayUno methoddal hogy kell-e mondani unot
                break;
                
            case "Skip": //kimaradas kartya
                System.out.println(msg); //akcio kiiratasa
                System.out.println(otherPlayer.getName() + " is out of the loop"); //kiiratjuk hogy a masik jatekos kimarad
                sayUno(ongoingPlayer); //megvizsgalom a sayUno methoddal hogy kell-e mondani unot
                checkAction(ongoingPlayer, otherPlayer); //es ujra a jelenlegi jatekos jon
                break;
            case "Reverse": //korforditos kartya
                System.out.println(msg); //akcio kiiratasa
                System.out.println(otherPlayer.getName() + " is out of the loop"); //kiiratjuk hogy a masik jatekos kimarad
                sayUno(ongoingPlayer); //megvizsgalom a sayUno methoddal hogy kell-e mondani unot
                checkAction(ongoingPlayer, otherPlayer); //es ujra a jelenlegi jatekos jon
                break;
            default:
        }
    }

    public void writeInfo(Player player1, Player player2) { //informaciok kiiratasa
        ArrayList<String> player1Card = new ArrayList<String>(); //elsojatekos kartyai
        ArrayList<String> player2Card = new ArrayList<String>(); // masodik jatekos kartyai

        player1.getHand().forEach(card -> player1Card.add(card.getCardFullName()));
        player2.getHand().forEach(card -> player2Card.add(card.getCardFullName()));

        System.out.println("\n---------\nINFORMATION");
        System.out.println("\nRemaining card in the deck: " + this.deck.size() + "\n");
        System.out.println(player1.getName() + ": ");
        System.out.println("Cards in his hand: " + player1Card.toString().replace("[", "").replace("]", ""));

        System.out.println("\n" + player2.getName() + ": ");
        System.out.println("Cards in his hand: " + player2Card.toString().replace("[", "").replace("]", ""));

        System.out.println("\nCard on the table: " + this.onTable.get(this.onTable.size() - 1).getCardFullName() + "\n");
    }

    public String chooseGoodColor(ArrayList<Card> cards) { //szinvalasztos method
        List<Card> goodColors = cards.stream().filter(card -> !card.getColor().equals("black")).toList(); // ide taroljuk a megfelelo szineket

        String choosedColor = ""; //a jo szinek kozul valo valasztas

        if (goodColors.size() == 0) {
            ArrayList<String> colors = new ArrayList<String>(Arrays.asList("Green", "Yellow", "Blue", "Red"));
            choosedColor = colors.get(new Random().nextInt(0, 4));
        } else {
            choosedColor = goodColors.get(new Random().nextInt(0, goodColors.size())).getColor(); //a jo szinek kozul valo valasztas
        }

        return choosedColor; //returnoljuk a kivalasztott szint
    }
}