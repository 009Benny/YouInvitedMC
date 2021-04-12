package com.example.youinvited.ui.slideshow

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.youinvited.PrincipalActivity
import com.example.youinvited.R
import kotlinx.android.synthetic.main.fragment_slideshow.*

class SlideshowFragment : Fragment(), View.OnClickListener{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_slideshow, container, false)

//        slideshowViewModel =
//            ViewModelProvider(this).get(SlideshowViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
//        btnLanguage.setOnClickListener {
//            Toast.makeText(activity, "Hola", Toast.LENGTH_SHORT).show()
//        }
//        return root
    }

    override fun onStart() {
        super.onStart()
        btnLanguage.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(p0 == this.btnLanguage){
            val act = activity as PrincipalActivity
            act.changeLanguage()
        }
    }

}