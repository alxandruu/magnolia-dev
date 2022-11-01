

<div class='events-list'>
    [#assign page = cmsfn.page(content)]
    [#assign pageLink = cmsfn.link(page)]
    [#assign eventSelectorName = state.getSelector()]
    [#assign eventNode = cmsfn.contentByPath("/"+(eventSelectorName!), "events")]
       
    [#if eventSelectorName?has_content && eventNode?has_content]
     
        [#assign eventName = eventNameFallback(eventNode)!]
        [#if eventName?has_content]
            <h3>${eventName}</h3>
        [/#if]
        [#if eventNode.location?has_content]
            <h4>[#if eventNode.date?has_content] ${eventNode.date?string["dd.MM.YYYY"]!} - [/#if]${eventNode.location}</h4>
        [/#if]
         
        ${eventNode.description!}
         
        [#if pageLink?has_content]
            <hr class="section-heading-spacer">
            <h4><a href="${pageLink!}">Show all events</a></h4>
            <hr class="section-heading-spacer">
        [/#if]
 
    [#else] [#-- IF THERE IS NO SELECTOR, LIST OF EVENTS WILL BE DISPLAYED --]
         
        [#assign eventsRoot = cmsfn.contentByPath("/", "events")]
        [#assign events = cmsfn.children(eventsRoot,"event")![]]
 
        [#list events]
            <ul class="list-group">
                <h3><li class="list-group-item active">Total events: ${events?size} </li></h3>
                [#items as event]
                    [#-- It might be, that the page link does not contain '.html' and/or ends with '/' --]
                    [#assign linkWithSelector = ensureLinkEndsWithDotHtml(pageLink)?replace('.html','~'+event.@name+'~.html')]
                    [#assign eventName = eventNameFallback(event)!]
                     
                    [#if linkWithSelector?has_content && eventName?has_content]    
                        <a href="${linkWithSelector}" class="list-group-item">
                    [#else]
                        <li class="list-group-item">
                    [/#if]
                            <h5>Event #${event_index+1}[#if event.date?has_content] - ${event.date?string["dd.MM.YY"]}[/#if]:</h5>
                            <h4>${eventName}</h4>
                            ${event.description!}
                     
                    [#if linkWithSelector?has_content && eventName?has_content]    
                        </a>
                    [#else]
                        </li>
                    [/#if]
                [/#items]
            </ul>
        [/#list]
         
    [/#if]
 
</div>
 
 
[#function ensureLinkEndsWithDotHtml link]
 
    [#if !link?contains(".html")]
        [#if link?ends_with("/")]
            [#assign link = link?keep_before_last('/')]
        [/#if]
        [#return link+'.html']
    [/#if]      
     
    [#return link]
     
[/#function]
 
 
[#function eventNameFallback eventContentMap]
 
    [#if eventContentMap?has_content]
        [#if eventContentMap.name?has_content]
             [#return eventContentMap.name]
        [/#if]
        [#if eventContentMap.name?has_content]
             [#return eventContentMap.name]
        [/#if]
        [#assign eventNode = cmsfn.asJCRNode(eventContentMap)]
        [#if eventNode.name?has_content]
             [#return eventNode.name]
        [/#if]
    [/#if]
     
[/#function]