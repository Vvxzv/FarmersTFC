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
