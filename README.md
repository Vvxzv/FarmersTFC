# 农夫群峦 Farmers TFC
## 这是一个让农夫乐事完全兼容群峦的模组
### 模组特性
1. 可以使用群峦里获取的资源制作农夫乐事的物品；
2. 每个农夫乐事的食物都用营养值；（部分食物是动态营养值）
3. 蘑菇可以自然生成在群峦世界里；
4. 煎锅自动适配了低温的加热配方；（温度小于201；属于这个温度范围但不在配方里是因为配方的输入物品是#farmerstfc:cant_cook）
5. 添加了带保质期的食物方块代替农夫乐事的食物方块；
6. 肥料使用在沃土上会有额外的肥力加成；
7. 狗粮和马饲料可以用在对应的生物上

### 添加了kubejs的兼容（注册带腐烂特性的方块，未完全完成的功能）
```JavaScript
StartupEvents.registry('block', event => {
    //.setRottenBlock(ResourceLocation)  如果方块腐烂了就会变成这个方块，否则就是它本身
    //.noDrops() 移除KJS自带的掉落物，否则会掉落两份掉落物
    
    event.create('example_decaying_block', 'farmerstfc:decaying_block') 
    .setRottenBlock('minecraft:dirt')   
    .noDrops()
})
```

## A Minecraft mod for Farmer's Delight completely adapts to TFC world.

Mod Traits
1. Using resources from TFC world for Farmer's Delight  recipes.
2. Each Farmer's Delight food has TFC nutrients (some foods have dynamic nutrients).
3. Mushrooms will naturally spawn in the TFC overworld.
4. Skillet can cook item which has heating recipe and the recipe's temperature lower than 201.  (In this temperature range but not in the recipe, it is because the input item is #farmerstfc:cant_cook)
5. Added decaying food block replace food block of Farmer's Delight.
6. Fertilization on Rich Soil Farmland will result in nutrient addition.
7. Dog Food and Horse Feed can be used on corresponding animal.

### Added KubeJS compat (Decaying block registry)
```JavaScript
StartupEvents.registry('block', event => {
    //.setRottenBlock(ResourceLocation)  If the block rots, it will become other block, otherwise it is the block itself
    //.noDrops() Remove the KJS drops, otherwise two items will be dropped.
    
    event.create('example_decaying_block', 'farmerstfc:decaying_block') 
    .setRottenBlock('minecraft:dirt')   
    .noDrops()
})
```
