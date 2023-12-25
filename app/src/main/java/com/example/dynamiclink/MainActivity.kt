package com.example.dynamiclink

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var send :TextView
    var shortLink=""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       send= findViewById(R.id.sendBtn)

        val intentFilter= IntentFilter(Intent.ACTION_VIEW)
        intentFilter.addDataScheme("https")
        intentFilter.addDataAuthority("google.com",null)
        registerReceiver(dynamiclinksReceiver,intentFilter)
        dynamiclinksReceiver()
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://google.com/userName?name=Jahid Raj&email=jahid.raj1132@gmail.com")
            domainUriPrefix = "https://dynamic6162.page.link"
            androidParameters {  }
            iosParameters("com.example.ios"){
            }
        }
            .addOnSuccessListener {

                shortLink= it.shortLink.toString()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
        send.setOnClickListener{
            val intent= Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_SUBJECT,"Firebase Dynamic Link")
            intent.putExtra(Intent.EXTRA_TEXT,shortLink)
//            startActivity(intent)
            startActivity(Intent.createChooser(intent,"Dynamic link App"))
        }
    }
    //Implement dynamiclinkReceiver to handle incoming links
    private  val dynamiclinksReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {
            //handle dynamic links data here
        }

    }
    private fun dynamiclinksReceiver() {

        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener {pendingDynamicLinkData ->

                var deepLink:Uri?= null
                if (pendingDynamicLinkData!= null){

                    deepLink = pendingDynamicLinkData.link
                }
                if (deepLink!=null){
                    val name= deepLink.getQueryParameter("name")
                    val email= deepLink.getQueryParameter("email")
                    send.text = "$name \n $email"
                }

            }
    }
}