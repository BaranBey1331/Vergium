# Vergium Hardening Plan — Phase 2 (2026-04-17)

## Objective
- Expand automated verification by roughly an order of magnitude.
- Add FPS-oriented simulation so render behavior can be reasoned about without a live game session.
- Remove or reduce remaining fake, crash-prone, or under-specified architecture paths.

## Risky / Under-tested Areas
1. `VergiumChunkBuilder` is still mostly placeholder logic.
2. `VergiumRenderDispatcher` lacks a tested non-GL fallback path.
3. `VulkanFastPath` has no structured diagnostics or simulation coverage.
4. `EntityInstancer` still couples planning and GL submission too tightly.
5. `ModBridge` does not expose measurable behavior beyond layer normalization.
6. Resource helpers lack explicit reset/test hooks for non-runtime verification.
7. Existing tests cover only a subset of pure-Java helpers.

## Cleanup / Hardening Strategy
1. Add a render simulation package for workload, FPS, and stability projections.
2. Extract pure-Java planning/data objects out of GL-facing components where possible.
3. Replace placeholder chunk-building behavior with deterministic, testable geometry planning.
4. Add fallback/stats paths so non-GL environments still produce measurable outcomes.
5. Expand unit coverage across planners, validators, diagnostics, bridge logic, and simulation scenarios.
6. Keep all new code Forge 1.20.1 / Java 17 compatible and runtime-dependency free.

## Verification Target
- Increase test execution count from the current low 20s to 200+ through broader coverage and parameterized scenarios.
- Verify with:
  - `./gradlew test build --stacktrace`
  - `./check_integrity.sh`
