import java.time.LocalDateTime;

public class Review
{
    // private member variables
    private String date;
    private double confidence;

    // constructors
    public Review(int c)
    {
        confidence = c;
        date = LocalDateTime.now().toString();
    }

    public Review(Review review)  // copy constructor
    {
        date = new String(review.getDate());
        confidence = review.getConfidence();
    }

    public Review()
    {
        date = LocalDateTime.now().toString();
    }

    // getters
    public String getDate()
    {
        return date;
    }

    public double getConfidence()
    {
        return confidence;
    }

    // setters
    public void setConfidence(double c)
    {
        confidence = Math.clamp(c, 1.0, 100.0);
    }
}