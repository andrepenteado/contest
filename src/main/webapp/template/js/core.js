/**
 * Formata o campo apenas com letras maiuscula
 */
function isUpper(obj) {
    obj.value = obj.value.toUpperCase();
}

/**
 * Formata o campo apenas com letras minusculas
 */
function isLower(obj) {
    obj.value = obj.value.toLowerCase();
}

/**
 * Formata o campo apenas com números
 */
function isNumber(obj) {
    var retorno = "";
    var flag = 0;  // Caso exista caracter invalido

    for (var i=0; i < obj.value.length; i++) {
        if (obj.value.charCodeAt(i) < 48 || obj.value.charCodeAt(i) > 57) {
            if (flag==0) {  // Mostra apenas 1 (um) alert por validacao
                alert("Caracter '" + obj.value.charAt(i) + "' invalido");
                flag = 1;
            }
        }
        else {
            retorno += obj.value.charAt(i);
        }
    }

    // Apenas atualiza o campo caso haja caracter invalido
    if (flag==1) {
        obj.value = retorno;
        obj.focus();
    }

    return true;
}

/**
 * Formata o campo no estilo moeda
 */
function isCurrency(obj) {
    var retorno = "";
    var flag = 0;  // Caso exista caracter invalido

    for (var i=0; i < obj.value.length; i++) {
        if (obj.value.charCodeAt(i) < 44 || obj.value.charCodeAt(i) > 57) {
            if (flag==0) {  // Mostra apenas 1 (um) alert por validacao
                alert("Caracter '" + obj.value.charAt(i) + "' invalido");
                flag = 1;
            }
        }
        else {
            if (obj.value.charCodeAt(i)==44) {
                flag = 1;
                retorno += ".";
            }
            else
                retorno += obj.value.charAt(i);
        }
    }

    // Apenas atualiza o campo caso haja caracter invalido
    if (flag==1) {
        obj.value = retorno;
        obj.focus();
    }

    return true;
}

function isEnter(event) {     
    NS4 = (document.layers) ? true : false;

    var code = 0;
    
    if (NS4)
        code = event.which;
    else
        code = event.keyCode;

    if (code==13)
        return true;
    else
        return false;
}

//metodo que corrige problema no inputDate do Mentawai
//function changeCalendarShowAtElement() {
//	if ((typeof Calendar) != 'undefined') {
//		Calendar.prototype.showAtElement = function(el) {
//			var p = Calendar.getAbsolutePos(el);
//			var yScroll = $('cc').scrollTop;
//			this.showAt(p.x, p.y + el.offsetHeight - yScroll);
//		}
//	}
//}

function f_clientWidth() {
    return f_filterResults (
        window.innerWidth ? window.innerWidth : 0,
        document.documentElement ? document.documentElement.clientWidth : 0,
        document.body ? document.body.clientWidth : 0
    );
}
function f_clientHeight() {
    return f_filterResults (
        window.innerHeight ? window.innerHeight : 0,
        document.documentElement ? document.documentElement.clientHeight : 0,
        document.body ? document.body.clientHeight : 0
    );
}

function f_filterResults(n_win, n_docel, n_body) {
    var n_result = 0
    if (n_win) n_result = n_win;
    else if (n_docel) n_result = n_docel;
    else if (n_body) n_result = n_body; 
    return n_result;
}

/*
 * @name: trimText(x) 
 * @version: 1.0 
 * @author: Leonardo Fortunato 
 * @param: x => String 
 * @return: String 
 * @description: Remove espacos em branco no inicio e no fim da string.
 */
function trimText(x) {
	while (x.charAt(0) == ' ')
		x = x.substring(1, x.length);
	while (x.charAt(x.length - 1) == ' ')
		x = x.substring(0, x.length - 1);
	return x;
}

/*
 * @name: TrimTexto(txt) 
 * @version: 1.0 
 * @author: Leonardo Fortunato 
 * @param: txt => String 
 * @return: String 
 * @description: Sinonimo de trimText.
 */
function TrimTexto(txt) {
	return trimText(txt);
}

/*
 * @name: getPageScroll() 
 * @version: 1.0 
 * @author: Leonardo Fortunato (adptado de original da globo.com) 
 * @return: Estrutura contendo a posicao da barra de
 * rolagem vertical @description: Pega a posicao da barra de rolagem vertical do
 * navegador
 */
