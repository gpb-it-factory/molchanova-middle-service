package ru.molchmd.minibank.middle.metrics.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TransfersMetrics {
    private Counter post;
    private Counter successPost;

    public TransfersMetrics(MeterRegistry meterRegistry) {
        post = meterRegistry.counter("requests.counter.transfers.post");
        successPost = meterRegistry.counter("responses.counter.transfers.success.post");
    }

    public void post() {
        post.increment();
    }

    public void successPost() {
        successPost.increment();
    }
}
