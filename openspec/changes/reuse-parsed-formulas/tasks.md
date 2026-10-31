# Tasks

## 1. Parsed formulas

- [ ] 1.1 Write the failing tests for repeated evaluation, nested calls and concurrent evaluation
- [ ] 1.2 Keep the parsed form per formula in `ExpressionEngine`
- [ ] 1.3 Reuse the parsed form in `FormulaFunction` so nested calls do not parse again
- [ ] 1.4 Update the Javadoc sentence that says each evaluation parses its source anew

## 2. Verification and pull request

- [ ] 2.1 Run `./gradlew build`
- [ ] 2.2 Open the pull request titled `perf(expression): reuse parsed formulas`
