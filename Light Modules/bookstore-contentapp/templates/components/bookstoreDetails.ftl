[#assign page = cmsfn.page(content)]
[#assign pageLink = cmsfn.link(page)]
[#assign pageLinkBook = pageLink?keep_before_last('/') + "/book-details.html"]

[#assign bookstoreSelectorName = state.getSelector()]
[#if bookstoreSelectorName == ""]
    [#assign bookSelectorName = "Librer-a-Venecia"]
[/#if]

[#assign bookstoreNode = cmsfn.contentByPath("/"+(bookstoreSelectorName!), "bookstores")]

[#assign books = navfn.navItemsFromApp("books", "/", "book")]

[#if bookstoreSelectorName?has_content && bookstoreNode?has_content]
    <h1 style="margin: 1rem 0; text-align: center; padding: 0.5rem 1.5rem; background: grey; border-radius: 10px; color: #fff; letter-spacing: 5px">${bookstoreNode.name}</h1>
    [@listBooksByBookstore books=books bookstoreId=bookstoreNode.@id /]  
[/#if]


[#macro listBooksByBookstore books bookstoreId]
        [#list books]
            <div class="row" style="margin: 2rem 0">
                [#items as book]
                    [#if book.bookstore==bookstoreId]
                    <div class="col-md-4" style="text-align: center;">
                        [#assign assetLink = damfn.getAssetLink(book.imageLink)!]
                        
                        <div>   
                            [#if assetLink?has_content]
                                <img class="img-responsive" src="${assetLink}"  style="border-radius: 10px; margin: 0 auto;">
                            [/#if]
                        </div>
                        <div style="padding: 0 0.5rem">
                    
                            [#assign linkWithSelector = ensureLinkEndsWithDotHtml(pageLinkBook)?replace('.html','~'+book.@name+'~.html')]
                            
                            <a href="${linkWithSelector}"><h3 >${book.name}</h3></a>
                        </div>
                    </div>
                    [/#if]
                [/#items]
            </div>
        [/#list]
[/#macro]
[#function ensureLinkEndsWithDotHtml link]
 
    [#if !link?contains(".html")]
        [#if link?ends_with("/")]
            [#assign link = link?keep_before_last('/')]
        [/#if]
        [#return link+'.html']
    [/#if]      
     
    [#return link]
     
[/#function]