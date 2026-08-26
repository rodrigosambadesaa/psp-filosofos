# PSP · Filósofos comensales

Reimplementación del problema clásico de los filósofos. En vez de depender de temporizaciones o de un `synchronized` global, cada tenedor es un `ReentrantLock` justo y todos los filósofos adquieren los dos tenedores siguiendo **un orden total por índice**. Esto elimina la espera circular y, por tanto, el interbloqueo.

```bash
mvn verify
java -cp target/classes dev.rodrigosambade.philosophers.DiningPhilosophers
```
