[#assign page = cmsfn.page(content)]

[#assign pageLink = cmsfn.link(page)]
[#assign pageLinkBook = pageLink?keep_before_last('/') + "/book-details.html"]
[#assign pageLinkBookstore = pageLink?keep_before_last('/') + "/see-bookstore-books.html"]
[#assign books = navfn.navItemsFromApp("books", "/", "book")]
[#assign bookstores = navfn.navItemsFromApp("bookstores", "/", "bookstore")]


[#list bookstores]
        [#items as bookstore]
           <ul>
                [#assign linkWithSelector = ensureLinkEndsWithDotHtml(pageLinkBookstore)?replace('.html','~'+bookstore.@name+'~.html')]
                <li><a href="${linkWithSelector}">${bookstore.name} - [#if bookstore.publicStore ] Pública [#else] Privada[/#if]</a></li>
                <ul>
                        [@listBooksByBookstore books=books bookstoreId=bookstore.@id /]                        
                </ul>
           </ul>
        [/#items]
   
[/#list]


[#macro listBooksByBookstore books bookstoreId]
        [#list books]
                [#items as book]
                        [#if book.bookstore==bookstoreId]
                                [#assign linkWithSelector = ensureLinkEndsWithDotHtml(pageLinkBook)?replace('.html','~'+book.@name+'~.html')]
                                <li><a href="${linkWithSelector}">${book.name}</a></li>
                        [/#if]
                [/#items]
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