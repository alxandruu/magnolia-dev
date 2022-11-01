
    [#if eventSelectorName?has_content && eventNode?has_content]
        <div class="panel panel-info">
        [#assign event = eventNode]
        <div class="panel-heading">
            <h1 class="panel-title">
                [#if event.name?has_content]
                    ${event.name}
                [/#if]
            </h1>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-4">
                    [#assign assetLink = damfn.getAssetLink(event.imageLink, "small")!]
                    [#if assetLink?has_content]
                        <img class="img-responsive" src="${assetLink}" alt="" style="border-radius: 50%">
                    [/#if]
                </div>
                <div class="col-md-8">
                    <h6>[#if event.date?has_content][${event.date?string["dd.MM.YY"]}][/#if]</h6>
                    <div style="margin: 1rem 0">${event.location!}</div>
                    [#if event.description?has_content]
                        <div>${cmsfn.decode(event).description!}</div>
                    [/#if]
                </div>
                
            </div>
        </div>
        </div>
    
    [/#if]
