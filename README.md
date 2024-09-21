![poster](common/src/main/resources/assets/yuushya/textures/0extra_building_material/preview/poster/poster_2xv.png)
![pack](common/src/main/resources/pack.png)

# Yuushya Townscape

Yuushya是一个结合Mod与材质的建筑系列。使用Yuushya系列，你可以建造出惟妙惟肖，生动且真实的小镇景观。驻足于街坊市井中，来往于街角，餐馆，奶茶店，书屋之间，你可以在Minecraft领略真实的生活气息。 Yuushya-方块小镇Mod 拥有 800+新增的建材、装饰等；拥有不同的小系统，例如招牌，连接模型，电网，管道网络，不同颜色的抽象文字等。

Yuushya Townscape is a Mod combined with the Yuushya texture pack. With Yuushya, you can build small but lively townscape on your own. By wandering through street and alley, you will find your true within this little Minecraft world.

Yuushya Townscape Mod contains 800+ delicate building materials and decorations with well-designed textures. We also have elaborate but simple features like signs and connected models.


## Website

[Yuushya Minecraft](https://yuushya.com/t/)

## License

代码（Code）：MIT

美术资源（Assets）：CC BY-NC-SA 4.0

[使用协议（Terms）](Terms_yuushya_user.md)

## Code Build

使用IDEA打开项目，运行构建任务。

Using Intellij IDEA to open the project and run the **build** task.

### Generate Assets and Data

运行任务，生成资源包和数据包

**单个版本（Single Version）:**

`gradle :common:gen-1.20.1`

**所有版本（All Version）：**

+ `gradle gen-chain-1` ：1.16.5~1.20.1
+ `gradle gen-chain-2` ：1.20.6
+ `gradle gen-chain-3` ：1.21.0

### Build Jar Package

构建模组的jar包

**单个版本（Single Version）:**

`gradle build-jar-1.20.1-forge`

**所有版本（All Version）：**

+ `gralde build-chain-1`
+ `gradle build-chain-2`

### Run Game in Dev Environment

在开发环境运行游戏，需要使用`:`定位到子项目

In dev environment, you need to use `:` to locate the subprojects.

+ `gradle :1.20.1:forge:runClient`
+ `gradle :1.20.1:forge:runServer`


=======



