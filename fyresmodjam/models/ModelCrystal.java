// Date: 12/13/2013 12:25:37 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package fyresmodjam.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrystal extends ModelBase
{
  //fields
    ModelRenderer CrystalMain;
    ModelRenderer CrystalEdge1;
    ModelRenderer CrystalEdge2;
    ModelRenderer CrystalTop1;
    ModelRenderer CrystalTop2;
  
  public ModelCrystal() {
    textureWidth = 64;
    textureHeight = 32;
    
      CrystalMain = new ModelRenderer(this, 0, 0);
      CrystalMain.addBox(0F, 0F, 0F, 5, 7, 5);
      CrystalMain.setRotationPoint(-3F, 10F, -3F);
      CrystalMain.setTextureSize(64, 32);
      CrystalMain.mirror = true;
      setRotation(CrystalMain, 0F, 0F, 0F);
      CrystalEdge1 = new ModelRenderer(this, 0, 14);
      CrystalEdge1.addBox(0F, 0F, 0F, 3, 1, 3);
      CrystalEdge1.setRotationPoint(-2F, 9F, -2F);
      CrystalEdge1.setTextureSize(64, 32);
      CrystalEdge1.mirror = true;
      setRotation(CrystalEdge1, 0F, 0F, 0F);
      CrystalEdge2 = new ModelRenderer(this, 0, 14);
      CrystalEdge2.addBox(0F, 0F, 0F, 3, 1, 3);
      CrystalEdge2.setRotationPoint(-2F, 17F, -2F);
      CrystalEdge2.setTextureSize(64, 32);
      CrystalEdge2.mirror = true;
      setRotation(CrystalEdge2, 0F, 0F, 0F);
      CrystalTop1 = new ModelRenderer(this, 0, 12);
      CrystalTop1.addBox(0F, 8F, 0F, 1, 1, 1);
      CrystalTop1.setRotationPoint(-1F, 0F, -1F);
      CrystalTop1.setTextureSize(64, 32);
      CrystalTop1.mirror = true;
      setRotation(CrystalTop1, 0F, 0F, 0F);
      CrystalTop2 = new ModelRenderer(this, 0, 12);
      CrystalTop2.addBox(-1F, 18F, -1F, 1, 1, 1);
      CrystalTop2.setRotationPoint(0F, 0F, 0F);
      CrystalTop2.setTextureSize(64, 32);
      CrystalTop2.mirror = true;
      setRotation(CrystalTop2, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    CrystalMain.render(f5);
    CrystalEdge1.render(f5);
    CrystalEdge2.render(f5);
    CrystalTop1.render(f5);
    CrystalTop2.render(f5);
    
    // TODO floating animation thing
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
