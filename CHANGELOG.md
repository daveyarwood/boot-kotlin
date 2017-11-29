# CHANGELOG

## 0.3.0 (2017-11-29)

* When no `--version` option is specified, instead of defaulting to `1.1.1`, we
  will now default to `RELEASE`, which resolves to the latest release version of
  the Kotlin libraries.

* Fixed a bug where the classpath wasn't being included (the `-cp` Kotlin
  compiler option was missing) when running the `kotlin-repl` task, resulting in
  the stdlib not being available in the Kotlin REPL.

## 0.2.1 (2017-04-24)

* Removed `-no-jdk` option. It turns out that we do need the JDK to be included!

## 0.2.0 (2017-04-24)

* Added `kotlin-repl` task.

## 0.1.0 (2017-04-24)

Initial release.
