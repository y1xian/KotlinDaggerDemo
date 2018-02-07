package com.yyx.kotlindaggerdemo.di

import android.content.Context
import com.yyx.kotlindaggerdemo.MainActivity
import com.yyx.kotlindaggerdemo.MyApplication
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

/**
 *
 * @author yyx
 * @date 2018/2/2
 */
@Module
abstract class ActivityBuilderModule {

    @Singleton
    @Binds
    abstract fun application(app: MyApplication): Context

    @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
    abstract fun bindMainActivity(): MainActivity

}