import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

plugins {
    alias(libs.plugins.version.catalog.update)
}

versionCatalogUpdate {
    keep {
        keepUnusedVersions.set(true)
    }
    versionSelector(VersionSelectors.STABLE)
}
