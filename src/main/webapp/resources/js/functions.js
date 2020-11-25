function copyToClip(str){
	// Create new element
	var el = document.createElement('textarea');
	// Set value (string to be copied)
	el.value = str;
	// Set non-editable to avoid focus and move outside of view
	el.setAttribute('readonly', '');
	el.style = {
		position : 'absolute',
		left : '-9999px'
	};
	document.body.appendChild(el);
	// Select text inside element
	el.select();
	// Copy text to clipboard
	document.execCommand('copy');
	// Remove temporary element
	document.body.removeChild(el);
}

function verifyScreen(elemento) {
	var h = $(window).height();
	var w = $(window).width();
	h = h-200+"px";
	w = w-200+"px";
	var scroll = $(elemento);
	scroll.css("height",h)
	scroll.css("width","100%")
}
