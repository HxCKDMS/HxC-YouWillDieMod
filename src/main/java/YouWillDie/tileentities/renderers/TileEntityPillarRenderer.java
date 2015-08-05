package YouWillDie.tileentities.renderers;

import YouWillDie.ModRegistry;
import YouWillDie.models.ModelPillar;
import YouWillDie.tileentities.TileEntityPillar;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;


public class TileEntityPillarRenderer extends TileEntitySpecialRenderer
{
	private ModelPillar modelPillar = new ModelPillar();

	public static ResourceLocation[] textures = {
		new ResourceLocation("youwilldie", "textures/blocks/pillar.png"),
		new ResourceLocation("youwilldie", "textures/blocks/pillarActive.png")
	};

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		TileEntityPillar tileEntityYour = (TileEntityPillar) tileEntity;
		renderBlockYour(tileEntityYour, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, ModRegistry.blockPillar);
		GL11.glPopMatrix();
	}

	public void renderBlockYour(TileEntityPillar tl, World world, int i, int j, int k, Block block)
	{
		Tessellator tessellator = Tessellator.instance;
		float f = block.getMixedBrightnessForBlock(world, i, j, k);
		int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f, f, f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		boolean active = player != null && player.getEntityData().hasKey("Blessing") && player.getEntityData().getString("Blessing").equals(tl.blessing);

		GL11.glPushMatrix();

		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0.5F, -1.5F, -0.5F);

		bindTexture(!active ? textures[0] : textures[1]);
		modelPillar.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}
}