<div class="row">
    <div class="col-md-6">
        <div>
            <h1 style="margin-left: 2rem">
                ${content.title!content.@name}
            </h1>

            [#if content.abstract?has_content]
                <div style="margin: 0  2.2rem; text-align: left">
                    ${content.abstract}
                </div>
            [/#if]
        </div>
    </div>
    <div class="col-md-6">
        [#if content.headerImage?has_content]
            [#assign assetLink = damfn.getAssetLink(content.headerImage, "medium")!]
            [#if assetLink?has_content]
                <img class="img-responsive" src="${assetLink}">
            [/#if]
        [/#if]
    </div>
</div>