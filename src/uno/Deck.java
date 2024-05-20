package uno;

import java.util.*;

class Deck {
    private static Stack<Card> deck = new Stack<Card>(); //a huzopakli
    private static Stack<Card> onTable = new Stack<Card>(); //az asztalra lerakott lapok

    public Stack<Card> getDeck() {
        return deck;
    } // a huzopaklinak a getterje

    public Stack<Card> getOnTable() {
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
        
        for(Card i : this.deck){
            if(i.getSpecialCard() == false){
                normalCards.add(i);
            }
        }
        this.onTable.add(normalCards.get(new Random().nextInt(0, normalCards.size())));
    }
  
    public static void setDeck(Stack<Card> deck) {
        Deck.deck = deck;
    }

    public void setOnTable(Stack<Card> onTable) {
        this.onTable = onTable;
    }

    public static void reloadDeck(){
        Stack<Card> cards = new Stack<Card>();
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
