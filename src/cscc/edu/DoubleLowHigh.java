package cscc.edu;

public class DoubleLowHigh<A, B> {
    A low = null;
    B high = null;

    DoubleLowHigh(A first, B second) {
        this.low = first;
        this.high = second;
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
