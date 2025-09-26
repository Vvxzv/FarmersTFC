# 农夫群峦
## 这是一个让农夫乐事完全兼容群峦的模组
### 模组特性
1. 可以使用群峦里获取的资源制作农夫乐事的物品；
2. 每个农夫乐事的食物都用营养值；（部分食物是动态营养值）
3. 蘑菇可以自然生成在群峦世界里；
4. 煎锅自动适配了低温的加热配方；（温度小于201；属于这个温度范围但不在配方里是因为配方的输入物品是#farmerstfc:cant_cook）
5. 添加了带保质期的食物方块代替农夫乐事的食物方块；
6. 肥料使用在沃土上会有额外的肥力加成；
7. 狗粮和马饲料可以用在对应的生物上。
8. 农夫乐事的炉灶是热源。

### 添加了kubejs的兼容（注册带腐烂特性的方块）
``` JavaScript
StartupEvents.registry('block', event => {
    //.setRottenBlock(ResourceLocation)  如果方块腐烂了就会变成这个方块，否则就是它本身
    //.eat(int) 1~15 用于表示一个状态，可以利用用这个状态做出不同的模型
    
    event.create('example_decaying_block', 'farmerstfc:decaying_block') 
    .setRottenBlock('minecraft:dirt')
    .eat(7)
})
```

### 实例：创建一个带腐烂特性的蛋糕
1. 在 ```/kubejs/startup_scripts/reg.js``` 创建方块
``` JavaScript
StartupEvents.registry('block', event => {
    event.create('example_decaying_block', 'farmerstfc:decaying_block')
    .eat(7)
    .box(1,0,1,15,8,15)
})
```
2. 在 ```/kubejs/assets/kubejs/blockstates/example_decaying_block.json``` 写不同的方块状态（这里是用原版蛋糕修改的）
``` Json
{
  "variants": {
    "eat=7": {
      "model": "minecraft:block/cake"
    },
    "eat=6": {
      "model": "minecraft:block/cake_slice1"
    },
    "eat=5": {
      "model": "minecraft:block/cake_slice2"
    },
    "eat=4": {
      "model": "minecraft:block/cake_slice3"
    },
    "eat=3": {
      "model": "minecraft:block/cake_slice4"
    },
    "eat=2": {
      "model": "minecraft:block/cake_slice5"
    },
    "eat=1": {
      "model": "minecraft:block/cake_slice6"
    }
  }
}
```
3. 在 ```/kubejs/assets/models/item/example_decaying_block.json``` 写物品模型（这里也是直接用的原版蛋糕的模型）
``` Json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/cake"
  }
}
```
4. 接下来写方块的功能 TFC食物数据 ```/kubejs/server_scripts/foodItem.js```
``` JavaScript
// 你需要安装KubeJS TFC https://www.curseforge.com/minecraft/mc-mods/kubejs-tfc

TFCEvents.data(event => {
    event.foodItem('kubejs:example_decaying_block', food => {
        food.hunger(6)
        food.saturation(2)
        food.decayModifier(22)
        food.water(5)
        food.grain(1)
        food.fruit(1)
        food.vegetables(1)
        food.protein(1)
        food.dairy(1)
    })
})
```
5. 最后写吃蛋糕 ```/kubejs/server_scripts/eatFoodBlock.js```
``` JavaScript
BlockEvents.rightClicked(event => {
    const { block, player, level, hand, server } = event
    if(hand.name() != 'MAIN_HAND') return
    if(block.id != 'kubejs:example_decaying_block') return
    if(player.shiftKeyDown) return

    const blockEntity = block.getEntity()

    if (blockEntity instanceof DecayingBlockEntity && !level.isClientSide()){
        if(!blockEntity.isRotten() && (player.foodLevel < 20 || player.isCreative())){
            const curEat = block.blockState.getValue(DecayingBlockJS.EAT)

            const newEat = `${curEat - 1}`.split('.')[0]
            
            server.runCommandSilent(`execute in ${block.dimension} run setblock ${block.pos.x} ${block.pos.y} ${block.pos.z} kubejs:example_decaying_block[eat=${newEat}, dropself=false]`)

            if(curEat <= 1){
                block.set('minecraft:air')
            }
            
            player.eat(level, 'minecraft:apple')
            event.cancel()
        }
    }
})
```

