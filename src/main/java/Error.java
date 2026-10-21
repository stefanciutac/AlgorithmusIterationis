import java.util.ArrayList;

public class Error
{
    private ArrayList<Review> reviews = new ArrayList<>();
    private int currentConfidence = 0;
    private int estimatedDuration; // in minutes
    private double urgencyMultiplier = 1.0;

    // constructors
    public Error(int eD)
    {
        estimatedDuration = eD;
    }

    public Error()
    {
        estimatedDuration = 30;
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

    // other methods
    public double calculateScore()
    {
        return (100.0 - (double)currentConfidence) / estimatedDuration * urgencyMultiplier;
    }
}
