public class Ticket {
    public static final double TAX_RATE = 0.10;
    // ==== Fields ====
    public String passengerName;
    public String startLocation;
    public String destination;
    public double price;

    public Ticket(String passengerName, String startLocation, String destination, double price) {
        this.passengerName = passengerName;
        this.startLocation = startLocation;
        this.destination = destination;
        this.price = price;
    }

    // Display basic passenger & trip info
    public void displayInfo() {
        System.out.println("Passenger Name : " + passengerName);
        System.out.println("Start Location : " + startLocation);
        System.out.println("Destination    : " + destination);
        System.out.println("Price          : " + price);
        System.out.println("Final Price    : " + calculateFinalPrice()); // include 10% tax
    }

    private double calculateFinalPrice() {
        return price + (price * TAX_RATE);
    }
}
