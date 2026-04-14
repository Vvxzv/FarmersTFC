# 农夫群峦
## 这是一个让农夫乐事完全兼容群峦的模组
### 模组特性
1. 可以使用群峦里获取的资源制作农夫乐事的物品。
2. 每个农夫乐事的食物都用营养值。（部分食物是动态营养值）
3. 蘑菇可以自然生成在群峦世界里。
4. 煎锅自动适配了食物的加热配方。
5. 将农夫乐事的食物方块修改为带保质期的方块。（可以使用KubeJS添加保质期方块）
6. 肥料使用在沃土上会有额外的肥力加成。
7. 狗粮和马饲料可以用在对应的生物上。
8. 农夫乐事的炉灶是群峦热源。
9. 添加了带保质期的箱装食物方块，手持食物下蹲右键即可放出箱装方块。目前胡萝卜、马铃薯、甜菜、卷心菜、西红柿、洋葱、大米、大米粒有箱装。（可以使用KubeJS添加箱装食物方块）
10. 本模组里的保质期方块都能在地窖中延长保质期。（如果安装了Firmalife）

## 关于KubeJS的兼容
### 注册带腐烂特性的方块
```JavaScript
StartupEvents.registry('block', event => {
    // .setRottenBlock(ResourceLocation id)  如果方块腐烂了就会变成这个方块，否则就是它本身
    
    // .eat(int value) 填入大于1的整数，表达方块状态，方块状态的范围是 0~value
    
    // .facing() 启用方块方向，不写这个方法就是该方块没有方向
    
    event.create('example_decaying_block', 'farmerstfc:decaying_block') 
    .setRottenBlock('minecraft:dirt')
    .eat(7)
    .facing()
})
```

#### 实例：创建一个带腐烂特性的蛋糕
1. 在 ```/kubejs/startup_scripts/reg.js``` 创建方块
```JavaScript
StartupEvents.registry('block', event => {
    event.create('example_decaying_block', 'farmerstfc:decaying_block')
    .eat(7)
    .box(1,0,1,15,8,15)
})
```
2. 在 ```/kubejs/assets/kubejs/blockstates/example_decaying_block.json``` 写不同的方块状态（这里是用原版蛋糕修改的）
```Json
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
    },
    "eat=0": {
      "model": "minecraft:block/cake_slice6"
    }
  }
}
```
3. 在 ```/kubejs/assets/models/item/example_decaying_block.json``` 写物品模型（这里也是直接用的原版蛋糕的模型）
```Json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/cake"
  }
}
```
4. 接下来写方块的功能 TFC食物数据 ```/kubejs/server_scripts/foodItem.js```
```JavaScript
// 你需要安装KubeJS TFC https://www.curseforge.com/minecraft/mc-mods/kubejs-tfc

TFCEvents.data(event => {
    event.food({
        ingredient: {
            item: "kubejs:example_decaying_block"
        },
        food: {
            decay_modifier: 22.0,
            hunger: 6,
            water: 5,
            saturation:2.0,
            intoxication:0,
            nutrients: [1, 1, 1, 1, 1]
        },
        edible: false,
    }, "kubejs:example_decaying_block")
})
```
5. 最后写吃蛋糕 ```/kubejs/server_scripts/eatFoodBlock.js```
```JavaScript
// 在 DecayingBlockJS 中提供了几个静态方法方便使用

// 设置方块方向
// .setFacing(BlockState state, Direction facing): BlockState

// 设置 eat 状态，这里数字要使用字符串 
// .setEat(BlockState state, String number): BlockState

// 设置方块破坏后是否会掉落该方块物品
// .setDrop(Level level, BlockPos pos, boolean drop): void

// 修改方块状态并且是否掉落， 将 eat 属性修改成 eat + addEat
// .consume(Level level, BlockPos pos, String addEat, boolean drop): BlockState

// 修改方块状态，不会掉落该方块， 将 eat 属性修改成 eat + addEat
// .consume(Level level, BlockPos pos, String addEat): BlockState

// 食用蛋糕例子
BlockEvents.rightClicked(event => {
    const { block, player, level, hand } = event
    if(hand.name() != 'MAIN_HAND') return
    if(block.id != 'kubejs:example_decaying_block') return
    if(player.shiftKeyDown) return

    const blockEntity = block.getEntity()

    if (blockEntity instanceof DecayingBlockEntityJS && !level.isClientSide()){
        if(!blockEntity.isRotten() && (player.foodLevel < 20 || player.isCreative())){
            let state = block.blockState
            const curEat = DecayingBlockJS.getEatValue(state)
            if(curEat > 1){
                let newState = DecayingBlockJS.consume(level, block.getPos(), '-1')
                level.setBlockAndUpdate(block.getPos(), newState)
            }
            else block.set('minecraft:air')
            player.eat(level, "tfc:food/red_apple")
            player.swing()
            event.success()
        }
    }
})
```
### 注册箱装食物方块
#### 实例
1. 在 ```/kubejs/startup_scripts/reg.js``` 创建方块
```JavaScript
StartupEvents.registry('block', event => {
    event.create('red_apple_crate', 'farmerstfc:crate')
})
```
2. 添加方块模型（跳过）
3. 在 ```/kubejs/server_scripts/crateData.js```定义物品箱装数据
```JavaScript
FarmersTFCEvent.data(event => {
    event.crate({
        ingredient: {
            item: "tfc:food/red_apple"
        }, 
        block: 'kubejs:red_apple_crate' 
    }, 'kubejs:example')
})
```

