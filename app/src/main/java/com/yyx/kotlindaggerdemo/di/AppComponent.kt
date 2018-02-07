package com.yyx.kotlindaggerdemo.di

import android.app.Application
import com.yyx.kotlindaggerdemo.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 *
 * @author yyx
 * @date 2018/2/2
 */
@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, ActivityBuilderModule::class))
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }

    fun inject(instance: MyApplication)
}