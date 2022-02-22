# LmySkinSwitcher使用文档

# 如何导入:

[![](https://www.jitpack.io/v/Ellen2018/LmySkinSwitcher.svg)](https://www.jitpack.io/#Ellen2018/LmySkinSwitcher)

```
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```

```
	dependencies {
	        implementation 'com.github.Ellen2018:LmySkinSwitcher:x.y.z'
	}
```

&emsp;&emsp;该轮子借鉴网易云换肤方案原理封装的一套，使用分为以下几个步骤:

- 1.Application里初始化
- 2.设置换肤的Setting:LmySkinSwitcherSetting，重写它的各种方法
- 3.设置皮肤切换的具体逻辑:实现接口LmySkinRefresh
- 4.调用LmySkinManager.getInstance().switchSkin方法进行换肤

&emsp;&emsp;下面我们看看具体使用:

## 1.Application里初始化

```
public class App extends Application {

    public static  App INSTANCE = null;



    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        SharePreferenceHelper.getInstance().init(this);
        LmySkinManager.getInstance().initApp(this);
    }
}
```

## 2.设置换肤的Setting:LmySkinSwitcherSetting，重写它的各种方法

&emsp;&emsp;首先我们先定义一个类去实现LmySkinSwitcherSetting接口:

```
public class MyLmySkinSwitcherSetting implements LmySkinSwitcherSetting {

    /**
     * 具体设置皮肤属性的逻辑
     * @return
     */
    @Override
    public LmySkinRefresh lmySkinRefresh() {
        //具体设置皮肤属性的逻辑
        return new MyLmySkinRefresh();
    }

    /**
     * 过滤皮肤切换要用到的属性
     * @return
     */
    @Override
    public List<String> interceptFieldNames() {
        List<String>  xmlFields = new ArrayList<>();
        //背景属性拦截
        xmlFields.add("background");
        //文本字颜色拦截
        xmlFields.add("textColor");
        //TabLayout
        xmlFields.add("tabIndicatorColor");
        xmlFields.add("tabSelectedTextColor");
        return xmlFields;
    }

    /**
     * 屏蔽某些Activity不进行重构
     * @return
     */
    @Override
    public List<Class<? extends Activity>> shieldActivityList() {
        //屏蔽重构的界面
        List<Class<? extends Activity>> shieldActivityList = new ArrayList<>();
        shieldActivityList.add(SkinManagerActivity.class);
        return shieldActivityList;
    }

    /**
     * 应用启动时使用的皮肤
     * @return
     */
    @Override
    public LmySkin applyAppLaunchSkin() {
        //获取用户是否已切换的皮肤
        LmySkin lmySkin = SharePreferenceHelper.getInstance().getCurrentSaveSkin(App.INSTANCE);
        return lmySkin;
    }

    /**
     * 获取默认皮肤
     * @return
     */
    @Override
    public LmySkin defaultSkin() {
       return getDefaultSkin();
    }

    /**
     * 设置能解析的皮肤包格式
     * @return
     */
    @Override
    public List<String> skinFormats() {
        List<String> skinFormats = new ArrayList<>();
        skinFormats.add(".app");
        skinFormats.add(".apk");
        return skinFormats;
    }

    //此方法读者可以自行方式去实现，定义默认皮肤
    public static LmySkin getDefaultSkin(){
        return new LmySkin("default","default");
    }
}
```

&emsp;&emsp;各种方法及其作用如下:

- lmySkinRefresh():此方法需要返回一个LmySkinRefresh类型对象，具体去实现皮肤切换的逻辑
- interceptFieldNames():此方法需要返回一个Lsit<String>对象，过滤皮肤切换需要的属性，对应XML中对应的background,textColor,res等
- shieldActivityList():此方法需要返回一个Lsit<? extends Activity>对象，屏蔽不刷新的界面，因为刷新界面使用的recreate()方法，它会导致闪屏现象，用户体验感不好，因此我们应该不让皮肤切换界面调用此方法，进行手动逻辑，这样就没有闪屏现象的同时，达到皮肤秒切效果。
- applyAppLaunchSkin():此方法返回一个LmySkin,作用是应用启动时使用的皮肤
- defaultSkin():此方法需返回一个LmySkin,作用是定义默认皮肤，你可以自己任意定义
- skinFormats():此方法需要返回一个Lsit<String>对象，作用是指定皮肤包对应的格式，例如:.apk。注意一定要格式对应，否则，会出现格式错误异常SkinFileFormatException

## 3.设置皮肤切换的具体逻辑:实现接口LmySkinRefresh

&emsp;&emsp;该接口只存在一个方法refresh(LmyRefreshSkin lmyRefreshSkin),此方法中LmyRefreshSkin类里有一些我们换肤时需要的，其实现代码如下:

```
/**
 * 刷新皮肤提供的实体类
 */
public class LmyRefreshSkin {
    //对应皮肤的资源,换肤时这个非常重要
    private Resources resources;
    //对应过滤出的控件对象
    private View view;
    //控件对象使用的资源id,换肤时这个非常重要
    private int resId;
    //xml中对应的属性名，例如:background
    private String attributeName;
    //xml中对应属性使用的value,无作用，因为笔者将其解析到resId
    private String attributeValue;
    //XML解析过后的属性与Value值都存在在它里面了，无作用
    private AttributeSet attributeSet;

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public AttributeSet getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(AttributeSet attributeSet) {
        this.attributeSet = attributeSet;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
```

&emsp;&emsp;其实我们在写换肤逻辑时，仅仅需要4个参数即可完成换肤:

- 1.换肤对象:View对象，对应LmyRefreshSkin中的view成员属性
- 2.换肤属性:对应xml中的属性，例如:background，对应LmyRefreshSkin中的attributeName成员属性
- 3.换肤资源对象:Resource，对应LmyRefreshSkin中的resources成员属性
- 4.换肤资源id:换肤属性对应使用的资源id，对应LmyRefreshSkin中的resId成员属性

&emsp;&emsp;接下来我们就来实现LmySkinRefresh接口:

```
public class MyLmySkinRefresh implements LmySkinRefresh {
    @Override
    public void refresh(LmyRefreshSkin lmyRefreshSkin) {
        View view = lmyRefreshSkin.getView();
        int resId = lmyRefreshSkin.getResId();
        String attributeName = lmyRefreshSkin.getAttributeName();
        Resources skinResource = lmyRefreshSkin.getResources();
        if (attributeName.equals("textColor")) {
            TextView textView = (TextView) view;
            textView.setTextColor(skinResource.getColor(resId));
        }
        if (attributeName.equals("background")) {
            try {
                view.setBackgroundColor(skinResource.getColor(resId));
            }catch (Exception e){
                view.setBackground(skinResource.getDrawable(resId));
            }
        }
        if (attributeName.equals("tabIndicatorColor")) {
            //TabLayout下划线颜色
            TabLayout tabLayout = (TabLayout) view;
            tabLayout.setSelectedTabIndicatorColor(skinResource.getColor(resId));
        }
        if (attributeName.equals("tabSelectedTextColor")) {
            //TabLayout选中文本颜色
            TabLayout tabLayout = (TabLayout) view;
            tabLayout.setTabTextColors(Color.BLACK, skinResource.getColor(resId));
        }
    }
}
```

## 4.调用LmySkinManager.getInstance().switchSkin方法进行换肤

&emsp;&emsp;换肤api很好调用，就是调用LmySkinManager.getInstance().switchSkin方法即可，在演示如何调用这个方法之前，我们先来介绍一下LmySkin类:

```
/**
 * 皮肤Bean类
 */
public class LmySkin {
    //皮肤对应的本地路径
    protected String path;
    //皮肤对应的名字
    protected String name;

    public LmySkin(String name, String path) {
        this.path = path;
        this.name = name;
    }

    public LmySkin(String path){
        this.path = path;
        this.name = new File(path).getName();
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

&emsp;&emsp;注意该类可以继承，使用继承后的LmySkin对框架使用毫无影响，api还是照样调用，此类有两个重要属性:

- path:皮肤空壳包对应的本地路径，必须赋值正确且文件存在的路径，否则调用切换皮肤api时，会报异常
- name:皮肤对应的名字，如果调用的LmySkin(String path)构造器，那么皮肤的名字就为文件名，如果你想自定义皮肤名调用LmySkin(String name, String path)构造器即可。

&emsp;&emsp;现在前面几个步骤我们都完成了，我们来演示下如何切换皮肤:

```
File file = new File(this.getCacheDir(), "skin_red.apk");
LmySkin lmySkin = new LmySkin(file.getAbsolutePath());
//切换皮肤
LmySkinManager.getInstance().switchSkin(lmySkin);
```

&emsp;&emsp;以上便讲解完了框架使用步骤，注意在切换皮肤时一定确保本地存在这个皮肤包，不然框架就会报异常，此外还需要文件读写权限哦。另外笔者还提供了一个监听的api：

```
//添加皮肤切换监听
LmySkinManager.getInstance().addLmySkinSwitchListener(lmySkinSwitchListener);

//移除皮肤切换监听
LmySkinManager.getInstance().removeLmySkinSwitchListener(lmySkinSwitchListener);
```

&emsp;&emsp;监听类实现如下：
```
LmySkinSwitchListener lmySkinSwitchListener = new LmySkinSwitchListener() {
    @Override
    public void switchSkin(LmySkin newSkin) {
        //切换的新皮肤为newSkin
    }
};
```
&emsp;&emsp;此外还提供了其它api：

```
//当前的皮肤是否使用的默认皮肤
boolean isDefault = LmySkinManager.getInstance().isDefault();

File file = new File(this.getCacheDir(), "skin_red.apk");
LmySkin lmySkin = new LmySkin(file.getAbsolutePath());
//此皮肤是否为默认皮肤
boolean isDefault = LmySkinManager.getInstance().isDefault(lmySkin);
```