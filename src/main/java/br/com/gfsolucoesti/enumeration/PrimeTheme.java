package br.com.gfsolucoesti.enumeration;

import lombok.Getter;

public enum PrimeTheme {
	afterdark("afterdark"),
	afternoon("afternoon"),
	afterwork("afterwork"),
	blacktie("black-tie"),
	blitzer("blitzer"),
	bluesky("bluesky"),
	bootstrap("bootstrap"),
	casablanca("casablanca"),
	cruze("cruze"),
	cupertino("cupertino"),
	darkhive("dark-hive"),
	delta("delta"),
	dotluv("dot-luv"),
	eggplant("eggplant"),
	excitebike("excite-bike"),
	flick("flick"),
	glassx("glass-x"),
	home("home"),
	hotsneaks("hot-sneaks"),
	humanity("humanity"),
	lefrog("le-frog"),
	midnight("midnight"),
	mintchoc("mint-choc"),
	overcast("overcast"),
	peppergrinder("pepper-grinder"),
	redmond("redmond"),
	rocket("rocket"),
	sam("sam"),
	smoothness("smoothness"),
	southstreet("south-street"),
	start("start"),
	sunny("sunny"),
	swankypurse("swanky-purse"),
	trontastic("trontastic"),
	uidarkness("ui-darkness"),
	uilightness("ui-lightness"),
	vader("vader");
	
	@Getter
	private String value;
	
	private PrimeTheme(String value) {
		this.value = value;
	}
}
