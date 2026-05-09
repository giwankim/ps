# Dependency Updates

Upgrades go through the [Version Catalog Update plugin][vcu] in two phases:
generate a review file, then apply it. The canonical catalog at
`gradle/libs.versions.toml` is never edited directly during an upgrade run.

[vcu]: https://github.com/littlerobots/version-catalog-update-plugin

## 1. Generate the review file

```bash
./gradlew versionCatalogUpdate --interactive
```

The `--interactive` flag does not prompt — it directs the plugin to write
proposed bumps to `gradle/libs.versions.updates.toml` instead of mutating
`gradle/libs.versions.toml` in place. Re-running this task overwrites the
review file, so finish reviewing before re-running.

## 2. Review `gradle/libs.versions.updates.toml`

For each proposed entry choose one of:

- **Apply it:** leave the line as-is.
- **Skip this run only:** comment out (or delete) the line. The bump will
  reappear the next time `versionCatalogUpdate` runs.
- **Pin permanently:** in `gradle/libs.versions.toml`, add a `# @pin`
  comment on the line *above* the entry. Pinned entries are excluded from
  every future `versionCatalogUpdate` run.

## 3. Apply the reviewed updates

```bash
./gradlew versionCatalogApplyUpdates
```

This rewrites `gradle/libs.versions.toml` with the entries that survived
review. Inspect the resulting diff before continuing.

## 4. Verify the build

```bash
./gradlew build
```

If the build fails, identify which dependency caused it and either pin
that entry (step 2) or revert its bump in `gradle/libs.versions.toml`,
then re-run the build.

## 5. Update the Gradle wrapper (optional, independent)

```bash
./gradlew wrapper --gradle-version=latest
```

Re-run `./gradlew build` afterward to verify. Commit the wrapper change
separately from the catalog change.
