package application;

public class Station {

    private int ID;
    private int number;
    private String destiny;
    private String departureTime;
    private String arrivalTime;
    private int amount;

    public Station(int ID, int number, String destiny, String departureTime, String arrivalTime, int amount) {
        this.ID = ID;
        this.number = number;
        this.destiny = destiny;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.amount = amount;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public int getNumber() {
        return number;
    }

    public String getDestiny() {
        return destiny;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "STATION{" +
                "ID=" + ID +
                ", number=" + number +
                ", destiny='" + destiny + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", amount=" + amount +
                '}';
    }
}
