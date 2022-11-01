[#assign page = cmsfn.page(content)]
[#assign pageLink = cmsfn.link(page)]
[#assign pageLink = pageLink?keep_before_last('/') + "/book-details.html"]

[#assign books = navfn.navItemsFromApp("books", "/", "book")]


[#list books]
    <div class="row" style="margin: 2rem 0">
        [#items as book]
            <div class="col-md-4" style="text-align: center">
                [#assign assetLink = damfn.getAssetLink(book.imageLink)!]
                
                <div>   
                    [#if assetLink?has_content]
                        <img class="img-responsive" src="${assetLink}"  style="border-radius: 10px; margin: 0 auto;">
                    [/#if]
                </div>
                <div style="padding: 0 0.5rem">
               
                    [#assign linkWithSelector = ensureLinkEndsWithDotHtml(pageLink)?replace('.html','~'+book.@name+'~.html')]
                    
                    <a href="${linkWithSelector}"><h3 >${book.name}</h3></a>
                </div>
            </div>
        [/#items]
    </div>
[/#list]

[#function ensureLinkEndsWithDotHtml link]
 
    [#if !link?contains(".html")]
        [#if link?ends_with("/")]
            [#assign link = link?keep_before_last('/')]
        [/#if]
        [#return link+'.html']
    [/#if]      
     
    [#return link]
     
[/#function]