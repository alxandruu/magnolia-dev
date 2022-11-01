[#assign targetPageLink = content.targetPageLink!]

[#if targetPageLink?has_content]
    [#assign targetPage = cmsfn.contentById(content.targetPageLink)!]
    [#if targetPage?has_content]
        <div class="default-teaser">        
            <h1><a href="${cmsfn.link(targetPage)!}" style="text-decoration: none;">${i18n['teaser.link.readon']}</a></h1>
            <p>${targetPage.abstract!}</a></p>
            [#if content.imageLink?has_content]
                [#assign asset = damfn.getAsset(content.imageLink)!]
                [#if asset?has_content && asset.link?has_content]
                    <img class="img-responsive" src="${asset.link}" alt="">
                    [#if content.imageCaption?has_content]
                        <div style="font-size: 1rem; padding: 1rem; letter-spacing: 0.5rem">${content.imageCaption}</div>
                    [/#if]
                [/#if]
            [/#if]
        </div>
    [#elseif cmsfn.editMode]
        <div>Target Page could not be resolved.</div>
    [/#if]
 
 
[#elseif cmsfn.editMode]
    <div>No target page has been chosen, please open dialog and do so.</div>
[/#if]