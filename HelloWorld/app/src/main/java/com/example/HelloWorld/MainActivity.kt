import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.HelloWorld.R

class MainActivity : AppCompatActivity() {
    lateinit var tvHeight: TextView
    lateinit var tvWidth: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.`activity_main.xml`)
        title = "KotlinApp"
        tvHeight = findViewById(R.id.tvHeight)
        tvWidth = findViewById(R.id.tvWidth)
        val displayMetrics = DisplayMetrics()
        val windowsManager = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels
        val deviceHeight = displayMetrics.heightPixels
        tvWidth.text = "Width in Pixels: $deviceWidth"
        tvHeight.text = "Height in Pixels: $deviceHeight"
    }
}