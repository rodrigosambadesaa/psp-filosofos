package dev.rodrigosambade.philosophers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public final class DiningPhilosophers {

    private final ReentrantLock[] forks;

    public DiningPhilosophers(int philosopherCount) {
        if (philosopherCount < 2) {
            throw new IllegalArgumentException("At least two philosophers are required");
        }

        forks = new ReentrantLock[philosopherCount];
        for (int index = 0; index < philosopherCount; index++) {
            forks[index] = new ReentrantLock(true);
        }
    }

    public int[] dine(int mealsPerPhilosopher) throws InterruptedException {
        if (mealsPerPhilosopher < 0) {
            throw new IllegalArgumentException("mealsPerPhilosopher must not be negative");
        }

        AtomicInteger[] mealCounts = createMealCounters();

        try (ExecutorService executor = Executors.newFixedThreadPool(forks.length)) {
            List<Future<?>> tasks = new ArrayList<>(forks.length);

            for (int philosopher = 0; philosopher < forks.length; philosopher++) {
                int philosopherId = philosopher;
                tasks.add(executor.submit(
                        () -> eatMeals(philosopherId, mealsPerPhilosopher, mealCounts)));
            }

            waitForAll(tasks);
        }

        return Arrays.stream(mealCounts)
                .mapToInt(AtomicInteger::get)
                .toArray();
    }

    private AtomicInteger[] createMealCounters() {
        AtomicInteger[] counters = new AtomicInteger[forks.length];
        Arrays.setAll(counters, ignored -> new AtomicInteger());
        return counters;
    }

    private void eatMeals(
            int philosopherId,
            int meals,
            AtomicInteger[] mealCounts) {
        ForkOrder order = forkOrderFor(philosopherId);

        for (int meal = 0; meal < meals; meal++) {
            eatOnce(order, mealCounts[philosopherId]);
            Thread.yield();
        }
    }

    private void eatOnce(ForkOrder order, AtomicInteger mealCount) {
        ReentrantLock firstFork = forks[order.first()];
        ReentrantLock secondFork = forks[order.second()];

        firstFork.lock();
        try {
            secondFork.lock();
            try {
                mealCount.incrementAndGet();
            } finally {
                secondFork.unlock();
            }
        } finally {
            firstFork.unlock();
        }
    }

    private ForkOrder forkOrderFor(int philosopherId) {
        int leftFork = philosopherId;
        int rightFork = (philosopherId + 1) % forks.length;

        return new ForkOrder(
                Math.min(leftFork, rightFork),
                Math.max(leftFork, rightFork));
    }

    private static void waitForAll(List<Future<?>> tasks) throws InterruptedException {
        for (Future<?> task : tasks) {
            try {
                task.get();
            } catch (ExecutionException exception) {
                throw new IllegalStateException("Philosopher task failed", exception.getCause());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DiningPhilosophers simulation = new DiningPhilosophers(5);
        int[] result = simulation.dine(100);
        System.out.println(Arrays.toString(result));
    }

    private record ForkOrder(int first, int second) {
    }
}
