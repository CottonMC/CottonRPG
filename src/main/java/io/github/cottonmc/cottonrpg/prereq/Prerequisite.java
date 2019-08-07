package io.github.cottonmc.cottonrpg.prereq;

import java.util.ArrayList;
import java.util.List;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.CharacterResource;
import io.github.cottonmc.cottonrpg.data.CharacterResources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

/**
 * A simple interface which allows checking various player-related stuff.
 * Should be adapted to work with items later.
 */
public interface Prerequisite {
  boolean isOkay(PlayerEntity player);
  
  String getDescription();
  Prerequisite[] getChildren();
  
  public default List<String> describe() {
    List<String> text = new ArrayList<>();
    text.add(getDescription());
    Prerequisite[] children = getChildren();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        List<String> lines = children[i].describe();
        for (String line : lines) {
          text.add("  " + line);
        }
      }
    }
    return text;
  }
  
  public static class All implements Prerequisite {
    private Prerequisite[] prereqs;
    
    public All(Prerequisite... ps) {
      prereqs = ps;
    }
    
    @Override
    public boolean isOkay(PlayerEntity player) {
      for (int i = 0; i < prereqs.length; i++) {
        if (!prereqs[i].isOkay(player)) {
          return false;
        }
      }
      return true;
    }
    
    @Override
    public Prerequisite[] getChildren() {
      return prereqs;
    }
    
    @Override
    public String getDescription() {
      return new TranslatableText("prereq.cottonrpg.all").asString();
    }
  }
  
  public static class Any implements Prerequisite {
    private Prerequisite[] prereqs;
    
    public Any(Prerequisite... prereqs) {
      this.prereqs = prereqs;
    }
    
    @Override
    public boolean isOkay(PlayerEntity player) {
      for (int i = 0; i < prereqs.length; i++) {
        if (prereqs[i].isOkay(player)) {
          return true;
        }
      }
      return false;
    }
    
    @Override
    public Prerequisite[] getChildren() {
      return prereqs;
    }
    
    @Override
    public String getDescription() {
      return new TranslatableText("prereq.cottonrpg.any").asString();
    }
  }
  
  public static class Not implements Prerequisite {
    private Prerequisite prereq;
    
    public Not(Prerequisite prereq) {
      this.prereq = prereq;
    }
    
    @Override
    public boolean isOkay(PlayerEntity player) {
      return !prereq.isOkay(player);
    }
    
    @Override
    public Prerequisite[] getChildren() {
      return new Prerequisite[] { prereq };
    }
    
    @Override
    public String getDescription() {
      return new TranslatableText("prereq.cottonrpg.not").asString();
    }
  }
  
  public static class WantsResource implements Prerequisite {
    private Identifier resourceId;
    private long amount;
    
    public WantsResource(Identifier resid, long amt) {
      resourceId = resid;
      amount = amt;
    }
    
    @Override
    public boolean isOkay(PlayerEntity player) {
      return CharacterData.get(player).getResources().get(resourceId).getCurrent() >= amount;
    }
    
    @Override
    public Prerequisite[] getChildren() {
      return null;
    }
    
    @Override
    public String getDescription() {
      return new TranslatableText(
        "prereq.cottonrpg.wants_resource",
        CottonRPG.RESOURCES.get(resourceId).getName().asString(),
        amount
      ).asString();
    }
  }
}
