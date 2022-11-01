[#assign rootPage = cmsfn.siteRoot(content, "training-home") /]
[#assign page = cmsfn.page(content)]
[#assign pageLink = cmsfn.link(page)]

[#assign eventsRoot = cmsfn.contentByPath("/customersEvents/", "events")]


  
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <button aria-controls="navbar-collapse-1" type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a href="${cmsfn.link(rootPage)}" class="navbar-brand">${rootPage.title!rootPage.@name}</a>
    </div>
    
    <div class="collapse navbar-collapse" id="navbar-collapse-1">
      <div class="navbar-left">
        [@childrenNodes node=rootPage /]
        [@listEvents events=eventsRoot /]
      </div> <!-- /navbar-left -->
    </div><!-- /.navbar-collapse -->  
  </div><!-- /.container-fluid -->
</nav>


[#macro childrenNodes node ulClass="nav navbar-nav"]
  
   [#list cmsfn.children(node, "mgnl:page")]
    <ul class="${ulClass}">
      [#items as childNode]
   
        [#if  childNode.checkboxShowPage!false]
          [#assign hasChildren = (cmsfn.children(childNode, "mgnl:page")?size)>0 /]
          [#if hasChildren]
              <li class="dropdown">
                  <a href="${cmsfn.link(childNode)!}" > ${childNode.title!childNode.@name}</a>
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" >
                      <span class="caret"></span>
                  </a>
                  [#if childNode.@depth <= 3]
                      [@childrenNodes node=childNode ulClass="dropdown-menu"/]
                  [/#if]
              </li>
          [#else]
              <li><a href="${cmsfn.link(childNode)!}">${childNode.title!childNode.@name}</a></li>      
          [/#if]
        [/#if]
      [/#items]
      
    </ul>
  [/#list]
[/#macro]


[#macro listEvents events ulClass=""]
[#assign pageLinkEvents = cmsfn.link(page)]
  [#assign pageLinkEvents = pageLink?keep_before_last('/') + "/see-events.html"]
   [#list cmsfn.children(events,"event")![]]
  
   <ul class="nav navbar-nav">
    <li class="dropdown">
      <a  href="#"> Events Complex</a>
      <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" >
                    <span class="caret"></span>
      </a>
      <ul class="dropdown-menu">
        [#items as event]
         
          [#assign linkWithSelector = ensureLinkEndsWithDotHtml(pageLinkEvents)?replace('.html','~'+event.@name+'~.html')]
          <li><a href="${linkWithSelector}" style="text-transform: capitalize">${event.name}</a></li>
        [/#items]
      </ul>
    </li>
    </ul>
  [/#list]
[/#macro]

[#function ensureLinkEndsWithDotHtml link]
 
    [#if !link?contains(".html")]
        [#if link?ends_with("/")]
            [#assign link = link?keep_before_last('/')]
        [/#if]
        [#return link+'.html']
    [/#if]      
     
    [#return link]
     
[/#function]
