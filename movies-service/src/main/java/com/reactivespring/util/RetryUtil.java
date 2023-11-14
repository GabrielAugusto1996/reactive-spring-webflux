package com.reactivespring.util;

import reactor.core.Exceptions;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RetryUtil {

    private RetryUtil() {}

    public static Retry retrySpec() {

        return Retry.backoff(3L, Duration.ofMillis(500))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure()));
    }
}
