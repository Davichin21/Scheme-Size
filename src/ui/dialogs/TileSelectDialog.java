package mindustry.ui.dialogs;

import arc.util.*;
import arc.func.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.scene.style.*;
import arc.graphics.g2d.*;
import mindustry.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.gen.*;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.content.*;
import mindustry.graphics.*;

public class TileSelectDialog extends BaseDialog{

	public Cons3<Floor, Block, Floor> callback;

	private int selected = 0;
	private Table category = new Table();
	private Table content = new Table();

	private Floor floor = Blocks.air.asFloor();
	private Block block = Blocks.air;
	private Floor overlay = Blocks.air.asFloor();

	private Image floorImg;
	private Image blockImg;
	private Image overlayImg;

	public TileSelectDialog(String name){
		super(name);
		addCloseButton();

		cont.add(category).size(288f, 270f).left();
		cont.add(content).growX();
		cont.table().width(288f).right();

		floorImg = template("@tile.floor", 0, b -> !(b instanceof Floor) || b instanceof OreBlock, b -> { floor = b.asFloor(); updateimg(); });
		blockImg = template("@tile.block", 1, b -> !(b instanceof StaticWall), b -> { block = b; updateimg(); });
		overlayImg = template("@tile.overlay", 2, b -> !(b instanceof OreBlock), b -> { overlay = b.asFloor(); updateimg(); });
	}

	private void rebuild(Boolf<Block> skip, Cons<Block> callback){
		content.clear();
		content.table(table -> {
			table.button(Icon.none, () -> { 
				callback.get(null);
			}).size(64f);
			table.button(Icon, () -> { 
				callback.get(Blocks.air);
			}).size(64f);

			Vars.content.blocks().each(block -> {
				if(skip.get(block) || block.isHidden()) return;

				var drawable = new TextureRegionDrawable(block.icon(Cicon.full));
				table.button(drawable, () -> { 
					callback.get(block);
				}).size(64f);

				if(table.getChildren().count(i -> true) % 10 == 9) table.row();
			});
		});
	}

	private Image template(String name, int select, Boolf<Block> skip, Cons<Block> callback){
		Button check = new Button(Styles.transt);
		check.changed(() -> {
			selected = select;
			rebuild(skip, callback);
		});

		Table icon = new Table(){
			@Override
			public void draw(){
				super.draw();
				Draw.color(check.isChecked() ? Pal.accent : Pal.gray);
				Draw.alpha(parentAlpha);
				Lines.stroke(Scl.scl(4f));
				Lines.rect(x, y, width, height);
				Draw.reset();
			}
		};
		icon.add(Image img = new Image().setScaling(Scaling.bounded)).pad(8f).grow();

		check.add(icon).size(74f);
		check.table(t -> {
			t.labelWrap(name).growX().row();
			t.image().height(4f).color(Pal.gray).growX().bottom().padTop(4f);
		}).size(170f, 74f).pad(10f);

		category.add(check).checked(t -> selected == select).size(264f, 74f).padBottom(16f).row();
		return img;
	}

	private void updateimg(){
		floorImg.setDrawable(floor.icon(Cicon.full));
		blockImg.setDrawable(block.icon(Cicon.full));
		overlayImg.setDrawable(overlay.icon(Cicon.full));
	}

	public void select(boolean show, Cons3<Floor, Block, Floor> callback){
		this.callback = callback;
		if(show) show();
		else callback.get(floor, block, overlay);
		updateimg();
	}
}