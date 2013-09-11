"sDom": '<"H">rt<"F">',
"bJQueryUI": true,
"bDestroy": true,
"bPaginate": false,
"bRetrieve": true,  
"bProcessing": true,
"sScrollX": "100%",
"bScrollCollapse": true,
"oLanguage": {
    "sProcessing":     "Processando...",
    "sZeroRecords":    "A pesquisa não retornou resultados",
    "sInfo":           "_TOTAL_ registros",
    "sInfoEmpty":      "Nenhum registro encontrado",
    "sInfoFiltered":   "(de um total de _MAX_)",
    "sInfoPostFix":    "",
    "sSearch":         "Filtrar:",
    "sLoadingRecords": "Carregando resultados...",
    "sUrl":            ""
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