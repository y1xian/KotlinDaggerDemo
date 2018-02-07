# KotlinDaggerDemo
文笔不好，有不懂的属性可以去百度，这里不作过多解释

######导入
没啥好说的，需要注意的就是别忘了加上
```
  apply plugin: 'kotlin-kapt'

  kapt {
      generateStubs = true
  }
```
在dependencies里 （我这里用的2.13 有最新就用最新的吧）
```
    //dagger2
    implementation 'com.google.dagger:dagger:2.13'
    kapt 'com.google.dagger:dagger-compiler:2.13'
    implementation 'com.google.dagger:dagger-android:2.13'
    implementation 'com.google.dagger:dagger-android-support:2.13'
    kapt 'com.google.dagger:dagger-android-processor:2.13'
```

######进入主题

Module
这里写了个ActivityBuilderModule
```
@Module
abstract class ActivityBuilderModule {

    @Singleton
    @Binds
    abstract fun application(app: MyApplication): Context

    @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
    abstract fun bindMainActivity(): MainActivity

}

/**
 *  Activity里的每一个Fragment，要在module里面如下声明
 *  并且Fragment还有用@Inject标注其无参构造方法
 */

@Module
abstract class MainModule {

    @Binds
    abstract fun bindMainFragment(mainFragment: MainFragment): Fragment

    @ContributesAndroidInjector
    abstract fun bindMainFragment(): MainFragment

}
```

Component
只需创建一个实例，有子组件可以写在modules里
AndroidSupportInjectionModule 这个module是来自Dagger.Android 用来帮助生成和定位SupComponents(子组件)

```
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
```

别忘了Make Project
 Component会在自己生成一个 （Dagger+你的Component），本文的是DaggerAppComponent，然后在Application创建单例的Component
######Application
1.你可以直接继承 DaggerApplication 
2.如本文，继承HasActivityInjector实现，否则编译不过
```
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
```
继承HasActivityInjector
```
class MyApplication : Application() , HasActivityInjector {

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
```
######注入Activity和Fragment
1.你可以直接继承DaggerAppCompatActivity、DaggerFragment
2.创建BaseActivity、BaseFragment 然后继承...如下文
```
//BaseActivity
abstract class BaseActivity : AppCompatActivity(), HasFragmentInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return supportFragmentInjector
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment> {
        return frameworkFragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}

//BaseFragment
abstract class BaseFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childFragmentInjector
    }

    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Perform injection here before M, L (API 22) and below because onAttach(Context)
            // is not yet available at L.
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(activity)
    }

    override fun onAttach(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(context)
    }

}
```
Activity 
ps:lateinit前不要用private 否则编译报错
```
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
```

Fragment
@Inject标注其无参构造方法
```
class MainFragment @Inject constructor(): BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

}
```

######总结坑点
1. DaggerAppComponent爆红不要急，Make Project就出来了（如果能顺利编译）
2. lateinit 前不要用 private 否则编译报错
3. @Inject标注其无参构造方法 （@Inject constructor()）,然后在Module @Binds
4. 实在编译不过，再看一下本文 或 看下[GitHub](https://github.com/y1xian/KotlinDaggerDemo)上的项目
