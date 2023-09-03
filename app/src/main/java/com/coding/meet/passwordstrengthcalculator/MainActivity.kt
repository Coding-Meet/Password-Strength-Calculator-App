package com.coding.meet.passwordstrengthcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.coding.meet.passwordstrengthcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    enum class StrengthLevel {
        WEAK,
        MEDIUM,
        STRONG,
        VERY_STRONG
    }

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        // add a text change listener to the password input field
        mainBinding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(password: CharSequence, p1: Int, p2: Int, p3: Int) {
                // Remove Space
                if (password.trim().isNotEmpty()) {

                    // Check for lowercase letters
                    val hasLowerCase = password.any { it.isLowerCase() }
                    updateStatusUI(hasLowerCase, mainBinding.lowerCaseImg, mainBinding.lowerCaseTxt)

                    // Check for Uppercase letters
                    val hasUpperCase = password.any { it.isUpperCase() }
                    updateStatusUI(hasUpperCase, mainBinding.upperCaseImg, mainBinding.upperCaseTxt)

                    // Check for digit
                    val hasDigit = password.any { it.isDigit() }
                    updateStatusUI(hasDigit, mainBinding.digitImg, mainBinding.digitTxt)

                    // Check for Special characters
                    val hasSpecialChar =
                        password.contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\",./<>?\\\\|]"))
                    updateStatusUI(
                        hasSpecialChar,
                        mainBinding.specialCharImg,
                        mainBinding.specialCharTxt
                    )


                    calculateStrength(
                        password, hasLowerCase, hasUpperCase, hasDigit, hasSpecialChar
                    )
                }else{
                    mainBinding.strengthLevelTxt.text = ""
                    mainBinding.strengthLevelTxt.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.darkGray))
                    mainBinding.strengthLevelInd.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.darkGray))

                    updateStatusUI(false, mainBinding.lowerCaseImg, mainBinding.lowerCaseTxt)
                    updateStatusUI(false, mainBinding.upperCaseImg, mainBinding.upperCaseTxt)
                    updateStatusUI(false, mainBinding.digitImg, mainBinding.digitTxt)
                    updateStatusUI(
                        false,
                        mainBinding.specialCharImg,
                        mainBinding.specialCharTxt
                    )
                }
            }
            override fun afterTextChanged(password: Editable) {}
        })


        mainBinding.continueBtn.setOnClickListener {
            Toast.makeText(this,"Continue",Toast.LENGTH_LONG).show()
        }

    }

    private fun displayStrengthLevel(strengthLevel: StrengthLevel, strengthColor: Int) {
        mainBinding.continueBtn.isEnabled = StrengthLevel.VERY_STRONG == strengthLevel || StrengthLevel.STRONG == strengthLevel


        mainBinding.strengthLevelTxt.text = strengthLevel.name
        mainBinding.strengthLevelTxt.setTextColor(ContextCompat.getColor(this, strengthColor))
        mainBinding.strengthLevelInd.setBackgroundColor(ContextCompat.getColor(this, strengthColor))

    }

    private fun calculateStrength(
        password: CharSequence,
        hasLowerCase: Boolean,
        hasUpperCase: Boolean,
        hasDigit: Boolean,
        hasSpecialChar: Boolean,
    ) {

        val strengthLevel : StrengthLevel
        val strengthColor : Int

        when (password.length){
            in 0..7 ->{
                strengthLevel = StrengthLevel.WEAK
                strengthColor = R.color.weak
            }
            in 8 .. 10 -> {
                if (hasLowerCase || hasUpperCase || hasDigit || hasSpecialChar){
                    strengthLevel = StrengthLevel.MEDIUM
                    strengthColor = R.color.medium
                }else{
                    strengthLevel = StrengthLevel.WEAK
                    strengthColor = R.color.weak
                }
            }
            in 11 .. 16 -> {
                if (hasLowerCase && hasUpperCase){
                    strengthLevel = StrengthLevel.STRONG
                    strengthColor = R.color.strong
                }else if (hasLowerCase || hasUpperCase || hasDigit || hasSpecialChar){
                    strengthLevel = StrengthLevel.MEDIUM
                    strengthColor = R.color.medium
                }else{
                    strengthLevel = StrengthLevel.WEAK
                    strengthColor = R.color.weak
                }
            }else -> {
                if (hasLowerCase && hasUpperCase && hasDigit && hasSpecialChar){
                    strengthLevel = StrengthLevel.VERY_STRONG
                    strengthColor = R.color.very_strong
                }else if (hasLowerCase && hasUpperCase){
                    strengthLevel = StrengthLevel.STRONG
                    strengthColor = R.color.strong
                }else if (hasLowerCase || hasUpperCase || hasDigit || hasSpecialChar){
                    strengthLevel = StrengthLevel.MEDIUM
                    strengthColor = R.color.medium
                }else{
                    strengthLevel = StrengthLevel.WEAK
                    strengthColor = R.color.weak
                }
            }

        }
        displayStrengthLevel(strengthLevel,strengthColor)


    }

    private fun updateStatusUI(
        condition: Boolean,
        imageView: ImageView,
        textView: TextView,
    ) {
        if (condition) {
            imageView.setImageResource(R.drawable.ic_correct)
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.very_strong))
            textView.setTextColor(ContextCompat.getColor(this, R.color.very_strong))

        } else {
            imageView.setImageResource(R.drawable.ic_wrong)
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.darkGray))
            textView.setTextColor(ContextCompat.getColor(this, R.color.darkGray))
        }
    }
}