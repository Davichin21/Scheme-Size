require("Scheme-Size/mod");

Events.on(EventType.ClientLoadEvent, e => {
	// don`t check for updates
	if(!Core.settings.getBool("checkupdate")) return;

	var ver = Vars.mods.locateMod("scheme-size").meta.version;
	Http.get("https://api.github.com/repos/xzxADIxzx/Scheme-Size/tags", res => {
		var str = res.getResultAsString();
		var json = JSON.parse(str);

		if(json[0].name.slice(1) != ver){
			Vars.ui.showInfo("@updater.info")
		}
	});

	// delete old interface... idk why but does work in java?
	Vars.ui.hudGroup.children.get(5).clear();
	// Time.runTask(10, () => {
	// });
});

// Events.on(EventType.ResetEvent, e => {
// 	Time.runTask(10, () => {
// 		Vars.ui.hudGroup.children.get(9).clear();
// 	});
// });

// Events.on(EventType.WorldLoadEvent, e => {
// 	Time.runTask(10, () => {
// 		Vars.ui.hudGroup.children.get(9).clear();
// 	});
// });

// TEMP: after comoplete interface delete this sh*t code...

// why not
Vars.enableConsole = true;