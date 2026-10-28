import java.time.LocalDateTime;
import java.util.ArrayList;

public class Error
{
    private ArrayList<Review> reviews = new ArrayList<>();
    private int currentConfidence = 50;
    private int estimatedDuration; // in minutes
    private double urgencyMultiplier = 1.0;
    private String dateCreated;

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
        tags = tg;
    }

    public Error()
    {
        dateCreated = LocalDateTime.now().toString();
    }

    // getters and setters
    public void addReview(Review review)
    {
        reviews.add(review);
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

    // other methods
    public double calculateScore()
    {
        return (100.0 - (double)currentConfidence) * urgencyMultiplier;
    }

    // error objects are considered equal if they were created at the same time
    public boolean equals(Error error)
    {
        return dateCreated.equals(error.dateCreated);
    }
}