# Farmers TFC

## A Minecraft mod for Farmer's Delight completely adapts to TFC world.

### Mod Traits
1. Using resources from TFC world for Farmer's Delight recipes.
2. Each Farmer's Delight food has TFC nutrients (some foods have dynamic nutrients).
3. Mushrooms will naturally spawn in the TFC overworld.
4. Skillet can cook item which has heating recipe and the recipe's temperature lower than 201. (In this temperature range but not in the recipe, it is because the input item is #farmerstfc:cant_cook)
5. Added decaying food block replace food block of Farmer's Delight.
6. Fertilization on Rich Soil Farmland will result in nutrient addition.
7. Dog Food and Horse Feed can be used on corresponding animal.
8. Farmer's Delight Stove is a heat source.

### Added KubeJS compat (Decaying block registry)
```JavaScript
StartupEvents.registry('block', event => {
    //.setRottenBlock(ResourceLocation)  If the block rots, it will become other block, otherwise it is the block itself.
    //.eat(int) 1~15 a blockstate, can be used to create different models.
    
    event.create('example_decaying_block', 'farmerstfc:decaying_block') 
    .setRottenBlock('minecraft:dirt')
    .eat(7)
})
```

### Example: Create a decaying cake
1. In ```/kubejs/startup_scripts/reg.js``` to create the block.
``` JavaScript
StartupEvents.registry('block', event => {
    event.create('example_decaying_block', 'farmerstfc:decaying_block')
    .eat(7)
    .box(1,0,1,15,8,15)
})
```
2. In ```/kubejs/assets/kubejs/blockstates/example_decaying_block.json``` to write different block states. (This is modified from the vanilla cake)
``` Json
{
  "variants": {
    "eat=7": {
      "model": "minecraft:block/cake"
    },
    "eat=6": {
      "model": "minecraft:block/cake_slice1"
    },
    "eat=5": {
      "model": "minecraft:block/cake_slice2"
    },
    "eat=4": {
      "model": "minecraft:block/cake_slice3"
    },
    "eat=3": {
      "model": "minecraft:block/cake_slice4"
    },
    "eat=2": {
      "model": "minecraft:block/cake_slice5"
    },
    "eat=1": {
      "model": "minecraft:block/cake_slice6"
    }
  }
}
```
3. In ```/kubejs/assets/models/item/example_decaying_block.json``` to write item model. (This is the vanilla cake's item model)
``` Json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/cake"
  }
}
```
4. TFC data food item.  ```/kubejs/server_scripts/foodItem.js```
``` JavaScript
// Need install KubeJS TFC https://www.curseforge.com/minecraft/mc-mods/kubejs-tfc

TFCEvents.data(event => {
    event.foodItem('kubejs:example_decaying_block', food => {
        food.hunger(6)
        food.saturation(2)
        food.decayModifier(22)
        food.water(5)
        food.grain(1)
        food.fruit(1)
        food.vegetables(1)
        food.protein(1)
        food.dairy(1)
    })
})
```
5. Eat cake ```/kubejs/server_scripts/eatFoodBlock.js```
``` JavaScript
BlockEvents.rightClicked(event => {
    const { block, player, level, hand, server } = event
    if(hand.name() != 'MAIN_HAND') return
    if(block.id != 'kubejs:example_decaying_block') return
    if(player.shiftKeyDown) return

    const blockEntity = block.getEntity()

    if (blockEntity instanceof DecayingBlockEntity && !level.isClientSide()){
        if(!blockEntity.isRotten() && (player.foodLevel < 20 || player.isCreative())){
            const curEat = block.blockState.getValue(DecayingBlockJS.EAT)

            const newEat = `${curEat - 1}`.split('.')[0]
            
            server.runCommandSilent(`execute in ${block.dimension} run setblock ${block.pos.x} ${block.pos.y} ${block.pos.z} kubejs:example_decaying_block[eat=${newEat}, dropself=false]`)

            if(curEat <= 1){
                block.set('minecraft:air')
            }
            
            player.eat(level, 'minecraft:apple')
            event.cancel()
        }
    }
})
```
