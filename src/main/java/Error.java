import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Error
{
    private ArrayList<Review> reviews = new ArrayList<>();
    private double currentConfidence = 50;
    private int estimatedDuration; // in minutes
    private double urgencyMultiplier = 1.0;
    final private String dateCreated;

    private String title = "";
    private String description = "";
    private String pointer = "";

    String tags;

    // constructors
    public Error(String t, String d, String p, String tg, int eD)
    {
        title = t;
        description = d;
        pointer = p;
        estimatedDuration = eD;
        dateCreated = LocalDateTime.now().toString();
        tags = dateCreated + ", " + tg;
    }

    public Error()
    {
        dateCreated = LocalDateTime.now().toString();
        tags = dateCreated;
    }

    // getters and setters
    public void addReview(Review review)
    {
        reviews.add(review);
        currentConfidence = review.getConfidence();
    }

    public void setUrgencyMultiplier(double multiplier)
    {
        urgencyMultiplier = multiplier;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getPointer()
    {
        return pointer;
    }

    public Review getLastReview()
    {
        return reviews.getLast();
    }

    public int getEstimatedDuration()
    {
        return estimatedDuration;
    }

    public double getUrgencyMultiplier()
    {
        return urgencyMultiplier;
    }

    // other methods
    public double calculateScore()
    {
        updateConfidence();
        return (100.0 - currentConfidence) * urgencyMultiplier;
    }

    private double calculateHoursPassed(LocalDateTime timeA, LocalDateTime timeB)
    {
        return Duration.between(timeA, timeB).toHours();
    }

    private double calculateDecayPerHour()
    {
        double totalHours = 0;
        double totalDecay = 0;

        for (int i = 1; i < reviews.size(); i ++)
        {
            totalHours += calculateHoursPassed(LocalDateTime.parse(reviews.get(i - 1).getDate()),
                    LocalDateTime.parse(reviews.get(i).getDate()));
            totalDecay += reviews.get(i - 1).getConfidence() - reviews.get(i).getConfidence();
        }

        // avoid division by 0 exception
        if (totalHours == 0)
        {
            totalDecay = 50.0;
            totalHours = 24.0;
        }

        return totalDecay / totalHours;
    }

    private void updateConfidence()
    {
        double timePassed = calculateHoursPassed(LocalDateTime.parse(reviews.getLast().getDate()), LocalDateTime.now());
        double halfLife = Math.clamp(50.0 / calculateDecayPerHour(), 1.0, 720.0);

        currentConfidence = currentConfidence * Math.pow(0.5, (timePassed / halfLife));
    }

    public double returnNewConfidence()
    {
        double timePassed = calculateHoursPassed(LocalDateTime.parse(reviews.getLast().getDate()), LocalDateTime.now());
        double halfLife = Math.clamp(50.0 / calculateDecayPerHour(), 1.0, 720.0);

        return currentConfidence * Math.pow(0.5, (timePassed / halfLife));
    }

    // error objects are considered equal if they were created at the same time
    public boolean equals(Error error)
    {
        return dateCreated.equals(error.dateCreated);
    }
}