function getPageScroll() {
	var yScroll;
	if (self.pageYOffset)
		yScroll = self.pageYOffset;
	else if (document.documentElement && document.documentElement.scrollTop)
		yScroll = document.documentElement.scrollTop;
	else if (document.body)
		yScroll = document.body.scrollTop;
	return {
		yScroll :yScroll
	};
}

/*
 * @name: getPageSize() 
 * @version: 1.0 
 * @author: Leonardo Fortunato (adptado de original da globo.com) 
 * @return: Estrutura contendo os valores de width e
 * height da pagina e da janela respectivamente. 
 * @description: Pega os valores de height e width da pagina e da janela do navegador.
 */
function getPageSize() {
	var xScroll, yScroll;
	if (window.innerHeight && window.scrollMaxY) {
		xScroll = document.body.scrollWidth;
		yScroll = window.innerHeight + window.scrollMaxY;
	} 
	else if (document.body.scrollHeight > document.body.offsetHeight) {
		xScroll = document.body.scrollWidth;
		yScroll = document.body.scrollHeight;
	} 
	else {
		xScroll = document.body.offsetWidth;
		yScroll = document.body.offsetHeight;
	}
	var windowWidth, windowHeight;
	if (self.innerHeight) {
		windowWidth = self.innerWidth;
		windowHeight = self.innerHeight;
	} 
	else if (document.documentElement
			&& document.documentElement.clientHeight) {
		windowWidth = document.documentElement.clientWidth;
		windowHeight = document.documentElement.clientHeight;
	} 
	else if (document.body) {
		windowWidth = document.body.clientWidth;
		windowHeight = document.body.clientHeight;
	}
	if (yScroll < windowHeight)
		pageHeight = windowHeight;
	else
		pageHeight = yScroll;
	if (xScroll < windowWidth)
		pageWidth = windowWidth;
	else
		pageWidth = xScroll;
	return {
		pageWidth :pageWidth,
		pageHeight :pageHeight,
		windowWidth :windowWidth,
		windowHeight :windowHeight
	};
}

/*
 * @name: setChageColor(x) 
 * @version: 1.0 
 * @author: Leonardo Fortunato 
 * @param: x => Object 
 * @description: Define no onFocus e onBlur a mudança de cor do objeto.
 */
function setChageColor(x) {
	if (x != undefined) {
		if ((x.tagName.toUpperCase() == 'INPUT' && (x.type.toUpperCase() == 'TEXT' || x.type
				.toUpperCase() == 'PASSWORD'))
				|| x.tagName.toUpperCase() == 'TEXTAREA') {
			var p1 = x.onfocus;
			x.onfocus = function() {
				if (p1)
					p1();
				setColorFocus(this);
			}
			var p2 = x.onblur;
			x.onblur = function() {
				if (p2)
					p2();
				setColorBlur(this);
			}
		}
	}
}

/*
 * @name: setColorFocus(x) 
 * @version: 1.0 
 * @author: Leonardo Fortunato 
 * @param: x => Object Input or Object Select or Object TextArea 
 * @description: Muda o estilo do objeto para quando ele estiver com foco.
 */
function setColorFocus(x) {
	if (x != undefined) {
		if (!x.readOnly) {
			if (hasClass(x, 'erroform')) {
				removeClass(x, 'erroform');
				addClass(x, 'erroformfoco');
			} else {
				addClass(x, 'foco');
			}
		}
	}
}

/*
 * @name: setColorBlur(x) 
 * @version: 1.0 
 * @author: Leonardo Fortunato 
 * @param: x => Object Input or Object Select or Object TextArea 
 * @description: Muda o estilo do objeto para quando ele perder o foco.
 */
function setColorBlur(x) {
	if (x != undefined) {
		if (!x.readOnly) {
			if (hasClass(x, 'erroformfoco')) {
				removeClass(x, 'erroformfoco');
				addClass(x, 'erroform');
			} else {
				removeClass(x, 'foco');
			}
		}
	}
}

/*
 * @name: setFocus(x) 
 * @version: 1.0 
 * @author: Leonardo Fortunato 
 * @param: x => Object Input or Object Select or Object TextArea 
 * @description: Define foco para um objeto de formulario.
 */
function setFocus(x) {
	if (x != undefined && !x.disabled && !x.readOnly)
		x.focus();
}

/*
 * @name: SetFoco(obj) 
 * @version: 1.0 
 * @author: Leonardo Fortunato 
 * @param: obj => Object Input or Object Select or Object TextArea 
 * @description: Sinonimo de setFocus
 */
function SetFoco(obj) {
	setFocus(obj);
}