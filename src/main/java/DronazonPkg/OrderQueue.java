package DronazonPkg;

import java.util.ArrayList;

public class OrderQueue {
    public ArrayList<Order> buffer = new ArrayList<Order>();
    public Order current;

    public OrderQueue() {   }

    public synchronized void put(Order newOrder) {
        buffer.add(newOrder);
        notify();
    }

    public synchronized Order take() {
        Order order = null;

        while(isEmpty()) {
            try {
                System.out.println("CONSUMER THREAD - Consumer in attesa...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        order = buffer.get(0);
        setCurrent(order);
        buffer.remove(0);
        return order;
    }

    public synchronized boolean isEmpty(){
        return buffer.size()==0;
    }

    public synchronized Order getCurrent() {
        return current;
    }

    public synchronized void setCurrent(Order current) {
        this.current = current;
    }
}
