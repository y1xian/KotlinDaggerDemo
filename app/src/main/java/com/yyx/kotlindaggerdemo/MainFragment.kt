package com.yyx.kotlindaggerdemo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyx.kotlindaggerdemo.base.BaseFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class MainFragment @Inject constructor(): BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

}
