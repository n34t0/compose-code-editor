package io.github.n34t0.compose.stubs

import io.github.n34t0.platform.CodeCompletionElement
import io.github.n34t0.platform.GotoDeclarationData
import io.github.n34t0.platform.Platform
import io.github.n34t0.platform.Project
import io.github.n34t0.platform.impl.GTDData

internal val isStub: Boolean = java.lang.Boolean.getBoolean("platform.stub")

internal class PlatformStub : Platform {

    override fun init() {}

    override fun stop() {}

    override fun openProject(rootFolder: String): Project = ProjectStub()

    override fun openFile(filePath: String): Project = ProjectStub()

}

internal class ProjectStub : Project {

    override fun addLibraries(p0: MutableList<String>) {}

    override fun synchronizeProjectDirectory() {}

    var count = 0
    var list = ArrayList<CodeCompletionElement>()

    override fun getCodeCompletion(p0: String, p1: Int): List<CodeCompletionElement> {
        repeat(3) {
            count++
            list.add(CcElementStub("Sorted$count"))
        }
        return list
    }

    override fun gotoDeclaration(path: String?, caretOffset: Int): GotoDeclarationData =
        GTDData.NON_NAVIGATABLE

    override fun closeProject() {}

}

internal class CcElementStub(
    private val name: String = "Name",
    private val type: String = "Type",
    private val tail: String = "Tail"
) : CodeCompletionElement {

    override fun getName(): String = name

    override fun getType(): String = type

    override fun getTail(): String = tail

    override fun toString(): String = "$type $name $tail"

}
