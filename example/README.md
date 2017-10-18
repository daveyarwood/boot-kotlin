# boot-kotlin example project

This is just a main function that parrots back its arguments.

When compiling a Kotlin file containing top-level functions, the Kotlin compiler
emits a Java `.class` file whose name resembles the name of the Kotlin file. In
this case, `test.kt` compiles into `TestKt.class`.

## Continuously compile & run

```
boot watch kotlinc run -m TestKt -a 1 -a 2 -a 3
```

Try making changes to `test.kt`. The Boot task pipeline will watch for changes,
recompile, and re-run the `main` function each time.

## Build a jar file

```
boot kotlinc uber jar -m TestKt target
```

## Run the jar file

```
java -jar target/project.jar a b c
```
