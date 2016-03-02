/**
 *
 * @author TauVai2
 */
public class Order {
    private int id;
    private String address;
    private Items items;
    private String personsFirstName;
    private String personsLastName;
    private boolean express;
    private boolean carriedOut;
    
    public Order() {}
    
    public Order(String address, Items items, String firstName, String lastName, boolean express)
    {
        this.address = address;
        this.items = items;
        this.personsFirstName = firstName;
        this.personsLastName = lastName;
        this.express = express;
    }
    
    public Order(int id ,String address, Items items, String firstName, String lastName, boolean express)
    {
        this.id = id;
        this.address = address;
        this.items = items;
        this.personsFirstName = firstName;
        this.personsLastName = lastName;
        this.express = express;
    }
    
    public int getId() { return id;}
    public String getAddress() { return address;}
    public Items getItems() { return items;}
    public String getFirstName() { return personsFirstName;}
    public String getLastName() { return personsLastName;}
    public boolean isExpress() { return express;}
    public boolean isCarriedOut() { return carriedOut;}
    
    
}