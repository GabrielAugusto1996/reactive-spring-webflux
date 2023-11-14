package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinksTest {

    @Test
    void sinkTest() {
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> fluxOfInteger = replaySink.asFlux();
        fluxOfInteger.subscribe(integer -> System.out.println("Subscriber 1:" + integer));

        Flux<Integer> fluxOfInteger2 = replaySink.asFlux();
        fluxOfInteger2.subscribe(integer -> System.out.println("Subscriber 2:" + integer));

        replaySink.tryEmitNext(3);

        Flux<Integer> fluxOfInteger3 = replaySink.asFlux();
        fluxOfInteger3.subscribe(integer -> System.out.println("Subscriber 3:" + integer));

    }

    @Test
    void sinkTest_multicast() {
        Sinks.Many<Integer> multicastSink = Sinks.many().multicast().onBackpressureBuffer();

        multicastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multicastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> fluxOfInteger = multicastSink.asFlux();
        fluxOfInteger.subscribe(integer -> System.out.println("Subscriber 1:" + integer));

        Flux<Integer> fluxOfInteger2 = multicastSink.asFlux();
        fluxOfInteger2.subscribe(integer -> System.out.println("Subscriber 2:" + integer));

        multicastSink.tryEmitNext(3);

        Flux<Integer> fluxOfInteger3 = multicastSink.asFlux();
        fluxOfInteger3.subscribe(integer -> System.out.println("Subscriber 3:" + integer));

    }

    //Allow only one Subscriber
    @Test
    void sinkTest_unicast() {
        Sinks.Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();

        unicastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        unicastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> fluxOfInteger = unicastSink.asFlux();
        fluxOfInteger.subscribe(integer -> System.out.println("Subscriber 1:" + integer));

        Flux<Integer> fluxOfInteger2 = unicastSink.asFlux();
        fluxOfInteger2.subscribe(integer -> System.out.println("Subscriber 2:" + integer));

        unicastSink.tryEmitNext(3);

        Flux<Integer> fluxOfInteger3 = unicastSink.asFlux();
        fluxOfInteger3.subscribe(integer -> System.out.println("Subscriber 3:" + integer));

    }
}
