package dev.rodrigosambade.philosophers;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class DiningPhilosophersTest{@Test void everyoneEatsWithoutDeadlock()throws Exception{assertArrayEquals(new int[]{50,50,50,50,50},new DiningPhilosophers(5).dine(50));}}
