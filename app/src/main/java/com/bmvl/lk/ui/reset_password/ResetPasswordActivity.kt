package com.bmvl.lk.ui.reset_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bmvl.lk.R
import com.bmvl.lk.Rest.NetworkService
import com.bmvl.lk.Rest.StandardAnswer
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = baseContext.resources.getString(R.string.Recovery_password_title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        input_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
              if(isUserNameValid(s.toString())){
                  resetPasswordBtn.isEnabled = true
              } else {
                  resetPasswordBtn.isEnabled = false
                  input_email.error = baseContext.resources.getString(R.string.invalid_email)
              }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }
    fun isUserNameValid(email :String): Boolean{
        if(email.contains("@"))
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        else return false;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun resetPassword(view: View) {
        NetworkService.getInstance()
                .jsonApi
                .resetPassword(input_email.text.toString())
                .enqueue(object : Callback<StandardAnswer> {
                    override fun onResponse(call: Call<StandardAnswer>, response: Response<StandardAnswer>) {
                        if (response.isSuccessful && response.body() != null)
                            if(response.body()!!.status == 200.toShort()) {
                                Toast.makeText(applicationContext, "Письмо успешно отправлено", Toast.LENGTH_SHORT).show()
                                finish()
                            } else
                                Toast.makeText(applicationContext, "Пользователь с таким E-mail не найден в системе", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<StandardAnswer>, t: Throwable) {
                        Toast.makeText(applicationContext, R.string.server_lost, Toast.LENGTH_SHORT).show()
                    }
                })
    }
}