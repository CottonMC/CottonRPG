package io.github.cottonmc.cottonrpg.components;

import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;

public interface IClassComponent extends Component, EntitySyncedComponent {
  /**
   * Invisible if <0
   * Unlockable if =0
   * Unlocked and possibly affects skill power if >0
   * @return Class level
   */  
  int getLevel();
}
