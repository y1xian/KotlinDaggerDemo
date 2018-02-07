package com.yyx.kotlindaggerdemo

import android.app.Activity
import android.app.Application
import com.yyx.kotlindaggerdemo.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 *
 * @author yyx
 * @date 2018/2/7
 */
class MyApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this)
                .build().inject(this)
    }

}