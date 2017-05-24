package io.anuke.novix.tools;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;

import io.anuke.novix.modules.Core;
import io.anuke.utools.MiscUtils;

public class DrawAction{
	public ObjectMap<Integer, ColorPair> positions = new ObjectMap<Integer, ColorPair>();
	public PixelCanvas fromCanvas; //used only for undoing image operations that change the canvas
	public PixelCanvas toCanvas;
	

	public void push(int x, int y, int from, int to){
		if(from == to) return; //ignore action that doesn't do anything
		int key = MiscUtils.asInt(x, y, io.anuke.novix.drawgrid.canvas.width());
		if(positions.containsKey(key)){
			ColorPair pos = positions.get(key);
			pos.tocolor = to;
		}else{
			positions.put(key, new ColorPair(from, to));
		}
	}

	public void clear(){
		positions.clear();
	}

	public void apply(PixelCanvas canvas, boolean reapply){
		if(fromCanvas != null){
			io.anuke.novix.i.drawgrid.actionSetCanvas(reapply ? toCanvas : fromCanvas);
			return;
		}
		Keys<Integer> keys = positions.keys();
		
		for(Integer i : keys){
			ColorPair pos = positions.get(i);
			int x = i % io.anuke.novix.drawgrid.canvas.width();
			int y = i / io.anuke.novix.drawgrid.canvas.width();
			
			Pixmap.setBlending(Blending.None);
			canvas.drawPixelActionless(x, y, reapply ? pos.tocolor : pos.fromcolor);

			Pixmap.setBlending(Blending.SourceOver);
		}
		canvas.updatePixmapColor();
		canvas.updateTexture();
	}

	class ColorPair{
		int fromcolor;
		int tocolor;

		public ColorPair(int fromcolor, int tocolor){
			this.fromcolor = fromcolor;
			this.tocolor = tocolor;
		}
	}
	
	
	public String toString(){
		return "DrawAction: " + positions.size + "x";
	}
}
