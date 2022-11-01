<ul class="list-group">
    [#list components as component ]
        <h6>Component Number: '${component?counter}'; Is Last Component: '${component?is_last?string}'</h6>
        <li class="list-group-item" style="margin: 1rem 0;">
            [@cms.component content=component /]
        </li>
    [/#list]
</ul>