plugins {
    id("kotlin-mpp-convention")
    id("org.jetbrains.kotlin.plugin.js-plain-objects")
}

kotlin {
    sourceSets {
        all {
            languageSettings.apply {
                setOf("ExperimentalJsExport", "ExperimentalJsStatic").forEach {
                    optIn("kotlin.js.$it")
                }
            }
        }
    }

    // val outputName = project.artifactName.get()
    val outputName = project.name.lowercase()

    js(IR) {
        outputModuleName = outputName
        nodejs() // Target Node.js;
        useEsModules() // Use ECMAScript 2015. This allows to import single components from the library;
        // useCommonJs() // If this is used, imports must specify the full package path.
        generateTypeScriptDefinitions() // Generate TypeScript definitions for the JavaScript code (d.ts files);
        binaries.library() // Generate some binaries for the library -> build/js/packages/...
    }
}
