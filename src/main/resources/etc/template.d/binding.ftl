<#ftl strip_whitespace = true>

<#assign charset="UTF-8">

<#setting number_format="computer">

<#import "macros.ftl" as macros>

<title>${binding}</title>

{{page>auto-svg}}

== Things ==
| Models  | <#list models as model>[[<@macros.model_start_path prefix=link_prefix binding=binding model=model/>]] \\  </#list>|
| Bridges | <#list bridges as bridge>[[<@macros.bridge_start_path prefix=link_prefix binding=binding bridge=bridge/>]] \\  </#list>|
| Devices | <#list things as thing>[[<@macros.thing_start_path prefix=link_prefix binding=binding thing=thing/>]] \\  </#list>|
