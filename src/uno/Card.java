package uno;

public class Card {
    private String color;
    private String number;
    private Boolean specialCard;

    public Card(String color, String number, Boolean specialCard) {
        this.color = color;
        this.number = number;
        this.specialCard = specialCard;
    }

    public String getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public Boolean getSpecialCard() {
        return specialCard;
    }

    public String getCardFullName() {
        return this.color + " " + this.number;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSpecialCard(Boolean specialCard) {
        this.specialCard = specialCard;
    }

}
