package com.example.bmicalculator
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.RoundedCornerShapepackage com.example.bmicalculator
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import android.content.Intent
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import kotlin.math.pow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorApp()
        }
    }
}

object SharedPreferencesHelper {

    private const val PREFS_NAME = "bmi_prefs"
    private const val KEY_BMI_HISTORY = "bmi_history"

    // Save BMI history to SharedPreferences
    fun saveBMIHistory(context: Context, bmiHistory: List<Pair<String, String>>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(bmiHistory)
        editor.putString(KEY_BMI_HISTORY, json)
        editor.apply()
    }

    // Load BMI history from SharedPreferences
    fun loadBMIHistory(context: Context): List<Pair<String, String>> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_BMI_HISTORY, "[]")
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        return Gson().fromJson(json, type)
    }
}

@Composable
fun BMICalculatorApp() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Load BMI history from SharedPreferences
    var bmiHistory by remember { mutableStateOf(SharedPreferencesHelper.loadBMIHistory(context)) }

    NavHost(
        navController = navController,
        startDestination = "bmi_calculator"
    ) {
        composable(
            "bmi_calculator",
            enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn(animationSpec = tween(1000)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut(animationSpec = tween(1000)) }
        ) {
            BMICalculatorScreen(navController, bmiHistory) { newHistory ->
                // Update bmiHistory and save to SharedPreferences
                bmiHistory = newHistory
                SharedPreferencesHelper.saveBMIHistory(context, newHistory)
            }
        }

        composable(
            "second_screen",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(1000)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(1000)) }
        ) {
            SecondScreen()
        }

        composable(
            "third_screen",
            enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn(animationSpec = tween(1000)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut(animationSpec = tween(1000)) }
        ) {
            ThirdScreen(bmiHistory = bmiHistory)
        }
    }
}


