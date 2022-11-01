[#-- Get the site definition --]
[#assign site = sitefn.site()!]
 
[#-- Get the theme configuration defined for this site --]
[#assign theme = sitefn.theme(site)!]
 
[#-- Same code as it was (and still is) in the 'defaultPageScript.ftl' --]
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="${content.description!content.abstract!}">
<meta name="keywords" content="${content.keywords!}">
<meta name="author" content="">


[#if content.imageIcon?has_content]
    [#assign assetLink = damfn.getAssetLink(content.imageIcon, "small")!]
    [#if assetLink?has_content]
        <link rel="icon" type="image/x-icon" href="${assetLink}">
    [/#if]
[/#if]

[#-- Access from the page's content: The page's 'navigationTitle' OR if non-existing, the page's 'title' OR if non-existing, the page's node name. --]
<title>${content.windowTitle!content.navigationTitle!content.title!content.@name}</title>
 
[#-- Including CSS manually:
<link rel="stylesheet" href="${ctx.contextPath}/.resources/training-templating-website/webresources/bootstrap.css" media="screen">
--]
 
[#list theme.cssFiles as cssFile]

<link rel="stylesheet" href="${cssFile.link}" media="${cssFile.media}" />

[/#list]

[#-- Using the 'resfn' for a more dynamic approach. --]

[#-- ${resfn.cachedCss("/training-templating-website.*css", "media='all'")} --]