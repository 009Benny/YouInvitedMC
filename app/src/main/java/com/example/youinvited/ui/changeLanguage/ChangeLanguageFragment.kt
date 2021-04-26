package com.example.youinvited.ui.changeLanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.youinvited.PrincipalActivity
import com.example.youinvited.R
import kotlinx.android.synthetic.main.changelanguage_fragment.*

class ChangeLanguageFragment : Fragment(), View.OnClickListener{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.changelanguage_fragment, container, false)
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