@Composable
fun ThirdScreen(bmiHistory: List<Pair<String, String>>) {
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current // Get context for launching the share intent

    // Simulate loading
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500) // Simulate a delay
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = Color(0xFF1E2A47)
                )
            }
        } else {
            Text(
                text = "BMI History",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                color = Color(0xFF1E2A47),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (bmiHistory.isEmpty()) {
                Text(
                    text = "No history available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                )
            } else {
                bmiHistory.forEach { (bmi, category) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = bmi,
                                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                                    color = Color(0xFF1E2A47)
                                )
                                Text(
                                    text = category,
                                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
                                    color = if (category.contains("😊")) Color.Green else Color.Red
                                )
                            }
                            IconButton(
                                onClick = {
                                    // Create a share intent
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, "BMI: $bmi, Category: $category")
                                    }
                                    context.startActivity(
                                        Intent.createChooser(shareIntent, "Share via")
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_share_24),
                                    contentDescription = "Share BMI"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculatorScreen(navController: NavController, bmiHistory: List<Pair<String, String>>, updateHistory: (List<Pair<String, String>>) -> Unit) {
    val context = LocalContext.current  // LocalContext should be used here
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    var bmiCategory by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("kg") }
    var heightUnit by remember { mutableStateOf("cm") }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val weightUnits = listOf("kg", "lbs")
    val heightUnits = listOf("cm", "m", "ft")
    fun shareBMIResult(bmi: String, category: String) {
        val shareText = "My BMI is $bmi and I am $category"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share your BMI"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
//            .background(Color(0xFFF0F4F8)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BMI Calculator",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            color = Color(0xFF1E2A47),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(0xFF1E2A47),
                    containerColor = Color.White
                )

        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(label = "Weight Unit", selectedOption = weightUnit, options = weightUnits) {
            weightUnit = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Enter Height") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color(0xFF1E2A47),
                containerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(label = "Height Unit", selectedOption = heightUnit, options = heightUnits) {
            heightUnit = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                errorMessage = ""
                // Trim whitespace and check if the values are valid numbers
                val weightValue = weight.trim().toFloatOrNull()
                val heightValue = height.trim().toFloatOrNull()

                // Check if the values are valid
                if (weightValue == null || heightValue == null || heightValue <= 0) {
                    // Determine the specific error
                    errorMessage = when {
                        weightValue == null -> "Please enter a valid weight"
                        heightValue == null -> "Please enter a valid height"
                        heightValue <= 0 -> "Height cannot be zero or negative"
                        else -> "Please enter valid height and weight"
                    }
                    showDialog = true
                    return@Button
                }

                // Convert weight and height based on the selected units
                val weightInKg = if (weightUnit == "lbs") weightValue / 2.205 else weightValue
                val heightInM = when (heightUnit) {
                    "cm" -> heightValue / 100
                    "ft" -> heightValue * 0.3048
                    else -> heightValue
                }

                // Calculate BMI
                val bmiValue = weightInKg.toDouble() / heightInM.toDouble().pow(2)
                bmi = String.format(Locale.US, "Your BMI is: %.2f", bmiValue)

                // Determine BMI category
                bmiCategory = when {
                    bmiValue < 18.5 -> "Underweight 😞"
                    bmiValue in 18.5..24.9 -> "Normal weight 😊"
                    bmiValue in 25.0..29.9 -> "Overweight 😞"
                    else -> "Obesity 😞"
                }

                // Update history
                val newEntry = bmi to bmiCategory
                val filteredHistory = bmiHistory.toMutableList()

// Add the new entry if it doesn't already exist
                if (newEntry !in bmiHistory) {
                    filteredHistory.add(newEntry)
                }

                updateHistory(filteredHistory)
                SharedPreferencesHelper.saveBMIHistory(context, filteredHistory) // Now using context properly inside composable

                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A47)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Calculate BMI", color = Color.White)
        }

    }

    // Bottom navigation updated to pass history
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Row to align icons horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Start // Align icons to the left
        ) {
            // Icon for third screen (BMI History)
            IconButton(
                onClick = { navController.navigate("third_screen") },
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Transparent)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Go to Third Screen",
                    tint = Color(0xFF1E2A47)
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // Adjust the space between the two icons

            // Icon for second screen (BMI Information)
            IconButton(
                onClick = { navController.navigate("second_screen") },
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Transparent)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Navigate to Second Screen",
                    tint = Color(0xFF1E2A47)
                )
            }
        }
    }

    // Show BMI Result or Error in a dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = if (errorMessage.isEmpty()) "BMI Result" else "Error") },
            text = {
                Column {
                    if (errorMessage.isEmpty()) {
                        Text(text = bmi, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium))
                        Text(text = bmiCategory, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal))
                    } else {
                        Text(text = errorMessage, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium))
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // This aligns OK to the left and Share to the right
                ) {
                    // OK Button
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text("OK", fontSize = 18.sp) // Slightly bigger text
                    }

                    // Conditionally show the Share Button if there's no error
                    if (errorMessage.isEmpty()) {
                        Button(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "My BMI is $bmi and I am $bmiCategory.")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share BMI via"))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text("Share", fontSize = 18.sp) // Same size and styling as OK button
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun SecondScreen() {
    var isLoading by remember { mutableStateOf(true) }

    // Simulate loading
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500) // Simulate a delay
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
//            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            // Show loading animation while the content is loading
            Box(
                modifier = Modifier
                    .fillMaxSize() // Take up the full screen
                    .padding(16.dp), // Optional padding
                contentAlignment = Alignment.Center // Use contentAlignment to center the spinner
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp), // Make it larger
                    color = Color(0xFF1E2A47)
                )
            }
        } else {
            // Content of the second screen
            Text(
                text = "BMI Information & Resources",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
                color = Color(0xFF1E2A47),
                modifier = Modifier.padding(bottom = 16.dp)
            )

        // Spacer to provide space between title and chart
        Spacer(modifier = Modifier.height(24.dp))

        // BMI Chart Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Adjusted height for better visual impact
                .background(Color.Gray)
                .padding(16.dp) // Padding inside the chart box
        ) {
            // Placeholder text or a chart can go here
            Text(
                text = "BMI Chart Placeholder",
                color = Color.White,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons for official links
        Button(
            onClick = {
                // Implement navigation to official BMI information link
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Padding for better button spacing
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A47)) // Consistent color
        ) {
            Text(
                text = "Learn More About BMI",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
        }

        Button(
            onClick = {
                // Implement navigation to official BMI chart link
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A47))
        ) {
            Text(
                text = "Official BMI Chart",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Text explaining BMI
        Text(
            text = "BMI (Body Mass Index) is a simple measure used to assess whether you are underweight, normal weight, overweight, or obese. It does not measure body fat directly but can indicate whether a person is at risk for various health conditions.",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp),
            color = Color(0xFF1E2A47),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Additional tips or health info
        Text(
            text = "Remember, BMI is just a guideline. Consult with a healthcare professional for personalized health advice.",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 22.sp),
            color = Color(0xFF1E2A47),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
}



@Composable
fun DropdownMenu(label: String, selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label)
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BMIPreview() {
    BMICalculatorApp()
}

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import android.content.Intent
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import kotlin.math.pow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorApp()
        }
    }
}

