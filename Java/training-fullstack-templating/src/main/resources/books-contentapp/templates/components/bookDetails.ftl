[#assign page = cmsfn.page(content)]
[#assign pageLink = cmsfn.link(page)]


[#assign bookSelectorName = state.getSelector()]
[#if bookSelectorName == ""]
    [#assign bookSelectorName = "Winter-of-the-World"]
[/#if]
[#assign bookNode = cmsfn.contentByPath("/"+(bookSelectorName!), "books")]


[#if bookSelectorName?has_content && bookNode?has_content]
[#assign book = bookNode]
    <div style="padding: 0.51rem">
         [#assign assetLink = damfn.getAssetLink(book.imageLink)!]
        <img class="img-responsive" src="${assetLink!'#'}"  style="border-radius: 10px; margin: 0 auto; float: left; margin: 0 2rem">
    
        <h1>${book.name!}</h1>
        <div>${cmsfn.decode(book).description!}</div>
    </div>
[/#if]

