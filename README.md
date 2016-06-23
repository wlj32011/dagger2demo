###前言
依赖注入概念网络有很多解释，这里就不详细介绍，本文通过一个简单的示例一步步深入了解依赖注入的优势以及为什么使用依赖注入。
###概念
依赖注入（Dependency Injection），简称DI，又叫控制反转（Inversion of Control），简称IOC。
当一个类的实例需要另另一个类的实例进行协助时，在传统的设计中，通常由调用者来创建被调用者的实例，然而依赖注入的方式，创建被调用者不再由调用者创建实例，创建被调用者的实例的工作由IOC容器来完成，然后注入到调用者。因此也被称为依赖注入。
###作用
将各层的对象以松耦合的方式组织在一起，解耦，各层对象的调用完全面向接口。当系统重构或者修改的时候，代码的改写量将大大减少。

####Android Studio 引入Dagger2
1.在工程根目录的build.gradle引入apt插件
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
整体文件：
```gradle
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.0-alpha1'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

2.在app目录下的build.gradle加上一行

apply plugin: 'com.neenbedankt.android-apt'

dependencies下面加上：
    apt 'com.google.dagger:dagger-compiler:2.2'
    provided 'org.glassfish:javax.annotation:10.0-b28'
    compile 'com.google.dagger:dagger:2.2'
  
别忘了加入lint warning
lintOptions {
        warning 'InvalidPackage'
    }
    
    
整体文件：
```gradle
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'

android {
    compileSdkVersion 23
    buildToolsVersion "23.0.3"
    defaultConfig {
        applicationId "com.iiseeuu.dagger2demo"
        minSdkVersion 15
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
    lintOptions {
        warning 'InvalidPackage'
    }
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    compile 'com.android.support:appcompat-v7:23.4.0'
    compile 'com.android.support.constraint:constraint-layout:1.0.0-alpha1'
    testCompile 'junit:junit:4.12'
    androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2.2'
    androidTestCompile 'com.android.support.test:runner:0.5'
    androidTestCompile 'com.android.support:support-annotations:23.4.0'
    apt 'com.google.dagger:dagger-compiler:2.2'
    provided 'org.glassfish:javax.annotation:10.0-b28'
    compile 'com.google.dagger:dagger:2.2'
    compile 'com.google.code.gson:gson:2.7'
}

```


####@Inject介绍
注解(Annotation)来标注目标类中所依赖的其他类，同样用注解来标注所依赖的其他类的构造函数，那注解的名字就叫Inject


**使用dagger2之前**
```java
public class A {
    private String field;

    public A(){

    }

    public void doSomething(){
        Log.e("A", "do something");
    }
}
//Activity调用
public class MainActivity extends AppCompatActivity {

    A a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = new A();
        a.doSomething();
    }
}
```
**使用dagger2之后**
```java
public class A {
    private String field;

    @Inject
    public A(){

    }

    public void doSomething(){
        Log.e("A", "do something");
    }
}
//Activity调用
public class MainActivity extends AppCompatActivity {

    @Inject
    A a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        a.doSomething();
    }
}
```
这样写直接运行的话 是会报错的，变量a没有被实例化，会报空指针错误。因为他们没有任何关联。这个时候我们需要把MainActivity和A类关联起来，这个时候就需要@Commponent了，下面请看@Commponent：

####@Commponent
A类的构造函数与调用类Activity都使用Inject注解，Component一般是个接口，就是将MainActivity与A类桥接起来。
我们定义一个AComponent接口，并使用@Component注解:
```java
@Component
public interface AComponent {
    A a();
}

//Activity调用
public class MainActivity extends AppCompatActivity {

    @Inject
    A a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = DaggerAComponent.builder().build().a();

        a.doSomething();
    }
}
```
AComponent会查找MainActivity中用Inject注解标注的属性，查找到相应的属性后会接着查找该属性对应的用Inject标注的构造函数（这时候就发生联系了），剩下的工作就是初始化该属性的实例并把实例进行赋值。因此我们也可以给Component叫另外一个名字注入器（Injector）

Component现在是一个注入器，就像注射器一样，Component会把A的实例注入到MainActivity中，来初始化MainActivity类中的依赖。

a = DaggerAComponent.builder().build().a();这种写法不太友好，一般情况下，我们只需要将MainActivity的实例交给AComponent引用即可。

```java
public class MainActivity extends AppCompatActivity {

