# reactive-spring-webflux

## Annotations
Reactive Streams are Immutable;

**Mono:** It´s just to add a single element

**Flux:** It´s used to add multiple elements

ConcatMap vs FlatMap: **concatMap** does the same thing that **flatMap**, the only difference between them is that concatMap keep the ordering sequence.
FlatMap is faster than ConcatMap (Use ConcatMap only if the order matter);

---

SwitchIfEmpty vs DefaultIfEmpty: **switchIfEmpty** accept a publisher, while **defaultIfEmpty** accept a type.

---

Concat and ConcatWith are operators that combine more than one reactive stream.
Concat is just available for Flux and the method is static, ConcatWith is available for Flux and Mono and the method is an instance.

---

Concat operators vs Merge operators: Concat operators are in sequence while merge operators are async.

Merge and MergeWith are operators that combine more than one reactive stream.
Merge is just available for Flux and the method is static, MergeWith is available for Flux and Mono and the method is an instance.

---

Zip and ZipWith are operators that merge 2 to 8 publishers in one.
Zip is just available for Flux and the method is static, ZipWith is available for Flux and Mono and the method is an instance.