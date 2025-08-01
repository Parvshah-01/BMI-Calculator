# BMICalculator

BMICalculator is an Android app built with Jetpack Compose that allows users to calculate their Body Mass Index (BMI), view BMI history, and access BMI information and resources.

## Features
- Calculate BMI using weight and height in various units (kg/lbs, cm/m/ft)
- View BMI category (Underweight, Normal, Overweight, Obesity) with emoji feedback
- Save and view BMI history
- Share BMI results
- Animated transitions between screens
- Material 3 design with custom theme
- Information and resources about BMI

## Screens
- **BMI Calculator:** Input weight and height, select units, calculate BMI, and view result
- **BMI History:** View previous BMI calculations and share results
- **BMI Information:** Learn about BMI and access official resources

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/Parvshah-01/BMI-Calculator.git
   ```
2. Open the project in Android Studio
3. Build and run on an emulator or device (minSdk 24, targetSdk 34)

## Usage
- Enter your weight and height, select units, and tap "Calculate BMI"
- View your BMI result and category
- Access BMI history and share results
- Learn more about BMI from the information screen

## Dependencies
- Jetpack Compose (Material3, Animation, Navigation)
- AndroidX Core, Lifecycle, Activity Compose
- Gson (for saving history)
- JUnit, Espresso (for testing)

## Project Structure
- `app/src/main/java/com/example/bmicalculator/` - Main app code
- `ui/theme/` - Theme and color definitions
- `res/` - Resources (drawables, values, icons)
- `test/` and `androidTest/` - Unit and instrumented tests
- Gradle build scripts and wrapper

## License
This project is licensed under the Apache License 2.0. See [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) for details.

## Author
- Parv

---
