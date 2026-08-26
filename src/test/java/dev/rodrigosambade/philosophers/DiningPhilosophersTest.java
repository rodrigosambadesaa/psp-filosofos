package dev.rodrigosambade.philosophers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DiningPhilosophersTest {

    @Test
    void everyoneEatsWithoutDeadlock() throws InterruptedException {
        DiningPhilosophers simulation = new DiningPhilosophers(5);

        int[] meals = simulation.dine(50);

        assertArrayEquals(new int[]{50, 50, 50, 50, 50}, meals);
    }
}