    @Inject
    A a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerAComponent.builder().build().inject(this);

        a.doSomething();  
    }
}
```


####@Module
Module的职责是用来生成实例，可以把他比作一个工厂，专业生成各种类的实例。

项目中使用到了第三方的类库，第三方类库又不能修改，所以根本不可能把Inject注解加入这些类中，这时我们的Inject就失效了。

比如我项目中依赖GSON解析库。这个时候我需要新建一个类一个提供一个Gson的实例。
```java
@Module
public class AModule {

    @Provides
    public Gson provideGson(){
        return new Gson();
    }
}
//component类 引用module为AModule.class
@Component(modules = AModule.class)
public interface AComponent {
    void inject(MainActivity mainActivity);
}

//activity调用
public class MainActivity extends AppCompatActivity {

    @Inject
    A a;

    @Inject
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerAComponent.builder().build().inject(this);

        a.field = "test";
        String aStr = gson.toJson(a);
        Log.e("mainactivity","astr = "+aStr);
        a.doSomething();
    }
}
```
####@Provides
module类对外提供实例方法的注解


####注意：
1.  component首先先去提供的module里面寻找提供的实例，没有找到时再去找构造函数@inject注解
2.  一个component可以依赖多个module，一个component可以依赖另一个component
例如：AModule 提供A类的实例，GsonModule提供Gson的实例
```java
//AModule.java
@Module
public class AModule {

    @Provides
    public A provideA(){
        return new A();
    }
}
//GsonModule.java
@Module
public class GsonModule {

    @Provides
    public Gson provideGson(){
        return new Gson();
    }
}
//component
@Component(modules = {AModule.class,GsonModule.class})
public interface AComponent {
    void inject(MainActivity mainActivity);
}
//调用activity不变
...
```
####@Scope and @Singleton
这个注解是用来划分作用域的，标记当前对象使用范围。
比如限制对象只能在所有Activity中使用，或者只能在Application中使用，或者只能在Fragment中使用
@Singleton 单例模式全局共用一个对象 就是@Scope的一个实现。

这个Scope比较难以理解，我们举个例子自定义一个Scope：
假如有个项目包含用户体系，用户登录成功后，A界面、B界面和C界面要依赖用户来获取一些数据，LoginActivity界面不依赖于用户体系。
我们想要把User对象实例可以在A、B、C界面共用。

那么整体项目的Scope划分结果图为：
![untitled.jpg](https://raw.githubusercontent.com/wlj32011/dagger2demo/master/untitled.jpg)


1.自定义UserScope注解
```java
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {

}
```
2.新建一个User实体类
```java
public class User {
    private String name;
    private String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
```
3.新建一个UserModule来提供User的实例,提供实例方法使用自定义的UserScope注解，表示提供实例仅限于UserScope范围内使用。
```java
@Module
public class UserModule {
    private User user;

    public UserModule(User user) {
        this.user = user;
    }

    @Provides
    @UserScope
    User provideUser(){
        return user;
    }

}
```
4.新建Component桥梁。他是一个子Component,依赖于一个全局的父Component。
Subcomponent注解与Component依赖另一个Component有点像，他们区别在于：
Subcomponent可以获取到父Component的所有可以产生出的对象。
Component依赖则只能获取到被依赖的Component所暴露出来的可以生产的对象

```java
@UserScope
@Subcomponent(modules = UserModule.class)
public interface UserComponent {
    AComponent plus(AModule aModule);
    BComponent plus(BModule bModule);
    CComponent plus(CModule cModule);
}
```
5.新建一个全局的父Component，引用子Component。
```java
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    UserComponent plus(UserModule userModule);
}
```
6.新建一个AppModule,提供一个全局的application实例
```java
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }
}
```
7.创建一个App实例，在程序启动时，调用它。
```java
public class App extends Application{
    //application组件
    private AppComponent appComponent;
    //用户组件
    private UserComponent userComponent;

    //获取当前application的实例
    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注入全局Application
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    //对外提供UserComponent
    public UserComponent getUserComponent() {
        return userComponent;
    }

    //注入UserComponent，调用此方法后，UserCope生效
    public UserComponent createUserComponent(User user) {
        userComponent = appComponent.plus(new UserModule(user));
        return userComponent;
    }
    //释放UserComponent组件
    public void releaseUserComponent() {
        userComponent = null;
    }
}

