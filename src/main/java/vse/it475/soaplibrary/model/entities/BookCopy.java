package vse.it475.soaplibrary.model.entities;

import java.util.Date;

/**
 * Created by hofmanix on 30/04/2017.
 */
public class BookCopy {
    private boolean borrowed;
    private boolean borrowerId;
    private Date from;
    private Date to;

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public boolean isBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(boolean borrowerId) {
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
}
