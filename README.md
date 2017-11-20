**手机蓝牙连接**

~~~
如果有问题请联系我：guochenhome@163.com

也可以关注微信公众号：指动天下  私信我
~~~

**可能出现的错误**

~~~
Error:Execution failed for task ':app:processXXXDebugManifest'. >
Manifest merger failed with multiple errors, see logs

/**
这说明在合并所有的Manfest文件时冲突了，几率最大的两个原因是

1.build.gradle中设置的compileSdkVersion buildToolsVersion minSdkVersion targetSdkVersion不统一，需要按宿主项目的配置进行统一。
2.几个项目的AndroidManifest文件中设置了多个android:allowBackup    android:icon   android:label  android:theme 属性，这里需要在宿主项目的Manfest文件中添加两句话
manifest 节点下加入
xmlns:tools="http://schemas.android.com/tools"
application节点下加入
tools:replace="android:allowBackup,icon,theme,label"
不能写成tools:replace="android:allowBackup，android:icon，android:theme"   虽然不报错，但是不起作用。

*/
~~~
**解决办法**
~~~
你可以直接下载   BluetoothPrint/printet/  这个文件  直接当成类库使用

~~~



**依赖包 1.1.1**
~~~
		allprojects {
    		repositories {
    			...
    			maven { url 'https://jitpack.io' }
    		}
    	}
    	
    		dependencies {
        	        compile 'com.github.guochenhome:BluetoothPrint:1.1.1'
        	}


~~~
**依赖包1.1.2**
~~~
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
		dependencies {
    	        compile 'com.github.guochenhome:BluetoothPrint:1.1.2'
    	}

~~~