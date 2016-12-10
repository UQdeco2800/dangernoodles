package uq.deco2800.dangernoodles;

import javafx.scene.canvas.GraphicsContext;

@FunctionalInterface
public interface RenderAction {
	void doRenderAction(GraphicsContext context, FrameHandler handler);
}
