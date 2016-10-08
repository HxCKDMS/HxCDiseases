package HxCKDMS.HxCDiseases.items;

import HxCKDMS.HxCDiseases.HxCDiseases;
import HxCKDMS.HxCDiseases.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ItemVial extends ItemFood{
	@SideOnly(Side.CLIENT)
	public HashMap<String,IIcon> icons = new HashMap<>();

	public ItemVial(){
		super(0,false);
		this.setCreativeTab(HxCDiseases.tabDiseases);
		this.setUnlocalizedName("vial");
		//this.setTextureName(HxCDiseases.MODID+":"+"vial_"+diseasename.replace(" ", "").toLowerCase());
		this.setHasSubtypes(true);
		this.setAlwaysEdible();
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean someBool) {
		if(itemStack.getTagCompound().getString("disease").equals("Full Syringe")) {
			if (itemStack.getTagCompound().hasKey("mob")) {
				list.add("Contains: "+itemStack.getTagCompound().getString("mob"));
			}
		}
		super.addInformation(itemStack,player,list,someBool);
	}

	/*@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		if(itemStack.getTagCompound().getString("disease")=="Full Syringe") {
			if(itemStack.getTagCompound().hasKey("mob")){
				return "Syringe: " + itemStack.getTagCompound().getString("mob");
			}
			return "Syringe: ???";
		}else if(itemStack.getTagCompound().getString("disease")=="Syringe"){
			return "Empty Syringe";
		}else{
			return super.getItemStackDisplayName(itemStack);
		}
		return "Error!";
	}
*/
	@Override
	public EnumAction getItemUseAction(ItemStack item){return EnumAction.drink;}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {return 32;}

	@Override
	@SuppressWarnings("unchecked")
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		List<ItemStack> dgs = new ArrayList<>();
		HxCDiseases.diseases.keySet().forEach(disease -> {
			ItemStack stack = new ItemStack(item, 1, 0);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("disease", disease);
			stack.setTagCompound(tag);
			dgs.add(stack);
		});
		HxCDiseases.mobs.values().forEach((mob) -> {
			ItemStack stack = new ItemStack(item, 1, 0);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("disease", "Full Syringe");
			tag.setString("mob", mob);
			stack.setTagCompound(tag);
			dgs.add(stack);
		});
		list.addAll(dgs);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass) {
		return icons.get(stack.getTagCompound().getString("disease"));
	}

	//icons.get(stack.getTagCompound().getString("disease"));

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconReg) {
		HxCDiseases.diseases.keySet().forEach(diseasename-> {
			icons.put(diseasename, iconReg.registerIcon(HxCDiseases.MODID + ":" + "disease_" +diseasename.replace(" ", "").toLowerCase()));
		});
		icons.put("Full Syringe", iconReg.registerIcon(HxCDiseases.MODID + ":" + "syringe_blood"));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		String disease = stack.getTagCompound().getString("disease");
		if(HxCDiseases.diseases.get(disease) != null) {
			if (Objects.equals(disease, "Syringe")) {
				String mob = stack.getTagCompound().getString("mob");

			} else if (Objects.equals(disease, "Full Syringe")) {
				String mob = stack.getTagCompound().getString("mob");

			} else if (!disease.equals("Vial") && !disease.equals("EyeDropper")) {
				if (player.capabilities.isCreativeMode) {
					this.onEaten(stack, world, player);
				} else {
					player.setItemInUse(stack, getMaxItemUseDuration(stack));
				}
			}
		}else if(disease.equals("Grand Panacea")){
			if (player.capabilities.isCreativeMode) {
				this.onEaten(stack, world, player);
			} else {
				player.setItemInUse(stack, getMaxItemUseDuration(stack));
			}
		}
		return stack;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return  "item.vial_" +itemStack.getTagCompound().getString("disease").replace(" ", "").toLowerCase();
	}

	@Override
	public boolean hasEffect(ItemStack itemStack, int pass) {
		String disease = itemStack.getTagCompound().getString("disease");
		if(disease.equals("Grand Panacea")){
			return true;
		}
		return false;
	}

	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
		String disease = itemStack.getTagCompound().getString("disease");
		if(disease.equals("Grand Panacea")){
			HxCDiseases.diseases.keySet().forEach((name)->{
				Utilities.removeDisease(player,name);
			});
			return player.inventory.decrStackSize(player.inventory.currentItem, itemStack.stackSize - 1);
		}else if(HxCDiseases.diseases.get(disease)!=null) {
			if (applyDisease(player, disease)) {
				return player.inventory.decrStackSize(player.inventory.currentItem, itemStack.stackSize - 1);
			}
		}
		return itemStack;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer myPlayer, Entity other){
		if(!myPlayer.worldObj.isRemote) {
			String disease = itemStack.getTagCompound().getString("disease");
			if (disease.equals("Syringe")) {
				if (!itemStack.getTagCompound().hasKey("mob") || itemStack.getTagCompound().getString("mob").isEmpty()) {
					if(HxCDiseases.mobs.containsKey(other.getClass())) {
						ItemStack newStack = new ItemStack(HxCDiseases.vial, 1);
						NBTTagCompound tag = new NBTTagCompound();
						tag.setString("disease", "Full Syringe");
						tag.setString("mob", HxCDiseases.mobs.get(other.getClass()));
						newStack.setTagCompound(tag);
						if(myPlayer.inventory.addItemStackToInventory(newStack)){
							myPlayer.inventory.decrStackSize(myPlayer.inventory.currentItem, 1);
							myPlayer.addChatMessage(new ChatComponentText("Loaded with: " + HxCDiseases.mobs.get(other.getClass())));
						}
					}

				}
			} else {
				if (applyDisease(other, disease)) {
					if (!myPlayer.capabilities.isCreativeMode) {
						myPlayer.inventory.decrStackSize(myPlayer.inventory.currentItem, 1);
					}
					Utilities.playSoundAtPlayer(myPlayer, "hxcdiseases:notify", 3, 1 + ((itemRand.nextFloat() - 0.5f) / 5));
				}
			}
		}
		return true;
	}

	public boolean applyDisease(Entity player, String disease){
		return disease != null && Utilities.applyDisease(player, disease);
	}
}
