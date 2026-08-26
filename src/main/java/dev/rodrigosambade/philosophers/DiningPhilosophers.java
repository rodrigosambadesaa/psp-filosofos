package dev.rodrigosambade.philosophers;
import java.util.*;import java.util.concurrent.*;import java.util.concurrent.atomic.AtomicInteger;import java.util.concurrent.locks.*;
public final class DiningPhilosophers{
 private final ReentrantLock[]forks;public DiningPhilosophers(int n){if(n<2)throw new IllegalArgumentException();forks=new ReentrantLock[n];for(int i=0;i<n;i++)forks[i]=new ReentrantLock(true);}
 public int[]dine(int mealsPerPhilosopher)throws InterruptedException{if(mealsPerPhilosopher<0)throw new IllegalArgumentException();AtomicInteger[]counts=new AtomicInteger[forks.length];Arrays.setAll(counts,i->new AtomicInteger());try(var ex=Executors.newFixedThreadPool(forks.length)){List<Future<?>>fs=new ArrayList<>();for(int p=0;p<forks.length;p++){int id=p;fs.add(ex.submit(()->{int left=id,right=(id+1)%forks.length,first=Math.min(left,right),second=Math.max(left,right);for(int m=0;m<mealsPerPhilosopher;m++){forks[first].lock();try{forks[second].lock();try{counts[id].incrementAndGet();}finally{forks[second].unlock();}}finally{forks[first].unlock();}Thread.yield();}}));}for(Future<?>f:fs)try{f.get();}catch(ExecutionException e){throw new IllegalStateException(e.getCause());}}return Arrays.stream(counts).mapToInt(AtomicInteger::get).toArray();}
 public static void main(String[]a)throws Exception{System.out.println(Arrays.toString(new DiningPhilosophers(5).dine(100)));}
}
