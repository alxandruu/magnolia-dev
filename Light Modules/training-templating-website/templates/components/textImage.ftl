[#if  content.imageLink?has_content || content.imageCaption?has_content ]
 
    <div class="default-text-image">
        [#-- Resolve the 'imageLink' property to an asset-rendition -> getting resized image --]         
        [#if content.imageLink?has_content]
            [#assign assetLink = damfn.getAssetLink(content.imageLink, "medium")!]
            [#if assetLink?has_content]
                <img class="img-responsive" src="${assetLink}" alt="">
            [/#if]
        [/#if]
        [#if content.imageCaption?has_content]
            <div style="font-size: 1rem; padding: 1rem; letter-spacing: 0.5rem">${content.imageCaption}</div>
        [/#if]
    </div> 
 
[#elseif cmsfn.editMode]
    <div>No Content defined.</div>
[/#if]