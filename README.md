[icon]: https://raw.githubusercontent.com/inc0g-repoz/lix/refs/heads/main/src/assets/icon.png
[reflection]: https://www.oracle.com/technical-resources/articles/java/javareflection.html
<!-- The stuff above is invisible -->

# ![icon] LIX – Lightweight Interpreted eXecutor

> [!TIP]
> Before you test out the engine, take a look at the wiki pages. They are worth reading.<br>
> Make sure the feature you want to use is supported by the script language to avoid confusion.

### What's this?
LIX is an acronym for Lightweight Interpreted eXecution.
The goal of this project is to implement a scalable and maintainable script engine with a C-like syntax that runs off JVM.
Instead of using types compatible with the engine it allows accessing them directly through [Reflection API][reflection].

### List of features
- [x] Local and script scope variables
- [x] Function calls with recursion
- [x] Inbuilt functions
- [x] Object members chaining
- [x] Basic Java logic and arithmetic unary, binary and ternary operators
- [x] Basic control flow blocks and statements
- [x] Continuation and breaking of loops
- [x] Inclusion of module scripts
- [x] Array index operator (one-dimensional only)
- [x] Function references
- [x] Full unicode escape sequences support
- [ ] Bitwise operators
