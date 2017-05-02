package vse.it475.soaplibrary.model.entities;

import java.util.Date;

/**
 * Created by Amir on 5/2/2017.
 */
public class BookingBook {

    private String bookerId;
    private Date bookFrom;
    private Date bookTo;

    public String getBookerId() {
        return bookerId;
    }

    public void setBookerId(String bookerId) {
        this.bookerId = bookerId;
    }

    public Date getBookFrom() {
        return bookFrom;
    }

    public void setBookFrom(Date bookFrom) {
        this.bookFrom = bookFrom;
    }

    public Date getBookTo() {
        return bookTo;
    }

    public void setBookTo(Date bookTo) {
        this.bookTo = bookTo;
    }
}
