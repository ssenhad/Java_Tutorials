package threads.caveofprogramming.part11;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This tutorial demonstrates deadlock and we can solve it using the tryLock() method from the
 * re-entrant lock class.
 */

public class App {

    public static void main(String[] args) {

        System.out.println("Starting...");

        final JobRunner runner = new JobRunner();

        Thread _1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Running thread1()");
                    runner.thread1_acquireLock();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });

        Thread _2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Running thread2()");
                    runner.thread2_acquireLock();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });

        _1.start();
        _2.start();

        try {
            _1.join();
            _2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runner.finishUp();
    }
}

class Runner {
    private Account acc1 = new Account();
    private Account acc2 = new Account();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    private void acquireLocks(Lock firstLock, Lock secondLock) throws InterruptedException {
        while(true) {
            // Acquire locks

            boolean gotFirstLock = false;
            boolean gotSecondLock = false;

            try {
                gotFirstLock = firstLock.tryLock();
                gotSecondLock = secondLock.tryLock();
            }
            finally {
                if(gotFirstLock && gotSecondLock) {
                    return;
                }

                if(gotFirstLock) {
                    firstLock.unlock();
                }

                if(gotSecondLock) {
                    secondLock.unlock();
                }
            }

            // Locks not acquired
            Thread.sleep(1);
        }
    }

    public void firstThread() throws InterruptedException {

        Random random = new Random();

        for (int i = 0; i < 10000; i++) {

            acquireLocks(lock1, lock2);

            try {
                Account.transfer(acc1, acc2, random.nextInt(100));
//                Account.transfer(acc1, acc2, );
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void secondThread() throws InterruptedException {
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {

            acquireLocks(lock2, lock1);

            try {
                Account.transfer(acc2, acc1, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void finished() {
        System.out.println("Account 1 balance: " + acc1.getBalance());
        System.out.println("Account 2 balance: " + acc2.getBalance());
        System.out.println("Total balance: "
                + (acc1.getBalance() + acc2.getBalance()));
    }
}

class JobRunner {

    private Account a_1 = new Account();
    private Account a_2 = new Account();

    private Lock l_1 = new ReentrantLock();
    private Lock l_2 = new ReentrantLock();


    // transfer from account a to b
    public void thread1_buggy () throws InterruptedException {
        Random r = new Random();
        for (int i=0; i<10000; i++) {
            l_1.lock();
            l_2.lock();
            try {
                Account.transfer(a_1, a_2, r.nextInt(100));
            } finally {
                l_1.unlock();
                l_2.unlock();
            }
        }
    }

    public void thread1_acquireLock () throws InterruptedException {
        Random r = new Random();
        for (int i=0; i<10000; i++) {
            acquireLocks(l_1, l_2);
            try {
                Account.transfer(a_1, a_2, r.nextInt(100));
            } finally {
                l_1.unlock();
                l_2.unlock();
            }
        }
    }

    // transfer from account b to a
    public void thread2_buggy () throws InterruptedException {
        Random r = new Random();
        for (int i=0; i<10000; i++) {
            //l_2.lock(); // make sure this is in the correct order to avoid deadlock
            //l_1.lock(); // l_1 acquires the lock first in thread1... uh-oh!

            /**
             * It is good practice to always lock the locks in the same order
             * OR you can write a method to acquire the locks.
             */
            l_1.lock();
            l_2.lock();
            try {
                Account.transfer(a_2, a_1, r.nextInt(100));
            } finally {
                l_1.unlock();
                l_2.unlock();
            }
        }
    }

    public void thread2_acquireLock () throws InterruptedException {
        Random r = new Random();
        for (int i=0; i<10000; i++) {
            acquireLocks(l_2, l_1);
            try {
                Account.transfer(a_2, a_1, r.nextInt(100));
            } finally {
                l_1.unlock();
                l_2.unlock();
            }
        }
    }

    // Acquire locks in any order
    private void acquireLocks(Lock a, Lock b)
            throws InterruptedException {

        while(true) {
            boolean gotLock_l_1 = false;
            boolean gotLock_l_2 = false;

            try{
                gotLock_l_1 = a.tryLock();
                gotLock_l_2 = b.tryLock();
            } finally {
                // acquired both locks
                if (gotLock_l_1 && gotLock_l_2) {
                    return;
                }

                // acquired first lock, so release it
                if(gotLock_l_1) {
                    a.unlock();
                }

                // acquired second lock, so release it
                if (gotLock_l_2) {
                    b.unlock();
                }
            }

            Thread.sleep(1);
        }
    }


    public void finishUp () {
        System.out.println("Account a_1 balance: " + a_1.getBalance());
        System.out.println("Account a_2 balance: " + a_2.getBalance());
        System.out.println("Sum of account balances: "
                + (a_1.getBalance() + a_2.getBalance()));
    }
}

class Account {

    private int balance = 10000;

    // Add to balance
    public void deposit (int amount) {
        balance += amount;
    }

    // Subtract from balance
    public void withdraw (int amount) {
        balance -= amount;
    }

    public int getBalance () {
        return balance;
    }

    public static void transfer (Account a, Account b, int amount) {
        a.withdraw(amount);
        b.deposit(amount);
        System.out.println("Transfer succesful: " + amount);
    }
}
