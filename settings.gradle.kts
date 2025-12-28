rootProject.name = "HyperConomy"

// Include SimpleDataLib as a composite build
includeBuild("../SimpleDataLib") {
    dependencySubstitution {
        substitute(module("regalowl.simpledatalib:simpledatalib")).using(project(":"))
    }
}

