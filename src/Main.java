import java.util.concurrent.*;

public class Main {
    static final int NUM_PHILOSOPHERS = 5;
    static final Semaphore[] forks = new Semaphore[NUM_PHILOSOPHERS];
    static final Semaphore waiter1 = new Semaphore(1); // Офіціант 1
    static final Semaphore waiter2 = new Semaphore(1); // Офіціант 2

    static class Philosopher extends Thread {
        private final int id;

        public Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    think();
                    // Користуються офіціантом
                    if (id % 2 == 0) {
                        waiter1.acquire();
                    } else {
                        waiter2.acquire();
                    }

                    pickUpForks();
                    eat();
                    putDownForks();

                    // Повертаються офіціанту
                    if (id % 2 == 0) {
                        waiter1.release();
                    } else {
                        waiter2.release();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void think() throws InterruptedException {
            System.out.println("Philosopher " + id + " is thinking.");
            Thread.sleep((long) (Math.random() * 1000));
        }

        private void pickUpForks() throws InterruptedException {
            System.out.println("Philosopher " + id + " is picking up forks.");
            forks[id].acquire();
            forks[(id + 1) % NUM_PHILOSOPHERS].acquire();
        }

        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + id + " is eating.");
            Thread.sleep((long) (Math.random() * 1000));
        }

        private void putDownForks() {
            System.out.println("Philosopher " + id + " is putting down forks.");
            forks[id].release();
            forks[(id + 1) % NUM_PHILOSOPHERS].release();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Semaphore(1);
        }

        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new Philosopher(i);
            philosophers[i].start();
        }
    }
}
