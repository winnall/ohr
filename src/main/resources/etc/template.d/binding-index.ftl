<#ftl strip_whitespace = true>

<#assign charset="UTF-8">

<#import "macros.ftl" as macros>

{{page>auto-svg}}

<#list bindings as binding>
- [[<@macros.binding_path prefix=link_prefix binding=binding />|${binding}]] \\
</#list>
