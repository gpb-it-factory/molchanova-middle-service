package ru.molchmd.minibank.middle.metrics.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UsersMetrics {
    private Counter post;
    private Counter successPost;

    public UsersMetrics(MeterRegistry meterRegistry) {
        post = meterRegistry.counter("requests.counter.users.post");
        successPost = meterRegistry.counter("responses.counter.users.success.post");
    }

    public void post() {
        post.increment();
    }

    public void successPost() {
        successPost.increment();
    }
}
