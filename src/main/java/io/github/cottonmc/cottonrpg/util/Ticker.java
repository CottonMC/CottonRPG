package io.github.cottonmc.cottonrpg.util;

public class Ticker {

  @FunctionalInterface
  public interface TickHandler {
    void handle();
  }
  
  private long ticker = 0;
  private long tickTo;
  private TickHandler handler;
  
  public Ticker(long tickTo, TickHandler handler) {
    this.tickTo = tickTo;
    this.handler = handler;
  }
  
  public void tick() {
    if (ticker++ >= tickTo) {
      ticker = 0;
      handler.handle();
    }
  }

}
