<#macro binding_path prefix binding>${prefix}:bindings:${binding}:</#macro>
<#macro model_start_path prefix binding model>${prefix}:bindings:${binding}:models:${model}:start</#macro>
<#macro bridge_start_path prefix binding bridge>${prefix}:bindings:${binding}:bridges:${bridge}:start</#macro>
<#macro thing_start_path prefix binding thing>${prefix}:bindings:${binding}:things:${thing}:start</#macro>

<#macro list_channels channels><#list channels as channel>${channel.uid.segments[4]}\\ </#list></#macro>

<#macro graphviz_include filename>{{${filename}}}
</#macro>

<#macro variables>==== Variables ====
<#list .data_model?keys as key>
${key}
</#list>
</#macro>