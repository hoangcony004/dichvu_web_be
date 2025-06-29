package web.backend.core.configs.system_configuration;

import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class RequestQueueService {

    private final Queue<String> queue = new ConcurrentLinkedQueue<>();

    public void enqueue(String data) {
        queue.offer(data);
    }

    public String dequeue() {
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }
}
