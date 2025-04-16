$blocks = @(
    "dried_reed_thatch",
    "riverbed_clay",
    "obelisk_stone",
    "mud_brick",
    "sunbaked_clay"
)

foreach ($block in $blocks) {
    # Create model file
    $modelContent = @"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "ancientcurse:block/$block"
  }
}
"@
    $modelPath = "src/main/resources/assets/ancientcurse/models/block/$block.json"
    Set-Content -Path $modelPath -Value $modelContent
    Write-Host "Created model: $modelPath"

    # Create blockstate file
    $blockstateContent = @"
{
  "variants": {
    "": [
      {
        "model": "ancientcurse:block/$block"
      },
      {
        "model": "ancientcurse:block/$block",
        "y": 90
      },
      {
        "model": "ancientcurse:block/$block",
        "y": 180
      },
      {
        "model": "ancientcurse:block/$block",
        "y": 270
      }
    ]
  }
}
"@
    $blockstatePath = "src/main/resources/assets/ancientcurse/blockstates/$block.json"
    Set-Content -Path $blockstatePath -Value $blockstateContent
    Write-Host "Created blockstate: $blockstatePath"

    # Create item model file
    $itemModelContent = @"
{
  "parent": "ancientcurse:block/$block"
}
"@
    $itemModelPath = "src/main/resources/assets/ancientcurse/models/item/$block.json"
    Set-Content -Path $itemModelPath -Value $itemModelContent
    Write-Host "Created item model: $itemModelPath"

    # Create loot table file
    $lootTableContent = @"
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "ancientcurse:$block"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
        }
      ]
    }
  ]
}
"@
    $lootTablePath = "src/main/resources/data/ancientcurse/loot_tables/blocks/$block.json"
    Set-Content -Path $lootTablePath -Value $lootTableContent
    Write-Host "Created loot table: $lootTablePath"
}

Write-Host "All files created successfully!" 