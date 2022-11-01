[#if content.targetFolderId?has_content]
    [#assign targetFolderNodeId = content.targetFolderId! /]
    [#assign targetFolderNode = cmsfn.contentById(targetFolderNodeId, "dam")! /]
     
    [#if targetFolderNode?has_content]
    <div>       
        <h5>${targetFolderNode.title!targetFolderNode.@name}</h5>
         
        [#list cmsfn.children(targetFolderNode, "mgnl:asset")]
            <ul>
                [#items as childNode]
                  <li><a href="${cmsfn.link(childNode)!}">${childNode.title!childNode.@name}</a></li>
                [/#items]
            </ul>
        [#else]
            <div>The folder has not assets</div>
        [/#list]    
             
 
        <br/>TODO 2: <br/>
        - If there are not assets in the folder, inform the Author about it.<br/>
                          
    </div>   
    [#elseif cmsfn.editMode]
        <div>The link to the target folder-node could not be resolved. Check if the target folder-node still exists.</div>
    [/#if]
[#elseif cmsfn.editMode]
    <div>No target folder-node link is defined. Please open the dialog and choose a target folder-node containing assets.</div>
[/#if]