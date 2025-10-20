public class TaxiTicket extends Ticket implements Operational {

    private double duration; // in minutes
    private double speed;    // in km/h

    private static final double MIN_SPEED = 5.0;
    private static final double MAX_SPEED = 100.0;

    // ==== Constructor ====
    public TaxiTicket(String passengerName,
                      String startLocation,
                      String destination,
                      double price,
                      double duration,
                      double speed) {
        super(passengerName, startLocation, destination, price);
        this.duration = duration;
        this.speed = speed;
    }

    // ==== Methods ====

    // Check taxi status
    @Override
    public void checkStatus() {
        System.out.println("Your taxi is heading to " + destination);
    }

    // Display estimated travel duration
    @Override
    public void displayEstimatedDuration() {
        System.out.println("Estimated travel duration: " + duration + " minutes");
    }

    // Display route
    @Override
    public void displayRoute() {
        System.out.println("Route: " + startLocation + " -> " + destination);
    }

    // Slow down the taxi
    public void slowDown(double speedReduction) {
        speed -= speedReduction;
        if (speed < MIN_SPEED) speed = MIN_SPEED;

        // Simple effect: slower speed increases duration a bit
        duration += speedReduction * 0.5;

        System.out.println("Taxi slowed down! Current speed: " + speed + " km/h");
    }

    // Speed up the taxi
    public void speedUp(double speedIncrease) {
        speed += speedIncrease;
        if (speed > MAX_SPEED) speed = MAX_SPEED;

        System.out.println("Taxi sped up! Current speed: " + speed + " km/h");
    }

    // Display full info including duration and speed
    public void detailedInfo() {
        displayInfo();
        System.out.println("Duration       : " + duration + " minutes");
        System.out.println("Speed          : " + speed + " km/h");
    }

}

