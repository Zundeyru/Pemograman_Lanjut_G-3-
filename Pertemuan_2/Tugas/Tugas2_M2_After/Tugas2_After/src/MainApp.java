public class MainApp{

    // ==== Demo ====
    public static void main(String[] args) {
        TaxiTicket ticket = new TaxiTicket(
                "Alice", "Downtown", "Airport",
                50.0, 30.0, 60.0
        );

        ticket.detailedInfo(); // full info

        ticket.checkStatus();           // status
        ticket.displayRoute();           // route
        ticket.displayEstimatedDuration();          // estimated duration

        // simulate slowing down and speeding up
        ticket.slowDown(20);
        ticket.speedUp(15);
    }
}
