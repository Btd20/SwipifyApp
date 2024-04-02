import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityUnitTest {

    private lateinit var registerActivity: RegisterActivity

    @Before
    fun setUp() {
        registerActivity = RegisterActivity()
    }

    /* PRUEBA UNITARIA DE METODO */
    @Test
    fun testRegister_SuccessfulRegistration() {

        val username = "usuario"
        val password = "contraseña"
        val confirmPassword = "contraseña"

        // Llamar al método register()
        registerActivity.register(username, password, confirmPassword)

        // Verificar si la navegación a la pantalla de inicio
        val expectedIntent = Intent(registerActivity, LoginActivity::class.java)
        val actualIntent = registerActivity.getNextStartedActivity()
        assertEquals(expectedIntent.component, actualIntent.component)
    }

    @Test
    fun testRegister_PasswordsDoNotMatch() {

        val username = "usuario"
        val password = "contraseña"
        val confirmPassword = "contraseñaIncorrecta"

        // Llamar al método register()
        registerActivity.register(username, password, confirmPassword)

        // Verificar si no se inició
        assertEquals(null, registerActivity.getNextStartedActivity())
    }

    /* PRUEBA UNITARIA DE METODO */
    @Test
    fun testNavigateToLogin() {
        // Llamar al método navigateToLogin()
        registerActivity.navigateToLogin()

        // Verificar si se inició la actividad de inicio de sesión
        val expectedIntent = Intent(registerActivity, LoginActivity::class.java)
        val actualIntent = registerActivity.getNextStartedActivity()
        assertEquals(expectedIntent.component, actualIntent.component)
    }
}
