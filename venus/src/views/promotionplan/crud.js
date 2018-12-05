
var mapstr = "${(rule.mapping)?default('')?js_string}";
var mapParams = "${ruleParams?default('')}";
var orgonSelectrule ;

$(function(){
   loadRuleMapStr();
   loadRuleMapParams();
   //document.getElementById("create_rule_idno").onchange =  reloadRuleMap;
   orgonSelectrule = window.onSelectrule;
   window.onSelectrule = function(selectedValue){
      orgonSelectrule(selectedValue);
   	//alert('new select : map : ' + selectedValue.mapping);
   	mapstr = selectedValue.mapping;
   	loadRuleMapStr();
   }
});

function reloadRuleMap(){
 	mapstr = "";
 	mapParams = "";
 	alert("change ! ");
}

function loadRuleMapStr(){
	   if(mapstr == null || mapstr.length ==0){
	     return ;
	  }
	  
	  var reg = /<([^:>]*):([^:>]*)>/g;
	  //reg = /<(\w*):(\w*)>/;
	  var editctrstr = "<input type='text' name=\"ruleParams['$1']\" id=\"ruleParams['$1']\" value='$2' size='3'>";
	  var dispstr = mapstr.replace(reg,editctrstr).replace(/[\r\n]+/g, "<br/>");
	  //dispstr = "<pre>" + dispstr + "</pre>";
	  //$("#pre_ruleparams").empty();
	  //$(dispstr).appendTo("#pre_ruleparams");
	  $("#pre_ruleParams").get(0).innerHTML=dispstr;
}
function loadRuleMapParams(){
 	if(mapParams == null || mapParams.length == 0 || mapParams == "{}")
 	  return ;
 	var reg =/([^,\{,\},=,\s]*)=([^,\{,\},=,\s]*)/g;
 	var msg = "";
       while(true){
	    	var result = reg.exec(mapParams);
	    	if(result == null) break ;
	    	//msg += "var : " + result[1] +" , val : " + result[2] + "\r\n";
	    var ctlname = "ruleParams['"+ result[1] +  "']";
	    //ctlname = "input[@name=ruleParams[\'" + result[1] + "\']" ;
	    var ctl = document.getElementById(ctlname);
	    msg += "ctl name : _" + ctlname + "_ var : " + ctl.name + " , val : " + ctl.value + "\r\n";
	    ctl.value = result[2]
    }
}
