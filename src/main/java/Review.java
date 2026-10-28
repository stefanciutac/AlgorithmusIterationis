import java.time.LocalDateTime;

public class Review
{
    // private member variables
    final private String date;
    private int confidence;

    // constructors
    public Review(int c)
    {
        confidence = c;
        date = LocalDateTime.now().toString();
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

    public int getConfidence()
    {
        return confidence;
    }

    // setters
    public void setConfidence(int c)
    {
        confidence = Math.clamp(c, 0, 100);
    }
}