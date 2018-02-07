package com.yyx.kotlindaggerdemo.di

import android.support.v4.app.Fragment
import com.yyx.kotlindaggerdemo.MainFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 *  Activity里的每一个Fragment，要在module里面如下声明
 *  并且Fragment还有用@Inject标注其无参构造方法
 * @author yyx
 * @date 2018/2/5
 */
@Module
abstract class MainModule {

    @Binds
    abstract fun bindMainFragment(mainFragment: MainFragment): Fragment

    @ContributesAndroidInjector
    abstract fun bindMainFragment(): MainFragment

}