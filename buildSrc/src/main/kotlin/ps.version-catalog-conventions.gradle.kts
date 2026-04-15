import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

plugins {
    id("nl.littlerobots.version-catalog-update")
}

versionCatalogUpdate {
    keep {
        keepUnusedVersions.set(true)
    }
    versionSelector(VersionSelectors.STABLE)
}
