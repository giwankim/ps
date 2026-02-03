**Testing Style Guide**

**Default Spec**
Use `FunSpec` for all unit tests in this module.

**Data-Driven Tests (Kotest 6.1.x)**
Use `withTests` when one row equals one test. Use `withContexts` when a row needs multiple tests or nested structure.

```kotlin
class ExampleTest : FunSpec({
    val sut = Example()

    withTests(
        nameFn = { (a, b, expected) -> "a=$a, b=$b, expected=$expected" },
        Case(1, 2, 3),
        Case(2, 3, 5),
    ) { (a, b, expected) ->
        sut.add(a, b) shouldBe expected
    }
})

private data class Case(
    val a: Int,
    val b: Int,
    val expected: Int,
)
```

**Naming**
Provide readable names for data tests using `nameFn`, especially when inputs are arrays. Use `contentToString()` for `IntArray` and similar types.

**Assertions**
Use Kotest matchers (for example `shouldBe`) consistently. Avoid mixing AssertJ or JUnit assertions.

**Structure**
Keep each test file focused on one class. Always wrap tests in a `context("...")` block that describes what is being tested, even if there is only a single `withTests` group.

**SUT Placement**
Declare `sut` at the spec scope (inside the `FunSpec` block) so it is available to all tests, and avoid re-creating it in every test unless isolation is required.

**Alternative DSLs (Optional)**
If you want more narrative test names, consider:
- `DescribeSpec` for `describe/it` structure.
- `BehaviorSpec` for `given/when/then` BDD structure.
- `ShouldSpec` for `should("...")` phrasing.

Keep `FunSpec` as the default unless you have a clear reason to switch for a specific test file.

For readability: prefer `FunSpec` for concise, uniform algorithm tests; switch to `DescribeSpec` when you want lightweight narrative grouping, and to `BehaviorSpec` when you want explicit `given/when/then` flows.
