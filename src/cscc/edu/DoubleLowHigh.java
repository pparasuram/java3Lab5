package cscc.edu;

public class DoubleLowHigh<A, B> {
    A low = null;
    B high = null;

    DoubleLowHigh(A low, B high) {
        this.low = low;
        this.high = high;
    }

    public A getLow() {
        return low;
    }

    public void setLow(A low) {
        this.low = low;
    }

    public B getHigh() {
        return high;
    }

    public void setHigh(B high) {
        this.high = high;
    }

}
