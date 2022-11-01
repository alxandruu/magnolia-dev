[#assign page = cmsfn.page(content)]


[#assign bookstores = navfn.navItemsFromApp("bookstores", "/", "bookstore")]


[#list bookstores]
   
        [#items as bookstore]
           <ul>
                <li>${bookstore.name}</li>
                <ul>
                </ul>
           </ul>
        [/#items]
   
[/#list]
