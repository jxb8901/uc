/**
 * 左菜单，理论上可支持任意层次
 * @author jxb 2005.7.21
 *
 * 可通过两种方式指定动态图片
 * 1. 通过img_default属性单独指定(此种方式优先级最高)
 * 2. 通过Menu的defaultImage_level批量指定
 * 3. img_default,defaultImage_level表示静态显示的图片
 * 4. img_mouse_over,mouseOverImage_level表示鼠标移上时显示的图片
 * 5. img_expand,expandImage_level表示展开时显示的图片
 */
Menu = {	
	initMenu : function() {
		Menu.initSubMenu(document.getElementById("nav"), 1);
	},
	initSubMenu : function(menu, level) {
		var submenu = Menu.getChildByTagName(menu, "UL");
		if (!submenu) return;
		menu.child_menu = new Array();
		for (var i = 0, j = 0; i < submenu.childNodes.length; i++) {
			var menuitem = submenu.childNodes[i];
			if (menuitem.tagName && menuitem.tagName.toUpperCase() == "LI") {
				menuitem.parent_menu = menu;
				menu.child_menu[j++] = menuitem;
				Menu.initMenuItem(menuitem, level);
			}
		}
	},
	initMenuItem : function(menuitem, level) {
		var aEle = Menu.getChildByTagName(menuitem, "A");
		var hasSubMenu = Menu.getChildByTagName(menuitem, "UL") ? true : false;
		if (aEle && hasSubMenu) { 
			aEle.onclick = function() {
				Menu.toggleMenu(this.parentNode);
			}; 
		}
		
		if (aEle) {
			var img = aEle.getAttribute("img_default");
			if (!img && hasSubMenu) img = Menu["defaultImage_"+level];
			if (img) {
				aEle.setAttribute("img_default", img);
				aEle.style.backgroundImage = "url(" + img + ")";
			}
			
			img = aEle.getAttribute("img_mouse_over");
			if (!img && hasSubMenu) img = Menu["mouseOverImage_"+level];
			if (img) {
				aEle.setAttribute("img_mouse_over", img);
				aEle.onmouseover = function() {
					Menu.setBackgroundImage(this, this.getAttribute("img_mouse_over"));
				};
				aEle.onmouseout = function() {
					if (Menu.isDisplay(this.parentNode)) 
						Menu.setBackgroundImage(this, this.getAttribute("img_expand"));
					else 
						Menu.setBackgroundImage(this, this.getAttribute("img_default"));
				};
			}
			if (!aEle.getAttribute("img_expand")) aEle.setAttribute("img_expand", Menu["expandImage_"+level]);
		}
		
		Menu.initSubMenu(menuitem, level + 1);
	},
	isDisplay : function(submenu) {
		return submenu.getAttribute("display") == "true";
	},
	setBackgroundImage : function(aEle, img) {
		if (img) aEle.style.backgroundImage = "url(" + img + ")";
	},
	getChildByTagName : function(parentNode, tagName) {
		for (var i = 0; i < parentNode.childNodes.length; i++) {
			var e = parentNode.childNodes[i];
			if (e.tagName && e.tagName.toUpperCase() == tagName) return e;
		}
	},
	toggleMenu : function(menuitem) {
		if (!Menu.isDisplay(menuitem)) {
			Menu.hideAll();
			Menu.showMenu(menuitem);
		}
		else {
			Menu.hideMenu(menuitem);
		}
	},
	showMenu : function(menuitem) {
		if (Menu.isDisplay(menuitem)) return;
		if (menuitem.parent_menu && menuitem.parent_menu != document.getElementById("nav")) Menu.showMenu(menuitem.parent_menu);
		var ele = Menu.getChildByTagName(menuitem, "UL");
		ele.style.display = "block";
		menuitem.setAttribute("display", "true");
		ele = Menu.getChildByTagName(menuitem, "A");
		if (ele) Menu.setBackgroundImage(ele, ele.getAttribute("img_expand"));
	},
	hideMenu : function(menuitem) {
		if (!Menu.isDisplay(menuitem)) return;
		for (var i = 0; i < menuitem.child_menu.length; i++) {
			Menu.hideMenu(menuitem.child_menu[i]);
		}
		var ele = Menu.getChildByTagName(menuitem, "UL");
		if (!ele) return;
		ele.style.display = "none";
		menuitem.setAttribute("display", "false");
		ele = Menu.getChildByTagName(menuitem, "A");
		if (ele) Menu.setBackgroundImage(ele, ele.getAttribute("img_default"));
	},
	hideAll : function () {
		var menuitem = document.getElementById("nav");
		for (var i = 0; i < menuitem.child_menu.length; i++) {
			Menu.hideMenu(menuitem.child_menu[i]);
		}
	}
}
initMenu = Menu.initMenu;