var bladefunction;

function openBlade(id) {
	try {
		document.getElementById(id).style.width = "94%";
		createEscape(id);
	} catch (e) {
		console.error('trying to open blade for '+id,e)
	}
}

function openBlade(id, width) {
	try {
		document.getElementById(id).style.width = width+"%";
		createEscape(id);
	} catch (e) {
		console.error('trying to open blade for '+id,e)
	}
}

function closeBlade(id) {
	document.getElementById(id).style.width = "0%";
	removeEscape();
}

function removeEscape() {
	document.removeEventListener("keyup", bladefunction);
}

function createEscape(id) {
	var navid = id;
	bladefunction = function(event) {
		if (event.key === "Escape") {
			closeBlade(navid);
		}
	};
	bladelistener = document.addEventListener("keyup", bladefunction);
}
