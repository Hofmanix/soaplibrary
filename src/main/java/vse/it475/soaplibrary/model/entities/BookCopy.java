package vse.it475.soaplibrary.model.entities;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by hofmanix on 30/04/2017.
 */
public class BookCopy {

    @Id
    private String uniqueSignature;
    private boolean borrowed;
    private String borrowerId;
    private String providerId;
    private Date from;
    private Date to;
    private Date returned;


    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public String isBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getUniqueSignature() { return uniqueSignature; }

    public void setUniqueSignature(String uniqueSignature) { this.uniqueSignature = uniqueSignature; }

    public String getProviderId() { return providerId; }

    public void setProviderId(String providerId) { this.providerId = providerId; }

    public Date getReturned() { return returned; }

    public void setReturned(Date returned) { this.returned = returned; }
}