---

# Farmers TFC
## A Minecraft mod for Farmer's Delight completely adapts to TFC world.
### Mod Traits
1. Using resources from TFC world for Farmer's Delight recipes.
2. Each Farmer's Delight food has TFC nutrients (some foods have dynamic nutrients).
3. Mushrooms will naturally spawn in the TFC overworld.
4. Skillet automatically adapts to the heating recipe of the food.
5. Modify the food block of Farmer's Delight to a decaying block. (Decaying block can be added using KubeJS)
6. Fertilization on Rich Soil Farmland will result in nutrient addition.
7. Dog Food and Horse Feed can be used on corresponding animal.
8. Farmer's Delight Stove is a tfc heat source.
9. Added decaying food crate block. Shift and right-click to place the crate block when you holding food. Currently, carrots, potatoes, beets, cabbages, tomatoes, onions, and rice have crate. (Decaying food crate block can be added using KubeJS)
10. The decaying block in this mod can extend it food life in the cellar. (If Firmalife is installed)

## About KubeJS compat
### Decaying block registry
```JavaScript
StartupEvents.registry('block', event => {
    //.setRottenBlock(ResourceLocation)  If the block rots, it will become other block, otherwise it is the block itself.

    //.eat(int) 1~15 a blockstate, can be used to create different models.

    // .facing() Enable block direction. If this method is not written, it means the block has no direction

    event.create('example_decaying_block', 'farmerstfc:decaying_block')
        .setRottenBlock('minecraft:dirt')
        .eat(7)
        .facing()
})
```

### Example: Create a decaying cake
1. In ```/kubejs/startup_scripts/reg.js``` to create the block.
```JavaScript
StartupEvents.registry('block', event => {
    event.create('example_decaying_block', 'farmerstfc:decaying_block')
    .eat(7)
    .box(1,0,1,15,8,15)
})
```
2. In ```/kubejs/assets/kubejs/blockstates/example_decaying_block.json``` to write different block states. (This is modified from the vanilla cake)
```Json
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
    },
    "eat=0": {
      "model": "minecraft:block/cake_slice6"
    }
  }
}
```
3. In ```/kubejs/assets/models/item/example_decaying_block.json``` to write item model. (This is the vanilla cake's item model)
```Json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/cake"
  }
}
```
4. TFC data food item.  ```/kubejs/server_scripts/foodItem.js```
```JavaScript
// Need install KubeJS TFC https://www.curseforge.com/minecraft/mc-mods/kubejs-tfc

TFCEvents.data(event => {
    event.food({
        ingredient: {
            item: "kubejs:example_decaying_block"
        },
        food: {
            decay_modifier: 22.0,
            hunger: 6,
            water: 5,
            saturation:2.0,
            intoxication:0,
            nutrients: [1, 1, 1, 1, 1]
        },
        edible: false,
    }, "kubejs:example_decaying_block")
})
```
5. Eat cake ```/kubejs/server_scripts/eatFoodBlock.js```
```JavaScript
// Several static methods are provided in DecayingBlockJS for ease of use

// Set the direction of the square
// .setFacing(BlockState state, Direction facing): BlockState

// Set the "eat" status, where numbers should be converted to strings
// .setEat(BlockState state, String number): BlockState

// Set whether the block item will drop after the block is destroyed
// .setDrop(Level level, BlockPos pos, boolean drop): void

// Modify the block state and determine whether it should drop, changing the 'eat' attribute to 'eat + addEat'
// .consume(Level level, BlockPos pos, String addEat, boolean drop): BlockState

// Modify the block state so that the block won't drop, and change the "eat" attribute to "eat + addEat"
// .consume(Level level, BlockPos pos, String addEat): BlockState

// Example of eating cake
BlockEvents.rightClicked(event => {
    const { block, player, level, hand } = event
    if(hand.name() != 'MAIN_HAND') return
    if(block.id != 'kubejs:example_decaying_block') return
    if(player.shiftKeyDown) return

    const blockEntity = block.getEntity()

    if (blockEntity instanceof DecayingBlockEntityJS && !level.isClientSide()){
        if(!blockEntity.isRotten() && (player.foodLevel < 20 || player.isCreative())){
            let state = block.blockState
            const curEat = DecayingBlockJS.getEatValue(state)
            if(curEat > 1){
                let newState = DecayingBlockJS.consume(level, block.getPos(), '-1')
                level.setBlockAndUpdate(block.getPos(), newState)
            }
            else block.set('minecraft:air')
            player.eat(level, "tfc:food/red_apple")
            player.swing()
            event.success()
        }
    }
})
```
### Decaying crate block registry
#### Example
1. In ```/kubejs/startup_scripts/reg.js``` to registry the block.
```JavaScript
StartupEvents.registry('block', event => {
    event.create('red_apple_crate', 'farmerstfc:crate')
})
```
2. Add block model. (skip)
3. In  ```/kubejs/server_scripts/crateData.js``` define data for crate block
```JavaScript
FarmersTFCEvent.data(event => {
    event.crate({
        ingredient: {
            item: "tfc:food/red_apple"
        }, 
        block: 'kubejs:red_apple_crate' 
    }, 'kubejs:example')
})
```
