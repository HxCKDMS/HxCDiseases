package HxCKDMS.HxCDiseases.Symptoms;


import net.minecraft.entity.player.EntityPlayer;

public class Coughing implements Symptom{
    @Override
    public void tick(EntityPlayer player){
        if(player.worldObj.rand.nextInt(900)==1) {
            player.playSound("hxcdiseases:cough", 1, 1+((player.worldObj.rand.nextFloat()-0.5f)/5));
        }
    }

    @Override
    public void remove(EntityPlayer player) {

    }
    @Override
    public void apply(EntityPlayer player) {

    }

}