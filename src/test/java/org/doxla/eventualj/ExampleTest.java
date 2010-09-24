package org.doxla.eventualj;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static org.doxla.eventualj.Eventually.eventually;
import static org.doxla.eventualj.EventuallyMatchers.willBe;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class ExampleTest {

    private static final int MESSAGES_TO_PLAY = 1000;
    private static Logger LOG = Logger.getLogger("test_logger");

    private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
    private ExecutorService executorService;

    @Before public void createProducerAndConsumerOnABlockingQueue() throws Exception {
        executorService = Executors.newFixedThreadPool(2);
        queue.put("initial");
        executorService.submit(new Producer(queue));
        executorService.submit(new Consumer(queue));
    }

    @After public void shutdown() {
        executorService.shutdownNow();
    }


    @Test public void exampleTest() throws Exception {
        assertThat("Queue is not empty yet", 
                queue.size(), is(not(0)));
        assertThat("But eventually it will be",
                eventually(queue).size(), willBe(0).within(seconds(5L)));
    }


    private static class Producer implements Runnable {
        private final BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        public void run() {
            for(int i = 0; i < MESSAGES_TO_PLAY; i++) {
                try {
                    queue.put("String: "+i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class Consumer implements Runnable {
        private final BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        public void run() {
            for (int i = 0; i < MESSAGES_TO_PLAY + 1; i++) {
                try {
                    String taken = queue.take();
                    LOG.info("taken = " + taken);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