object SharedPreferencesHelper {

    private const val PREFS_NAME = "bmi_prefs"
    private const val KEY_BMI_HISTORY = "bmi_history"

    // Save BMI history to SharedPreferences
    fun saveBMIHistory(context: Context, bmiHistory: List<Pair<String, String>>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(bmiHistory)
        editor.putString(KEY_BMI_HISTORY, json)
        editor.apply()
    }

    // Load BMI history from SharedPreferences
    fun loadBMIHistory(context: Context): List<Pair<String, String>> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_BMI_HISTORY, "[]")
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        return Gson().fromJson(json, type)
    }
}

@Composable
fun BMICalculatorApp() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Load BMI history from SharedPreferences
    var bmiHistory by remember { mutableStateOf(SharedPreferencesHelper.loadBMIHistory(context)) }

    NavHost(
        navController = navController,
        startDestination = "bmi_calculator"
    ) {
        composable(
            "bmi_calculator",
            enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn(animationSpec = tween(1000)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut(animationSpec = tween(1000)) }
        ) {
            BMICalculatorScreen(navController, bmiHistory) { newHistory ->
                // Update bmiHistory and save to SharedPreferences
                bmiHistory = newHistory
                SharedPreferencesHelper.saveBMIHistory(context, newHistory)
            }
        }

        composable(
            "second_screen",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(1000)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(1000)) }
        ) {
            SecondScreen()
        }

        composable(
            "third_screen",
            enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn(animationSpec = tween(1000)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut(animationSpec = tween(1000)) }
        ) {
            ThirdScreen(bmiHistory = bmiHistory)
        }
    }
}


