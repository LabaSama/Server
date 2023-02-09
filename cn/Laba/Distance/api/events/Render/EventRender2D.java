
package cn.Laba.Distance.api.events.Render;

import cn.Laba.Distance.api.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D extends Event {
	private final ScaledResolution sr;
	private final float pt;

	public EventRender2D(ScaledResolution sr, float pt) {
		this.sr = sr;
		this.pt = pt;
	}

	public ScaledResolution getSR() {
		return this.sr;
	}
	
	public float getPartialTicks() {
		return this.pt;
	}
	public float getPT() {
		return this.pt;
	}
}
