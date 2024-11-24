import java.util.LinkedList;
import java.util.Queue;

class MessageQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final int capacity;

    
    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }


    public synchronized void produce(String message) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); 
        }
        queue.add(message);
        System.out.println("Produced: " + message);
        notifyAll(); 
    }

    
    public synchronized String consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        String message = queue.poll();
        System.out.println("Consumed: " + message);
        notifyAll(); 
        return message;
    }
}


class Producer implements Runnable {
    private final MessageQueue queue;

    public Producer(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                queue.produce("Message-" + i);
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


class Consumer implements Runnable {
    private final MessageQueue queue;

    public Consumer(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                queue.consume();
                Thread.sleep(200); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


public class DistributedMessageQueue {
    public static void main(String[] args) {
        
        MessageQueue queue = new MessageQueue(5);

        
        Thread producer1 = new Thread(new Producer(queue));
        Thread producer2 = new Thread(new Producer(queue));
        Thread consumer1 = new Thread(new Consumer(queue));
        Thread consumer2 = new Thread(new Consumer(queue));

       
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        
        try {
            producer1.join();
            producer2.join();
            consumer1.join();
            consumer2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}