[#assign page = cmsfn.page(content)]
[#assign pageLink = cmsfn.link(page)]

[#assign eventsRoot = cmsfn.contentByPath("/customersEvents/", "events")]
[#assign events = cmsfn.children(eventsRoot,"event")![]]

[#assign eventSelectorName = state.getSelector()]
[#assign eventNode = cmsfn.contentByPath("/customersEvents/"+(eventSelectorName!), "events")]
[#assign eventNodeName = eventNode.name!""]

[#include "/training-events-contentapp/templates/components/scripts/viewEvent.ftl" /]


[#list events]
    [#if eventNode?has_content ]
        [#assign counter = 1]
    [#else]
        [#assign counter = 0]
    [/#if]
   
    [#items as event]
        [#if !(eventNodeName == event.name) ]
            [#assign counter++]
            [#assign linkWithSelector = ensureLinkEndsWithDotHtml(pageLink)?replace('.html','~'+event.@name+'~.html')]
            <div class="row" style="padding-top: 3rem">
                [#if counter % 2 == 0 ]
                    <div class="col-md-8">
                        [#if linkWithSelector?has_content ]    
                            <h6><a href="${linkWithSelector}">Event #${event_index+1}:   [#if event.date?has_content][${event.date?string["dd.MM.YY"]}][/#if]</a></h6>
                            [#if event.name?has_content]
                                <h4 style="margin: 1rem 0"><a href="${linkWithSelector}" >${event.name}</a></h4>
                            [/#if]
                        [#else]
                            <h6>Event #${event_index+1}:   [#if event.date?has_content][${event.date?string["dd.MM.YY"]}][/#if]</h6>
                            [#if event.name?has_content]
                                <h4 style="margin: 1rem 0">${event.name}</h4>
                            [/#if]
                        [/#if]
                        <div style="margin: 1rem 0">${event.location!}</div>
                        [#if event.description?has_content]
                            <div>${cmsfn.decode(event).description!}</div>
                        [/#if]
                        [#if linkWithSelector?has_content ]
                            <a href="${linkWithSelector}">Go to Event</a>
                        [/#if]
                        [#assign contactData = cmsfn.children(event.contacts)![]]
                        [#list contactData ]
                            [#items as contact]
                                ${contact.firstName},
                            [/#items]
                        [/#list]
                    </div>
                    <div class="col-md-4">
                        [#assign assetLink = damfn.getAssetLink(event.imageLink, "small")!]
                        [#if assetLink?has_content]
                            <img class="img-responsive" src="${assetLink}" alt="" style="border-radius: 10px">
                        [/#if]
                    </div>
                [#else]
                    <div class="col-md-4">
                        [#assign assetLink = damfn.getAssetLink(event.imageLink, "small")!]
                        [#if assetLink?has_content]
                            <img class="img-responsive" src="${assetLink}" alt="" style="border-radius: 10px">
                        [/#if]
                    </div>
                    <div class="col-md-8">
                        [#if linkWithSelector?has_content ]    
                            <h6><a href="${linkWithSelector}">Event #${event_index+1}:   [#if event.date?has_content][${event.date?string["dd.MM.YY"]}][/#if]</a></h6>
                            [#if event.name?has_content]
                                <h4 style="margin: 1rem 0"><a href="${linkWithSelector}" >${event.name}</a></h4>
                            [/#if]
                        [#else]
                            <h6>Event #${event_index+1}:   [#if event.date?has_content][${event.date?string["dd.MM.YY"]}][/#if]</h6>
                            [#if event.name?has_content]
                                <h4 style="margin: 1rem 0">${event.name}</h4>
                            [/#if]
                        [/#if]
                        <div style="margin: 1rem 0">${event.location!}</div>
                        [#if event.description?has_content]
                            <div>${cmsfn.decode(event).description!}</div>
                        [/#if]
                        [#if linkWithSelector?has_content ]
                            <a href="${linkWithSelector}">Go to Event</a>
                        [/#if]
                    </div>
                [/#if]
            </div>
        [/#if]
    [/#items]
[/#list]

[#if eventSelectorName?has_content && eventNode?has_content && pageLink?has_content]
    <h5 style="margint-top: 2rem">
        <a href="${pageLink!}" >Show all events</a>
    </h5>
[/#if]



[#function ensureLinkEndsWithDotHtml link]
 
    [#if !link?contains(".html")]
        [#if link?ends_with("/")]
            [#assign link = link?keep_before_last('/')]
        [/#if]
        [#return link+'.html']
    [/#if]      
     
    [#return link]
     
[/#function]