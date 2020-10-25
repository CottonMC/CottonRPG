package io.github.cottonmc.cottonrpg.data.resource;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.RpgDataType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

/**
 * A type of resource, like mana, rage, blood, etc.
 */
public interface CharacterResource extends RpgDataType {

	/**
	 * How the resource should be displayed/synced to the user
	 * INVISIBLE: Not synced to client.
	 * HIDDEN: Synced to client, but not displayed in Cotton RPG's HUD.
	 * HUD: Synced to client, and displayed in the HUD.
	 */
	enum ResourceVisibility {
		INVISIBLE,
		HIDDEN,
		HUD
	}

	@Override
	default Identifier getId() {
		return Objects.requireNonNull(CottonRPG.RESOURCES.getId(this), this + " is not a registered resource");
	}

	/**
	 * @return How many units can be displayed in a bar before they get boxed.
	 */
	int getUnitsPerBar();

	/**
	 * @return The max amount of this resource you can hold at a time when you first obtain the resource.
	 */
	int getDefaultMaxLevel();

	/**
	 * @return How much of this resource you start with when you spawn.
	 */
	int getDefaultLevel();

	/**
	 * @return the color of the bar to display, in RGB format.
	 */
	int getColor();

	/**
	 * @return The visibility of the resource to the player.
	 */
	ResourceVisibility getVisibility();

	/**
	 * @param entry The entry on this player.
	 * @return A Ticker to manage what should happen to this resource every tick.
	 */
	Ticker makeTicker(CharacterResourceEntry entry);

	/**
	 * @return The translation key for this resource. Typically at `resource.<namespace>.<path>`.
	 */
	default String getTranslationKey() {
		Identifier id = this.getId();
		return "resource." + id.getNamespace() + "." + id.getPath();
	}

	/**
	 * @return The name of this resource to be displayed in a gui.
	 */
	default Text getName() {
		return new TranslatableText(getTranslationKey());
	}

	/**
	 * @return Lines of text to be put in a gui.
	 */
	List<Text> getDescription();

}