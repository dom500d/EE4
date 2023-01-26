package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Block ALCHEMICAL_CHEST = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f).requiresTool());
	public class AlchemicalChestEntity extends BlockEntity {
		public AlchemicalChestEntity(BlockPos pos, BlockState state) {
			super(ExampleMod.Alchemical_Chest_Entity, pos, state);
		}
	}
	public static final BlockEntityType<AlchemicalChestEntity> Alchemical_Chest_Entity = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			new Identifier("ee4", "alchemical_chest_entity"),
			FabricBlockEntityTypeBuilder.create(AlchemicalChestEntity::new, ALCHEMICAL_CHEST).build()
	);
	public class RegisterItems {
		public static final ArmorMaterial DARK_MATTER_MATERIAL = new CustomArmorMaterial();
//		public static final Item DARK_MATTER = new CustomMaterialItem(new Item.Settings());
		// If you made a new material, this is where you would note it.
		public static final Item DARK_MATTER_HELMET = new ArmorItem(DARK_MATTER_MATERIAL, EquipmentSlot.HEAD, new Item.Settings());
		public static final Item DARK_MATTER_CHESTPLATE = new ArmorItem(DARK_MATTER_MATERIAL, EquipmentSlot.CHEST, new Item.Settings());
		public static final Item DARK_MATTER_LEGGINGS = new ArmorItem(DARK_MATTER_MATERIAL, EquipmentSlot.LEGS, new Item.Settings());
		public static final Item DARK_MATTER_BOOTS = new ArmorItem(DARK_MATTER_MATERIAL, EquipmentSlot.FEET, new Item.Settings());
		public static void register() {
//			Registry.register(Registries.ITEM, new Identifier("ee4", "dark_matter_material"), DARK_MATTER_MATERIAL);
			Registry.register(Registries.ITEM, new Identifier("ee4", "dark_matter_helmet"), DARK_MATTER_HELMET);
			Registry.register(Registries.ITEM, new Identifier("ee4", "dark_matter_chestplate"), DARK_MATTER_CHESTPLATE);
			Registry.register(Registries.ITEM, new Identifier("ee4", "dark_matter_leggings"), DARK_MATTER_LEGGINGS);
			Registry.register(Registries.ITEM, new Identifier("ee4", "dark_matter_boots"), DARK_MATTER_BOOTS);
		}
	}

	public static final Logger LOGGER = LoggerFactory.getLogger("ee4");
	public static class CustomArmorMaterial implements ArmorMaterial {
		private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
		private static final int[] PROTECTION_VALUES = new int[] {20, 25, 27, 20};
		@Override
		public int getDurability(EquipmentSlot slot) {
			return BASE_DURABILITY[slot.getEntitySlotId()] * 100;
		}
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return PROTECTION_VALUES[slot.getEntitySlotId()];
		}
		@Override
		public int getEnchantability() {
			return 10;
		}
		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ITEM_ARMOR_EQUIP_CHAIN;
		}
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(DARK_MATTER);
		}
		@Override
		public String getName() {
			// Must be all lowercase`
			return "dark_matter";
		}
		@Override
		public float getToughness() {
			return 6.0F;
		}
		@Override
		public float getKnockbackResistance() {
			return 0.2F;
		}
	}
	public static final Item DARK_MATTER =
			Registry.register(Registries.ITEM, new Identifier("ee4", "dark_matter"),
					new CustomItem(new FabricItemSettings()));
	public static final Item ALCHEMICAL_COAL =
			Registry.register(Registries.ITEM, new Identifier("ee4", "alchemical_coal"),
					new CustomItem(new FabricItemSettings()));
	public static final Item MOBIUS_FUEL =
			Registry.register(Registries.ITEM, new Identifier("ee4", "mobius_fuel"),
					new CustomItem(new FabricItemSettings()));
	public static final Item AETERNALIS_FUEL =
			Registry.register(Registries.ITEM, new Identifier("ee4", "aeternalis_fuel"),
					new CustomItem(new FabricItemSettings()));
	public static final Item PHILOSOPHERS_STONE =
			Registry.register(Registries.ITEM, new Identifier("ee4", "philosophers_stone"),
					new CustomItem(new FabricItemSettings()));
	/*public static final Item ALCHEMICAL_CHEST =
			Registry.register(Registries.ITEM, new Identifier("ee4", "alchemical_chest"),
					new CustomItem(new FabricItemSettings()));*/

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("ee4", "test_group"))
			.icon(() -> new ItemStack(ALCHEMICAL_CHEST))
			.build();


	@Override
	public void onInitialize() {
		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
			content.add(ALCHEMICAL_CHEST);
			content.addAfter(ALCHEMICAL_CHEST, ALCHEMICAL_COAL);
			content.addAfter(ALCHEMICAL_COAL, MOBIUS_FUEL);
			content.addAfter(MOBIUS_FUEL, AETERNALIS_FUEL);
			content.addAfter(AETERNALIS_FUEL, PHILOSOPHERS_STONE);
			content.addAfter(PHILOSOPHERS_STONE, DARK_MATTER);
			content.addAfter(DARK_MATTER, RegisterItems.DARK_MATTER_HELMET);
			content.addAfter(RegisterItems.DARK_MATTER_HELMET, RegisterItems.DARK_MATTER_CHESTPLATE);
			content.addAfter(RegisterItems.DARK_MATTER_CHESTPLATE, RegisterItems.DARK_MATTER_LEGGINGS);
			content.addAfter(RegisterItems.DARK_MATTER_LEGGINGS, RegisterItems.DARK_MATTER_BOOTS);
		});
		RegisterItems.register();
		Registry.register(Registries.BLOCK, new Identifier("ee4", "alchemical_chest"), ALCHEMICAL_CHEST);
		Registry.register(Registries.ITEM, new Identifier("ee4", "alchemical_chest"), new BlockItem(ALCHEMICAL_CHEST, new FabricItemSettings()));
	}
	/*@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

		// default white text
		tooltip.add(Text.translatable("item.ee4.custom_item.tooltip"));

		// formatted red text
		tooltip.add(Text.translatable("item.ee4.custom_item.tooltip").formatted(Formatting.RED));
	}*/
}

