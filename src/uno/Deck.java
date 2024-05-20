package uno;

import java.util.*;
import java.awt.*;
import javax.swing.*;

class Deck {

    private static ArrayList<Card> deck = new ArrayList<Card>(); //a huzopakli
    private static ArrayList<Card> onTable = new ArrayList<Card>(); //az asztalra lerakott lapok

    public ArrayList<Card> getDeck() {
        return deck;
    } // a huzopaklinak a getterje

    public ArrayList<Card> getOnTable() {
        return onTable;
    }// a huzopaklinak a getterje
    
    
    public void uppLoadDeck(Card card) {//ezzel a methoddal toltjuk fel a huzopaklit
        this.deck.add(card);
        if (this.deck.size() == 112) {
            Collections.shuffle(deck); //ha teljes lesz a pakli akkor megkeverjuk a paklit
        }
    }
    
    public void selectStarterCard(){ //kezdo kartya kivalasztasa
        ArrayList<Card> normalCards  = new ArrayList<Card>();
        
        for(Card i : deck){
            if(i.getSpecialCard() == false){
                normalCards.add(i);
            }
        }
        
        onTable.add(normalCards.get(new Random().nextInt(0, normalCards.size())));
    }

  
    public static void setDeck(ArrayList<Card> deck) {
        Deck.deck = deck;
    }

    public void setOnTable(ArrayList<Card> onTable) {
        this.onTable = onTable;
    }

    public static void reloadDeck(){
        ArrayList<Card> cards = new ArrayList<Card>();
        
        for(int i = onTable.size()-2; i> 1; i--){
            if(!onTable.get(i).getNumber().equals("-")){
                cards.add(onTable.get(i));
                onTable.remove(i);
            } else{
                onTable.remove(i);
            }
        }
        
        Collections.shuffle(cards);
        
        deck = cards;
    }
    
    
}
