package mindustry.ui.dialogs;

import arc.*;
import arc.util.*;
import arc.func.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.graphics.g2d.*;
import mindustry.*;
import mindustry.ui.*;
import mindustry.gen.*;
import mindustry.game.*;
import mindustry.graphics.*;

public class ModTeamSelectDialog extends BaseDialog{

	public Cons2<Team, Player> callback;
	public Player player;

	private Table list = new Table();
	private Table team = new Table();

	public ModTeamSelectDialog(String name){
		super(name);
		addCloseButton();

		template("team-derelict", Team.derelict);
		template("team-sharded", Team.sharded);
		template("team-crux", Team.crux);
		template("status-electrified-ui", Team.green);
		template("status-spore-slowed-ui", Team.purple);
		template("status-wet-ui", Team.blue);

		cont.add(list).padRight(16f);
		cont.add(team);
	}

	private void rebuild(){
		list.clear();
		Groups.player.each(player -> {
			Button check = new Button(Styles.transt);
			check.changed(() -> this.player = player);

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
            icon.add(new Image(player.icon()).setScaling(Scaling.bounded)).grow();
            icon.name = player.name();

            check.add(icon).size(74f);
            check.labelWrap("[#" + player.color().toString().toUpperCase() + "]" + player.name()).width(170f).pad(10);

			list.add(check).checked(t -> this.player == player).size(264f, 74f).padBottom(16f).row();
		});
	}

	private void template(String icon, Team team){
		var draw = Core.atlas.drawable(icon);
		this.team.button(draw, () -> {
			callback.get(team, player);
			hide();
		}).size(64).row();
	}

	public void select(Cons2<Team, Player> callback){
		this.callback = callback;
		this.player = Vars.player;
		rebuild();
		show();
	}
}