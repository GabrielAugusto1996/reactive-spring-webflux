# reactive-spring-webflux

## Annotations
Reactive Streams are Immutable;

**Mono:** It´s just to add a single element

**Flux:** It´s used to add multiple elements

ConcatMap vs FlatMap: **concatMap** does the same thing that **flatMap**, the only difference between them is that concatMap keep the ordering sequence.
FlatMap is faster than ConcatMap (Use ConcatMap only if the order matter);