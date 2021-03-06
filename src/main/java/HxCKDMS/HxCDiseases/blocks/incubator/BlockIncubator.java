package HxCKDMS.HxCDiseases.blocks.incubator;


import HxCKDMS.HxCDiseases.HxCDiseases;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockIncubator extends Block {

    public IIcon[] blockIcons = new IIcon[3];

    public BlockIncubator() {
        super(Material.iron);
        this.setCreativeTab(HxCDiseases.tabDiseases);
        this.textureName = HxCDiseases.MODID+":incubator";
        this.setLightLevel(1);
    }

    @Override
    public String getUnlocalizedName() {
        return HxCDiseases.MODID+".blockincubator";
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        if(side == ForgeDirection.UP){
            return false;
        }
        return true;
    }

    @Override
    public IIcon getIcon(int side, int metadata){
        switch(side){
            case 1://top
                return blockIcons[0];
            case 0://bottom
                return blockIcons[2];
        }
        return blockIcons[1];
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcons[0] = reg.registerIcon(this.textureName + "_top");
        this.blockIcons[1] = reg.registerIcon(this.textureName + "_sides");
        this.blockIcons[2] = reg.registerIcon(this.textureName + "_bottom");
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityIncubator();
    }


}
