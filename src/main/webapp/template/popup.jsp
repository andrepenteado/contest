<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw"%>

<html>
<head>
  <%@include file="head.jsp"%>
  <link rel="stylesheet" type="text/css" href="jquery/ui/redmond/jquery-ui-1.8.20.custom.css" />
  
  <style type="text/css">
  /* Padrão */
  * {
     margin: 0;
     padding: 0;
  }
  a {
     color: #36C;
  }
  a:hover {
     color: #06F;
  }
  p,cite,code,ul {
     font-size: 1.2em;
     padding-bottom: 1.2em;
  }
  h1 {
     font-size: 1.4em;
     margin-bottom: 4px;
  }
  cite {
     background: url(images/star.png) no-repeat;
     color: #666;
     display: block;
     font: normal 1.3em Verdana, Arial, Helvetica, Sans-Serif;
     padding-left: 23px;
  }
  table.form td.barrabotao {
     text-align: center;
  }
  
  html {
      margin: 0px;
      padding: 0px;
  }
  
  body {
      margin: 0px auto;
      padding: 0px;
      text-align: left;
      color: #444;
      font: 74% Verdana, Arial, Helvetica, Sans-Serif;
      margin: 0;
  }
  
  hr {
      display: none;
  }
  
  #c {
      width: 100%;
  }
  </style>
</head>
<body>
  <cite><b><font color="#367EA6"><sitemesh:write property='title' /></font></b></cite>

  <mtw:outMessages>
    <mtw:loop>
      <div class="ui-widget">
        <div class="ui-state-highlight ui-corner-all"> 
          <p>
            <center><strong><mtw:out/></strong></center>
          </p>
        </div>
      </div>
    </mtw:loop>
    <br/>
  </mtw:outMessages>
  <mtw:outErrors>
    <mtw:loop>
      <div class="ui-widget">
        <div class="ui-state-error ui-corner-all"> 
          <p>
            <center><strong><mtw:out/></strong></center>
          </p>
        </div>
      </div>
    </mtw:loop>
    <br>
  </mtw:outErrors>

  <sitemesh:write property='body' />
</body>
</html>
