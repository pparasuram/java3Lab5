package cscc.edu;
public class USGSMain {
    public static void main(String[] args) {
        // write your code here
        USGSDataInteractionHibernate usgsDataInteractionHibernate = new USGSDataInteractionHibernate();
        usgsDataInteractionHibernate.inputLoop();
    }
}
