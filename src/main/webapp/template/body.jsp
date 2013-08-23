<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw"%>

<h1><sitemesh:write property='title'/></h1>
<cite><sitemesh:write property="meta.header" /></cite>

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