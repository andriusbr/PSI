import java.util.List;

/**
 * Created by Tautvydas on 2016-02-14.
 */
public class Items {

    private List<Item> items = new List<Item>;
    private List<int> ids = new List<int>;
    private int currentId;

    public Items () {}

    public Items (List<Item> items) {
        this.items = items;
    }

    public Item get (int i) {
        return items.get(i);
    }

    public void add (Item item) {
        this.items.add(item);
    }

    public boolean isEmpty () {
        return items.isEmpty();
    }

    public boolean contains (Item item) {
        return items.contains(item);
    }

    public int generateID () {

    }

    public boolean containsId (int id) {
        return items.contains();
    }
    /*-items : List<Item>
-ids : List<Integer>
-currentId : integer
+Items()
+Item(items : List<Item>)
+get(i : integer) : Item
+add(item : Item) : void
+isEmpty() : boolean
+contains(item : Item) : boolean
-generateId() : integer
-containsId(id : integer) : boolean
*/
}
