package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * A type of resource, like mana, rage, blood, etc.
 */
public interface CharacterResource {

  /**
   * How the resource should be displayed/synced to the user
   * INVISIBLE: Not synced to client.
   * HIDDEN: Synced to client, but not displayed anywhere by Cotton RPG itself.
   * HUD: Synced to client, and displayed in the HUD.
   */
  public enum ResourceVisibility {
    INVISIBLE,
    HIDDEN,
    HUD
  }

  /**
   * @return How many units can be displayed in a bar before they get boxed.
   */
  long getUnitsPerBar();
  
  /**
   * @return The max amount of this resource you can hold at a time when you first obtain the resource.
   */
  long getDefaultMaxLevel();

  /**
   * @return How much of this resourse you start with when you spawn.
   */
  long getDefaultLevel();

  /**
   * @return the color of the bar to display, in RGB format.
   */
  int getColor();

  /**
   * @return The visibility of the resource to the player.
   */
  ResourceVisibility getVisibility();

  //TODO: figure out of needed?
//	Ticker makeTicker(PlayerResource res);

  default String getTranslationKey() {
    Identifier id = CottonRPG.RESOURCES.getId(this);
    return "resource." + id.getNamespace() + "." + id.getPath();
  }

  default Text getName() {
    return new TranslatableText(getTranslationKey());
  }

  List<Text> getDescription();

  /**
   * What should happen when the resource bar is ticked
   */
  void tick(CharacterResourceEntry entry);

}