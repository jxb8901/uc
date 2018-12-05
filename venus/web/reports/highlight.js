dojo.require("dojo.event.*");			
dojo.require("dojo.html.*");
			
function addHighlight(evt)
{							
	dojo.html.setClass(evt.currentTarget,"highlight");
}		

function removeHighlightOdd(evt)
{							
	dojo.html.setClass(evt.currentTarget,"odd");
}	
		
function removeHighlightEven(evt)
{							
	dojo.html.setClass(evt.currentTarget,"even");
}	

function removeHighlightTriggered(evt)
{							
	dojo.html.setClass(evt.currentTarget,"even triggered");
}	
		
function init()
{
	var evenNode = dojo.html.getElementsByClass("even");				
	dojo.event.connect(evenNode, "onmouseover", dj_global, "addHighlight");
	dojo.event.connect(evenNode, "onmouseout", dj_global, "removeHighlightEven");
				
	var oddNode = dojo.html.getElementsByClass("odd");				
	dojo.event.connect(oddNode, "onmouseover", dj_global, "addHighlight");
	dojo.event.connect(oddNode, "onmouseout", dj_global, "removeHighlightOdd");
	
	var triggeredNode = dojo.html.getElementsByClass("triggered");				
	dojo.event.connect(triggeredNode, "onmouseover", dj_global, "addHighlight");
	dojo.event.connect(triggeredNode, "onmouseout", dj_global, "removeHighlightTriggered");
}

dojo.event.connect(dojo, "loaded", "init") 