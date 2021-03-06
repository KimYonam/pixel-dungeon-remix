/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.windows;

import com.nyrds.android.util.GuiProperties;
import com.nyrds.pixeldungeon.levels.objects.LevelObject;
import com.nyrds.pixeldungeon.levels.objects.sprites.LevelObjectSprite;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.Text;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Window;

public class WndInfoCell extends Window {
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	
	private static final String TXT_NOTHING	= Game.getVar(R.string.WndInfoCell_Nothing);
	
	public WndInfoCell( int cell ) {
		
		super();
		
		int tile = Dungeon.level.map[cell];
		if (Dungeon.level.water[cell]) {
			tile = Terrain.WATER;
		} else if (Dungeon.level.pit[cell]) {
			tile = Terrain.CHASM;
		}
		
		IconTitle titlebar = new IconTitle();
		if (tile == Terrain.WATER) {
			Image water = new Image( Dungeon.level.getWaterTex() );
			water.frame( 0, 0, DungeonTilemap.SIZE, DungeonTilemap.SIZE );
			titlebar.icon( water );
		} else {
			titlebar.icon( DungeonTilemap.tile( cell ) );
		}
		titlebar.label( Dungeon.level.tileName( tile ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		float yPos = titlebar.bottom();

		Text info = PixelScene.createMultiline(GuiProperties.regularFontSize());
		add( info );
		
		StringBuilder desc = new StringBuilder( Dungeon.level.tileDesc( tile ) );
		
		final char newLine = '\n';

		LevelObject obj = Dungeon.level.objects.get(cell);

		if(obj != null) {
			LevelObjectSprite sprite = new LevelObjectSprite();
			sprite.reset(obj);
			sprite.setPos(0,0);
			add(sprite);

			desc.append(newLine);
			desc.append(obj.desc());
		}

		for (Blob blob:Dungeon.level.blobs.values()) {
			if (blob.cur[cell] > 0 && blob.tileDesc() != null) {
				if (desc.length() > 0) {
					desc.append( newLine );
				}
				desc.append( blob.tileDesc() );
			}
		}

		info.text( desc.length() > 0 ? desc.toString() : TXT_NOTHING );
		info.maxWidth(WIDTH);
		info.measure();
		info.x = titlebar.left();
		info.y = yPos + GAP;
		
		resize( WIDTH, (int)(info.bottom()) );
	}
}
