自定义车牌输入，可以输入七位和八位的车牌号码，只适用于车牌输入的项目，所以包含自定义省份键盘和数字字母键盘。

效果如下：

![](http://ww1.sinaimg.cn/large/78e0e0dfgy1fo8wb1v6qbg20go0tnkbn.gif)

### 功能说明

 * 支持7位车牌和8位车牌的输入
 * 点击任意位置，进行编辑，删除当前选中位置内容
 * 自动切换键盘布局
 * 输入监听包含输入完成、删除内容
 * 获取输入的内容
 * 清空输入内容
 * 设置输入框字体颜色
 
### 使用说明
 
1、在工程的 build.gradle 中添加：

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
2、在应用的 build.gradle 中添加：

	dependencies {
	        compile 'com.github.winterPei:LicensePlateUtil:v1.0'
	}
  
3、XML布局文件中添加 
```Java
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main_rl_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    
    <com.winterpei.LicensePlateView
        android:id="@+id/activity_lpv"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:layout_gravity="center_horizontal"
        android:layout_margin="39dp"/>
        
</RelativeLayout>
```
#### 很重要！！！说明：因为需要自定义的键盘，所以在代码中需要动态添加布局，LicensePlateView 需要一个 RelativeLayout 作为根布局作为父容器。

4、在 Activity 或 Fragment 中添加代码

```Java
  LicensePlateView mPlateView;
  RelativeLayout mContainer;
  
  mPlateView = findViewById(R.id.activity_lpv);
  mContainer = findViewById(R.id.main_rl_container);
  
  //设置监听，实现对应的方法，方便在当前页面实现自己的逻辑
  mPlateView.setInputListener(this);
  
  //设置父布局作为自定义键盘的容器
  mPlateView.setKeyboardContainerLayout(mContainer);
  
  //输入七位和八位车牌号码的方法
  mPlateView.showLastView();
  mPlateView.hideLastView();
  
  //设置输入框字体颜色
  mPlateView.onSetTextColor(R.color.colorAccent);
  
  //两个需要实现的方法
  @Override
  public void inputComplete(String content) {}

  @Override
  public void deleteContent() {}
  
```
