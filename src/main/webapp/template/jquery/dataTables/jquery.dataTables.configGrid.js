<c:if test="${popup != '1'}">
  "sDom": '<"H"CflRipT>rt<"F"ip>',
</c:if>
<c:if test="${popup == '1'}">
"sDom": '<"H"flip>rt<"F"ip>',
</c:if>
"bJQueryUI": true,
"sPaginationType": "full_numbers",
"bDestroy": true,
"bRetrieve": true,  
//"iDisplayLength" : 50,
"bProcessing": true,
//"sScrollY": $(window).height()*0.2,
"sScrollX": "100%",
"bScrollCollapse": true,
"oLanguage": {
    "sProcessing":     "Processando...",
    "sLengthMenu":     "Por Página: _MENU_",
    "sZeroRecords":    "A pesquisa não retornou resultados",
    "sInfo":           "Exibindo de _START_ até _END_ do total de _TOTAL_ registros",
    "sInfoEmpty":      "Nenhum registro encontrado",
    "sInfoFiltered":   "(de um total de _MAX_ registros)",
    "sInfoPostFix":    "",
    "sSearch":         "Filtrar:",
    "sLoadingRecords": "Carregando resultados...",
    "sUrl":            "",
    "oPaginate": {
        "sFirst":    "<li class=\"ui-state-default ui-corner-all\" title=\"Primeiro\"><span class=\"ui-icon ui-icon-seek-first\"></span></li>",
        "sPrevious": "<li class=\"ui-state-default ui-corner-all\" title=\"Anterior\"><span class=\"ui-icon ui-icon-seek-prev\"></span></li>",
        "sNext":     "<li class=\"ui-state-default ui-corner-all\" title=\"Próximo\"><span class=\"ui-icon ui-icon-seek-next\"></span></li>",
        "sLast":     "<li class=\"ui-state-default ui-corner-all\" title=\"Último\"><span class=\"ui-icon ui-icon-seek-end	\"></span></li>"
    }
},
"oColVis": {
    "buttonText": "<span class=\"ui-icon ui-icon-plusthick\" style=\"float: left\"></span><font size=\"2\">Colunas&nbsp;</font>",
    "iOverlayFade": 500,
    "sAlign": "left",
    "fnLabel": function ( index, title, th ) {
	    return (index+1) +'. '+ title;
	}
},
"oTableTools": {
	"sSwfPath": "jquery/dataTables/tabletools/swf/copy_csv_xls_pdf.swf",
    "aButtons": [
        { "sExtends": "pdf", "sButtonText": "&nbsp;&nbsp;PDF&nbsp;&nbsp;", "mColumns": "visible", "sPdfOrientation": "landscape" },
        { "sExtends": "xls", "sButtonText": "&nbsp;&nbsp;Excel&nbsp;&nbsp;", "mColumns": "visible" }
    ]
}