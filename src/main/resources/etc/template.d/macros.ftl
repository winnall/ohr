<#macro binding_path prefix binding>${link_prefix}:bindings:${binding}:</#macro>
<#macro model_start_path prefix binding model>${link_prefix}:bindings:${binding}:models:${model}:start</#macro>
<#macro bridge_start_path prefix binding bridge>${link_prefix}:bindings:${binding}:bridges:${bridge}:start</#macro>
<#macro thing_start_path prefix binding thing>${link_prefix}:bindings:${binding}:things:${thing}:start</#macro>

<#macro graphviz_include filename>{{${filename}}}
</#macro>

<#macro variables>==== Variables ====
<#list .data_model?keys as key>
${key}
</#list>
</#macro>