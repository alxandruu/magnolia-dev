<div >
    <h1>${content.text}</h1>
    [#if content.rich_text?has_content]
        <div>${cmsfn.decode(content).rich_text!}</div>
    [/#if]
    <div style="margin: 1rem auto;">
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
</div>