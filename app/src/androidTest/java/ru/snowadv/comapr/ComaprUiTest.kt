package ru.snowadv.comapr

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.compose.node.element.KNode

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ComaprUiTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport(),
) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login() = run {
        step("Введем логин") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    usernameField {
                        assertIsDisplayed()
                        performTextInput("testaccount")
                    }
                }
            }
        }
        step("Введем пароль") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    passwordField {
                        assertIsDisplayed()
                        performTextInput("testpassword")
                    }
                }
            }
        }
        step("Выполним авторизацию") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    usernameField {
                        assertIsDisplayed()
                        performTextInput("testpassword")
                    }
                }
            }
        }
        step("Проверим, что отображается экран роадмапов") {
            flakySafely {
                onComposeScreen<RoadMapsScreen>(composeTestRule) {
                    isFocused()
                }
            }
        }
    }


    @Test
    fun register() = run {
        step("Перейдем на экран регистрации") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    changeModeButton {
                        assertIsDisplayed()
                        performClick()
                    }
                }
            }
        }
        step("Введем логин") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    usernameField {
                        assertIsDisplayed()
                        performTextInput("testaccount")
                    }
                }
            }
        }
        step("Введем пароль") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    passwordField {
                        assertIsDisplayed()
                        performTextInput("testpassword")
                    }
                }
            }
        }
        step("Введем почту") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    emailField {
                        assertIsDisplayed()
                        performTextInput("testaccount@example.com")
                    }
                }
            }
        }
        step("Выполним авторизацию") {
            flakySafely {
                onComposeScreen<ComposeAuthScreen>(composeTestRule) {
                    usernameField {
                        assertIsDisplayed()
                        performTextInput("testpassword")
                    }
                }
            }
        }
        step("Проверим, что отображается экран роадмапов") {
            flakySafely {
                onComposeScreen<RoadMapsScreen>(composeTestRule) {
                    isFocused()
                }
            }
        }
    }

}

internal class ComposeAuthScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ComposeAuthScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AuthScreen") }
    ) {

    val usernameField: KNode = child {
        hasTestTag("Username")
    }
    val emailField: KNode = child {
        hasTestTag("Email")
    }
    val passwordField: KNode = child {
        hasTestTag("Password")
    }
    val authButton: KNode = child {
        hasTestTag("AuthButton")
    }
    val changeModeButton: KNode = child {
        hasTestTag("ChangeModeButton")
    }
}

internal class RoadMapsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<RoadMapsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("RoadMapsScreen") }
    )