package cc.unitmesh.devti.custom.variable

class SpecVariableResolver(val key: String, val value: String) : VariableResolver {
    override val type: CustomVariableType get() = CustomVariableType.SPEC_VARIABLE

    override fun resolve(): String = value

    override fun variableName(): String = key
}
