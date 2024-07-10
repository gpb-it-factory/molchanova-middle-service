package ru.molchmd.minibank.middle.metrics.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AccountsMetrics {
    private Counter post;
    private Counter get;
    private Counter successGet;
    private Counter successPost;

    public AccountsMetrics(MeterRegistry meterRegistry) {
        post = meterRegistry.counter("requests.counter.accounts.post");
        get = meterRegistry.counter("requests.counter.accounts.get");
        successPost = meterRegistry.counter("responses.counter.accounts.success.post");
        successGet = meterRegistry.counter("responses.counter.accounts.success.get");
    }

    public void post() {
        post.increment();
    }

    public void get() {
        get.increment();
    }

    public void successPost() {
        successPost.increment();
    }

    public void successGet() {
        successGet.increment();
    }
}
