# 遗传算法和神经网络实现小车的局部路径规划

使用遗传算法和神经网络，非监督的训练小车，让它自己学会躲避障碍物，走出迷宫！还可以自己绘制地图，设定小车的数量。

![image-20200427005548039](https://github.com/a82647041/ImgLib/blob/master/image-20200427005548039.png)



## 关于项目

+ 构建项目工具：Gradle
+ IDE：Android Studio
+ LibGDX游戏引擎（不了解的戳这个https://libgdx.badlogicgames.com/documentation/）
+ BOX2D物理引擎（不了解的戳这个https://box2d.org/documentation/）
+ Lua脚本语言（不了解的戳这个https://www.runoob.com/lua/lua-tutorial.html）
+ 遗传算法的基本知识（网上很多）
+ 神经网络的基本知识（网上很多）

## 项目要点

### 目录结构

+ desktop：项目的启动模块
+ core：项目核心模块
  + assets：资源文件夹

### 入口点

在`desktop`模块下面的`DesktopLauncher.java`中

### 渲染逻辑

在core模块下面的`MyGdxGame`中

### 神经网络

神经网络计算在`NN.java`中，以下是用来计算神经网络权重和偏移的核心代码

```java
public void updateWeight(double[] tar){
        int l=layer.length-1;
        for(int j=0;j<layerErr[l].length;j++)
            layerErr[l][j]=layer[l][j]*(1-layer[l][j])*(tar[j]-layer[l][j]);

        while(l-->0){
            for(int j=0;j<layerErr[l].length;j++){
                double z = 0.0;
                for(int i=0;i<layerErr[l+1].length;i++){
                    z=z+l>0?layerErr[l+1][i]*layer_weight[l][j][i]:0;
                    layer_weight_delta[l][j][i]= mobp*layer_weight_delta[l][j]	[i]+rate*layerErr[l+1][i]*layer[l][j];//隐含层动量调整
                    layer_weight[l][j][i]+=layer_weight_delta[l][j][i];//隐含层权重调整
                    if(j==layerErr[l].length-1){
                        layer_weight_delta[l][j+1][i]= mobp*layer_weight_delta[l][j+1][i]+rate*layerErr[l+1][i];//截距动量调整
                        layer_weight[l][j+1][i]+=layer_weight_delta[l][j+1][i];//截距权重调整
                    }
                }
                layerErr[l][j]=z*layer[l][j]*(1-layer[l][j]);//记录误差
            }
        }
    }
```



### 配置文件

配置文件为`Config.java`

```java
static public boolean isMove = true;
	//小车是否可以移动
    static public boolean initMap = true;
	//小车初始化数量（种群数量）
    public static int popSize = 120;
	//是否显示小车的视野
    public static boolean sight = true;
	//用何种碰撞模式
    public static int hitMode = 1;
	//地图名字
    public static String mapName = "map-hard";
	//·视线·数量
    public static int versionNum = 5;
	//小车移动速度
    public static float speed = 10;
	//小车角速度
    public static float angle_speed = 4;
	//视线长度
    public static float sight_length = 300;
	//神经网络参数文件
    public static String branFile = "\\gene\\self1";
	//是否使用神经网络（不用的话，就是从头开始训练）
    public static boolean useGeneFile = true;
```

### Lua相关

lua对应Java中的静态类

```lua
config = luajava.newInstance("com.mygdx.game.Entity.Config");
--main = luajava.newInstance("com.mygdx.game.MyGdxGame");
main = luajava.bindClass("com.mygdx.game.MyGdxGame")
nn = luajava.bindClass("com.mygdx.game.Entity.NN")
util = luajava.bindClass("com.mygdx.game.Util.MyUtil")
pop = luajava.bindClass("com.mygdx.game.Entity.Population")

--print 'helloworld';

```

## 最后

移动小车的项目到这里就告了一段落，接下来要做的是用遗传算法+神经网络去让AI自己玩马里奥！！

