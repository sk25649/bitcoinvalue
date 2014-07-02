package austin.siwan.bitcoinvalue.bitcoinvalue;

/**
 * Created by Jojo on 7/2/14.
 */
public class Bitcoin {

    private String ask, bid, last, timestamp;

    public String getAsk() {
        return ask;
    }

    public String getBid() {
        return bid;
    }

    public String getLast() {
        return last;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