@Composable
fun ThirdScreen(bmiHistory: List<Pair<String, String>>) {
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current // Get context for launching the share intent

    // Simulate loading
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500) // Simulate a delay
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = Color(0xFF1E2A47)
                )
            }
        } else {
            Text(
                text = "BMI History",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                color = Color(0xFF1E2A47),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (bmiHistory.isEmpty()) {
                Text(
                    text = "No history available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                )
            } else {
                bmiHistory.forEach { (bmi, category) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = bmi,
                                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                                    color = Color(0xFF1E2A47)
                                )
                                Text(
                                    text = category,
                                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
                                    color = if (category.contains("😊")) Color.Green else Color.Red
                                )
                            }
                            IconButton(
                                onClick = {
                                    // Create a share intent
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, "BMI: $bmi, Category: $category")
                                    }
                                    context.startActivity(
                                        Intent.createChooser(shareIntent, "Share via")
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_share_24),
                                    contentDescription = "Share BMI"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculatorScreen(navController: NavController, bmiHistory: List<Pair<String, String>>, updateHistory: (List<Pair<String, String>>) -> Unit) {
    val context = LocalContext.current  // LocalContext should be used here
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    var bmiCategory by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("kg") }
    var heightUnit by remember { mutableStateOf("cm") }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val weightUnits = listOf("kg", "lbs")
    val heightUnits = listOf("cm", "m", "ft")
    fun shareBMIResult(bmi: String, category: String) {
        val shareText = "My BMI is $bmi and I am $category"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share your BMI"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
//            .background(Color(0xFFF0F4F8)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BMI Calculator",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            color = Color(0xFF1E2A47),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color(0xFF1E2A47),
                containerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(label = "Weight Unit", selectedOption = weightUnit, options = weightUnits) {
            weightUnit = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Enter Height") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color(0xFF1E2A47),
                containerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(label = "Height Unit", selectedOption = heightUnit, options = heightUnits) {
            heightUnit = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                errorMessage = ""
                // Trim whitespace and check if the values are valid numbers
                val weightValue = weight.trim().toFloatOrNull()
                val heightValue = height.trim().toFloatOrNull()

                // Check if the values are valid
                if (weightValue == null || heightValue == null || heightValue <= 0) {
                    // Determine the specific error
                    errorMessage = when {
                        weightValue == null -> "Please enter a valid weight"
                        heightValue == null -> "Please enter a valid height"
                        heightValue <= 0 -> "Height cannot be zero or negative"
                        else -> "Please enter valid height and weight"
                    }
                    showDialog = true
                    return@Button
                }

                // Convert weight and height based on the selected units
                val weightInKg = if (weightUnit == "lbs") weightValue / 2.205 else weightValue
                val heightInM = when (heightUnit) {
                    "cm" -> heightValue / 100
                    "ft" -> heightValue * 0.3048
                    else -> heightValue
                }

                // Calculate BMI
                val bmiValue = weightInKg.toDouble() / heightInM.toDouble().pow(2)
                bmi = String.format(Locale.US, "Your BMI is: %.2f", bmiValue)

                // Determine BMI category
                bmiCategory = when {
                    bmiValue < 18.5 -> "Underweight 😞"
                    bmiValue in 18.5..24.9 -> "Normal weight 😊"
                    bmiValue in 25.0..29.9 -> "Overweight 😞"
                    else -> "Obesity 😞"
                }

                // Update history
                val newEntry = bmi to bmiCategory
                val filteredHistory = bmiHistory.toMutableList()

// Add the new entry if it doesn't already exist
                if (newEntry !in bmiHistory) {
                    filteredHistory.add(newEntry)
                }

                updateHistory(filteredHistory)
                SharedPreferencesHelper.saveBMIHistory(context, filteredHistory) // Now using context properly inside composable

                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A47)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Calculate BMI", color = Color.White)
        }

    }

    // Bottom navigation updated to pass history
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Row to align icons horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Start // Align icons to the left
        ) {
            // Icon for third screen (BMI History)
            IconButton(
                onClick = { navController.navigate("third_screen") },
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Transparent)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Go to Third Screen",
                    tint = Color(0xFF1E2A47)
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // Adjust the space between the two icons

            // Icon for second screen (BMI Information)
            IconButton(
                onClick = { navController.navigate("second_screen") },
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Transparent)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Navigate to Second Screen",
                    tint = Color(0xFF1E2A47)
                )
            }
        }
    }

    // Show BMI Result or Error in a dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = if (errorMessage.isEmpty()) "BMI Result" else "Error") },
            text = {
                Column {
                    if (errorMessage.isEmpty()) {
                        Text(text = bmi, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium))
                        Text(text = bmiCategory, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal))
                    } else {
                        Text(text = errorMessage, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium))
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // This aligns OK to the left and Share to the right
                ) {
                    // OK Button
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text("OK", fontSize = 18.sp) // Slightly bigger text
                    }

                    // Conditionally show the Share Button if there's no error
                    if (errorMessage.isEmpty()) {
                        Button(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "My BMI is $bmi and I am $bmiCategory.")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share BMI via"))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text("Share", fontSize = 18.sp) // Same size and styling as OK button
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun SecondScreen() {
    var isLoading by remember { mutableStateOf(true) }

    // Simulate loading
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500) // Simulate a delay
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
//            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            // Show loading animation while the content is loading
            Box(
                modifier = Modifier
                    .fillMaxSize() // Take up the full screen
                    .padding(16.dp), // Optional padding
                contentAlignment = Alignment.Center // Use contentAlignment to center the spinner
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp), // Make it larger
                    color = Color(0xFF1E2A47)
                )
            }
        } else {
            // Content of the second screen
            Text(
                text = "BMI Information & Resources",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
                color = Color(0xFF1E2A47),
                modifier = Modifier.padding(bottom = 16.dp)
            )

        // Spacer to provide space between title and chart
        Spacer(modifier = Modifier.height(24.dp))

        // BMI Chart Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Adjusted height for better visual impact
                .background(Color.Gray)
                .padding(16.dp) // Padding inside the chart box
        ) {
            // Placeholder text or a chart can go here
            Text(
                text = "BMI Chart Placeholder",
                color = Color.White,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons for official links
        Button(
            onClick = {
                // Implement navigation to official BMI information link
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Padding for better button spacing
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A47)) // Consistent color
        ) {
            Text(
                text = "Learn More About BMI",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
        }

        Button(
            onClick = {
                // Implement navigation to official BMI chart link
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A47))
        ) {
            Text(
                text = "Official BMI Chart",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Text explaining BMI
        Text(
            text = "BMI (Body Mass Index) is a simple measure used to assess whether you are underweight, normal weight, overweight, or obese. It does not measure body fat directly but can indicate whether a person is at risk for various health conditions.",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp),
            color = Color(0xFF1E2A47),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Additional tips or health info
        Text(
            text = "Remember, BMI is just a guideline. Consult with a healthcare professional for personalized health advice.",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 22.sp),
            color = Color(0xFF1E2A47),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
}



@Composable
fun DropdownMenu(label: String, selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label)
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BMIPreview() {
    BMICalculatorApp()
}
