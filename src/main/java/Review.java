import java.time.LocalDateTime;

public class Review
{
    // private member variables
    private String date;
    private int confidence;

    // constructor
    public Review(int c)
    {
        confidence = c;
        date = LocalDateTime.now().toString();
    }

    // getters
    public String getDate()
    {
        return date;
    }

    public int getConfidence()
    {
        return confidence;
    }
}