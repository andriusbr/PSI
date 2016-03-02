import java.util.List;

/**
 * Created by tauvai2
 */

public class Orders {

    private List<Order> orders = new List<Order>;
    private List<int> ids = new List<int>;
    private int currentId;

    public Orders () {}

    public Orders (List<Order> orders) {
        this.orders = orders;
    }

    public Order get (int i) {
        return orders.get(i);
    }

    public void add (Order order) {
        this.orders.add(order);
    }

    public boolean isEmpty () {
        return orders.isEmpty();
    }

    public boolean contains (Order order) {
        return orders.contains(order);
    }

    private int generateID () {

    }

    private boolean containsId (int id) {
        return orders.contains();
    }

    public boolean removeAllCarriedOutOrders() {
        for (int i=0; i < size; i++) {
            elementDarta
        }
        size = 0;
    }
 