```
8.LoginActivity 点击登录按钮，创建User实例，并开始UserScope生命周期
```java
findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setName(etUserName.getText().toString());
                user.setAvatar(etPassword.getText().toString());

                App.get(MainActivity.this).createUserComponent(user);
                startActivity(new Intent(MainActivity.this, AActivity.class));
            }
        });
```
9.AActivity、BActivity、CActivity  使用User对象为同一个User对象，LoginActivity是没有权限使用User对象的。
下面为详细代码：
```java
//AActivity.java
public class AActivity extends AppCompatActivity{
    @Inject
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        //注入
        App.get(this).getUserComponent().plus(new AModule()).inject(this);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("username:"+user.getName()+"user:"+user);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AActivity.this, BActivity.class));
            }
        });

    }

}
//BActivity.java
public class BActivity extends AppCompatActivity{
    @Inject
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        App.get(this).getUserComponent().plus(new BModule()).inject(this);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("username:"+user.getName()+"--user:"+user);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BActivity.this, CActivity.class));
            }
        });
    }


}

//CActivity.java
public class CActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.get(CActivity.this).releaseUserComponent();
                startActivity(new Intent(CActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}


```
10.最终效果，打印出User对象地址都是同一个地址:user@1524f0fa
![device-2016-06-23-144101.png](https://raw.githubusercontent.com/wlj32011/dagger2demo/master/device-2016-06-23-144101.png)
![device-2016-06-23-144137.png](https://raw.githubusercontent.com/wlj32011/dagger2demo/master/device-2016-06-23-144137.png)
![device-2016-06-23-144101.png](https://raw.githubusercontent.com/wlj32011/dagger2demo/master/device-2016-06-23-144149.png)

####@Qualifier and @Named 
@Named 其实是@Qualifier的一种实现，弄明白@Qualifier(限定符)基本上也就明白了@Named
@Qualifier(限定符)主要作用是用来区分不同对象实例。

假如上面示例系统支持多用户，在Activity中引用了两个不同的User实例，我们该怎么区分呢？

1.首先我们自定义一个@Qualifier 名称叫做UserNamed

```java
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNamed {
    String value() default "";
}
```
2.修改UserModule对外提供两个User实例userA和UserB,并使用@UserNamed注解标识实例
```java
@Module
public class UserModule {
    private User userA;
    private User userB;

    public UserModule(User userA,User userB) {
        this.userB = userB;
        this.userA = userA;
    }

    @UserNamed("a")
    @Provides
    @UserScope
    User provideUserA(){
        return userA;
    }
    @UserNamed("b")
    @Provides
    @UserScope
    User provideUserB(){
        return userB;
    }

}

```
3.修改App.java的createUserComponent 传入两个实例
```java
 public UserComponent createUserComponent(User userA,User userB) {
        userComponent = appComponent.plus(new UserModule(userA,userB));
        return userComponent;
    }
```    
4.登录时创建2个实例
```java
findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User userA = new User();
                userA.setName(etUserName.getText().toString()+"AAA");
                userA.setAvatar(etPassword.getText().toString());

                User userB = new User();
                userB.setName(etUserName.getText().toString()+"BBB");
                userB.setAvatar(etPassword.getText().toString());

                App.get(MainActivity.this).createUserComponent(userA,userB);
                startActivity(new Intent(MainActivity.this, AActivity.class));
            }
        });
```
5.Activity中的使用：
```java
public class AActivity extends AppCompatActivity{
    @UserNamed("a")
    @Inject
    User userA;

    @UserNamed("b")
    @Inject
    User userB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        //注入
        App.get(this).getUserComponent().plus(new AModule()).inject(this);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("username:"+userA.getName()+"--user:"+userA+""+"username:"+userB.getName()+"--user:"+userB);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AActivity.this, BActivity.class));
            }
        });

    }

}

```

我们在回头看下我们自定义的@UserNamed与系统定义的@Named源码的区别大家应该就能明白了~两个类的实现是一样的代码
```java
//@UserNamed定义：
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNamed {
    String value() default "";
}

//@Named的定义：
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Named {

    /** The name. */
    String value() default "";
}

```

dagger2 主要功能 就此介绍完毕，如果觉得不错，就尽快用起来吧~

####参考：
<http://google.github.io/dagger/>
<http://frogermcs.github.io/>
####demo地址：
<https://github.com/wlj32011/dagger2demo>