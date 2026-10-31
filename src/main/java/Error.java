import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Error
{
    private ArrayList<Review> reviews = new ArrayList<>();
    private double currentConfidence = 50;
    private int estimatedDuration; // in minutes
    private double urgencyMultiplier = 1.0;
    private String dateCreated;

    private String title = "";
    private String description = "";
    private String pointer = "";

    private String tags;

    private boolean appearsInGreedyFill = true;

    // constructors
    public Error(String t, String d, String p, String tg, int eD, boolean a)
    {
        title = t;
        description = d;
        pointer = p;
        estimatedDuration = eD;
        dateCreated = LocalDateTime.now().toString();
        tags = dateCreated + ", " + tg;
        appearsInGreedyFill = a;
    }

    public Error()
    {
        dateCreated = LocalDateTime.now().toString();
        tags = dateCreated;
    }

    public Error(Error error)  // copy constructor
    {
        for (Review review: error.getReviews()      )
        {
            reviews.add(review);
        }

        currentConfidence = error.getCurrentConfidence();
        estimatedDuration = error.getEstimatedDuration();
        urgencyMultiplier = error.getUrgencyMultiplier();
        dateCreated = error.getDateCreated();
        title = error.getTitle();
        description = error.getDescription();
        pointer = error.getPointer();
        tags = error.getTags();
        appearsInGreedyFill = error.getVisibilityStatus();
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

    public String getDateCreated()
    {
        return dateCreated;
    }

    public String getTags()
    {
        return tags;
    }

    public String getDescription()
    {
        return description;
    }

    public ArrayList<Review> getReviews()
    {
        return reviews;
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

    public double getCurrentConfidence()
    {
        return currentConfidence;
    }

    public boolean getVisibilityStatus()
    {
        return appearsInGreedyFill;
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

    public void updateConfidence()
    {
        double timePassed = calculateHoursPassed(LocalDateTime.parse(reviews.getLast().getDate()), LocalDateTime.now());
        double halfLife = Math.clamp(50.0 / calculateDecayPerHour(), 0.1, 720.0);

        currentConfidence = reviews.getLast().getConfidence() * Math.pow(0.5, (timePassed / halfLife));
    }

    public double returnNewConfidence()
    {
        double timePassed = calculateHoursPassed(LocalDateTime.parse(reviews.getLast().getDate()), LocalDateTime.now());
        double halfLife = Math.clamp(50.0 / calculateDecayPerHour(), 0.1, 720.0);

        return reviews.getLast().getConfidence() * Math.pow(0.5, (timePassed / halfLife));
    }

    public double returnTheoreticalConfidence(double timePassed)
    {
        double halfLife = Math.clamp(50.0 / calculateDecayPerHour(), 0.1, 720.0);

        return reviews.getLast().getConfidence() * Math.pow(0.5, (timePassed / halfLife));
    }

    // error objects are considered equal if they were created at the same time
    public boolean equals(Error error)
    {
        return dateCreated.equals(error.dateCreated);
    }
}
