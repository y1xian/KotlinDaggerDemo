package com.yyx.kotlindaggerdemo

import android.os.Bundle
import com.yyx.kotlindaggerdemo.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var mMainFragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_content, mMainFragment)
                .commit()
    }
}
