<%@page import="com.github.andrepenteado.util.ConfigHelper"%>

<!-- Título -->
<title>:: <%=ConfigHelper.get().getString("sistema.nome")%> :: <sitemesh:write property='title' /></title>

<!-- Configurações de cabeçalho-->
<base href="<%=request.isSecure() ? "https://" : "http://"%><%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/template/" />
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1"/>
<meta http-equiv="Content-Language" content="pt-br" />
<meta http-equiv="imagetoolbar" content="no" />
<meta name="robots" content="index" />
<meta name="MSSmartTagsPreventParsing" content="true" />
<meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="pragma" content="no-cache" />

<!-- Icone -->
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.png" />
<link rel="icon" href="<%=request.getContextPath()%>/favicon.png" />

<!-- Descrição -->
<meta name="description" content="<%=ConfigHelper.get().getString("sistema.nome")%>" />
<meta name="keywords" content="alphadev, gestão empresarial, contest" />
<meta name="author" content="André Penteado" />

<!-- Bibliotecas JavaScript -->
<script type="text/javascript" src="js/core.js"></script>

<!-- CSS -->
<link rel="stylesheet" type="text/css" href="css/print.css" media="print"/>
<link rel="stylesheet" type="text/css" href="css/form.css" media="screen, projection"/>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" media="screen, projection"/>

<!-- JQuery -->
<script type="text/javascript" src="jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="jquery/ui/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="jquery/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="jquery/dataTables/colvis/js/ColVis.min.js"></script>
<script type="text/javascript" src="jquery/dataTables/ColReorderWithResize.js"></script>
<script type="text/javascript" src="jquery/dataTables/TableTools.js"></script>
<script type="text/javascript" src="jquery/dataTables/tabletools/ZeroClipboard.js"></script>
<script type="text/javascript" src="jquery/dataTables/jquery.dataTables.rowGrouping.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/dataTables/css/jquery.dataTables.css"/>
<link rel="stylesheet" type="text/css" href="jquery/dataTables/css/jquery.dataTables_themeroller.css"/>
<link rel="stylesheet" type="text/css" href="jquery/dataTables/colvis/css/ColVis.css"/>

<!-- Boostrap twitter -->
<!-- <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css"/> -->

<sitemesh:write property='head' />