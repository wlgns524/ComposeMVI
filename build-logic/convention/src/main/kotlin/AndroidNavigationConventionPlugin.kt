import com.kotlin.compose_mvi.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
class AndroidNavigationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("androidx.navigation.safeargs.kotlin")

            dependencies {
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.navigation.fragment.ktx").get())
                add("implementation", libs.findLibrary("androidx.navigation.ui.ktx").get())
            }
        }
    }
}
