package webscraber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tester {

    private List<TagCounter> urls = new ArrayList();

    public void runSequental() {
        List<TagCounter> urls = new ArrayList();
        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://politiken.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));
        for (TagCounter tc : urls) {
            tc.doWork();
            System.out.println("Title: " + tc.getTitle());
            System.out.println("Div's: " + tc.getDivCount());
            System.out.println("Body's: " + tc.getBodyCount());
            System.out.println("----------------------------------");
        }
    }

    public void runParrallel() throws InterruptedException {
        System.out.println("Submitting Task ...");

        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<Future<TagCounter>> counters = new ArrayList();

        counters.add(executor.submit(new TagCounterCallable("https//www.fck.dk")));
        counters.add(executor.submit(new TagCounterCallable("https://www.google.com")));
        counters.add(executor.submit(new TagCounterCallable("https://politiken.dk")));
        counters.add(executor.submit(new TagCounterCallable("https://cphbusiness.dk")));

        System.out.println("Task is submitted");

//        while (!executor.isShutdown()) {
//            System.out.println("Task is not completed yet....");
//            Thread.sleep(1000);
//        }

        for (Future<TagCounter> future : counters) {
            try {
                TagCounter tc = future.get();
                System.out.println("Title: " + tc.getTitle());
                System.out.println("Div's: " + tc.getDivCount());
                System.out.println("Body's: " + tc.getBodyCount());
                System.out.println("----------------------------------");
            } catch (ExecutionException ex) {
                System.out.println("Exception: " + ex);
            }
        }

        executor.shutdown();
    }
    
    public List<TagCounter> getTagCounters() throws InterruptedException {
        System.out.println("Submitting Task ...");

        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<Future<TagCounter>> counters = new ArrayList();

        counters.add(executor.submit(new TagCounterCallable("https//www.fck.dk")));
        counters.add(executor.submit(new TagCounterCallable("https://www.google.com")));
        counters.add(executor.submit(new TagCounterCallable("https://politiken.dk")));
        counters.add(executor.submit(new TagCounterCallable("https://cphbusiness.dk")));

        List<TagCounter> tcounters = new ArrayList();
        for (Future<TagCounter> future : counters) {
            try {
                TagCounter tc = future.get();
                tcounters.add(tc);
            } catch (ExecutionException ex) {
                System.out.println("Exception: " + ex);
            }
        }
        executor.shutdown();
        return tcounters;
    }

    public static void main(String[] args) throws InterruptedException {
        long timeSequental;
        long timeParallel;
        long start = System.nanoTime();

        new Tester().runSequental();

        long end = System.nanoTime();
        timeSequental = end - start;
        System.out.println("Time Sequential: " + ((timeSequental) / 1_000_000) + " ms.");

        System.out.println("*****************************************************");
        System.out.println("************        Run Parallel     ****************");
        System.out.println("*****************************************************");

        start = System.nanoTime();
        new Tester().runParrallel();
        end = System.nanoTime();
        timeParallel = end - start;
        System.out.println("Time Parallel: " + ((timeParallel) / 100_000_000) + " ms. ");

        System.out.println("Paralle was " + timeSequental / timeParallel + " times faster");

    }
}
